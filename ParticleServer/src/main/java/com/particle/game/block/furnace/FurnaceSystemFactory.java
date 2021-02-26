package com.particle.game.block.furnace;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.module.ECSModule;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.core.ecs.system.ECSSystem;
import com.particle.core.ecs.system.ECSSystemFactory;
import com.particle.game.block.common.modules.CookModule;
import com.particle.game.common.modules.NBTTagCompoundModule;
import com.particle.game.player.craft.RecipeManager;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.modules.SingleContainerModule;
import com.particle.game.player.inventory.service.InventoryAPIProxy;
import com.particle.model.entity.Entity;
import com.particle.model.inventory.FurnaceInventory;
import com.particle.model.inventory.Inventory;
import com.particle.model.item.ItemStack;
import com.particle.model.nbt.NBTTagCompound;
import com.particle.model.network.packets.data.ContainerSetDataPacket;
import com.particle.model.player.Player;
import com.particle.network.NetworkManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Set;

@Singleton
public class FurnaceSystemFactory implements ECSSystemFactory<FurnaceSystemFactory.FurnaceSystem> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FurnaceSystemFactory.class);

    private static final ECSModuleHandler<NBTTagCompoundModule> NBT_TAG_COMPOUND_MODULE_HANDLER = ECSModuleHandler.buildHandler(NBTTagCompoundModule.class);
    private static final ECSModuleHandler<CookModule> COOK_MODULE_HANDLER = ECSModuleHandler.buildHandler(CookModule.class);
    private static final ECSModuleHandler<FurnaceFuelModule> FURNACE_FUEL_MODULE_HANDLER = ECSModuleHandler.buildHandler(FurnaceFuelModule.class);
    private static final ECSModuleHandler<SingleContainerModule> SINGLE_CONTAINER_MODULE_HANDLER = ECSModuleHandler.buildHandler(SingleContainerModule.class);

    /**
     * 每200tick炼成
     */
    private static final int COST_TIME_EVERY_TIMES = 200;

    @Inject
    private NetworkManager networkManager;

    @Inject
    private InventoryAPIProxy inventoryServiceProxy;

    @Inject
    private InventoryManager inventoryManager;

    @Inject
    private RecipeManager recipeManager;

    public class FurnaceSystem implements ECSSystem {

        private Entity entity;
        private FurnaceFuelModule furnaceFuelModule;
        private CookModule cookModule;
        private NBTTagCompoundModule nbtTagCompoundModule;
        private SingleContainerModule singleContainerModule;

        FurnaceSystem(Entity entity) {
            this.entity = entity;
            this.furnaceFuelModule = FURNACE_FUEL_MODULE_HANDLER.getModule(entity);
            this.cookModule = COOK_MODULE_HANDLER.getModule(entity);
            this.nbtTagCompoundModule = NBT_TAG_COMPOUND_MODULE_HANDLER.getModule(entity);
            this.singleContainerModule = SINGLE_CONTAINER_MODULE_HANDLER.getModule(entity);
        }

        @Override
        public void tick(long deltaTime) {
            // 基本属性
            NBTTagCompound nbtTagCompound = this.nbtTagCompoundModule.getNbtTagCompound();
            Inventory inventory = this.singleContainerModule.getInventory();

            // 燃料
            if (!this.furnaceFuelModule.checkFuel(inventory)) {
                FurnaceFuelModule.updateBlockState(this.entity, this.entity.getLevel(), false);
                // 更新燃烧状态
                this.updateFurnaceData(inventory, this.cookModule, 0, 0);
                // 存储数据
                this.cookModule.setCookTime(0);
                nbtTagCompound.setShort("CookTime", (short) 0);
                nbtTagCompound.setShort("BurnTime", (short) 0);
                return;
            }

            int tickRate = (int) (deltaTime / 50);
            if (tickRate > 20) tickRate = 20;
            short burnTime = furnaceFuelModule.getBurnTime();
            int cookTime = this.cookModule.getCookTime();
            if (burnTime > 0) {
                burnTime -= tickRate;
                ItemStack output = recipeManager.matchFurnaceRecipe(inventory);
                if (output != null) {
                    cookTime += tickRate;
                    if (cookTime >= COST_TIME_EVERY_TIMES) {
                        inventoryServiceProxy.setItem(inventory, FurnaceInventory.ResultIndex, output);
                        ItemStack input = inventoryServiceProxy.getItem(inventory, FurnaceInventory.SmeltingIndex);
                        input.setCount(input.getCount() - 1);
                        inventoryServiceProxy.setItem(inventory, FurnaceInventory.SmeltingIndex, input);
                        cookTime -= COST_TIME_EVERY_TIMES;
                    }
                } else {
                    cookTime = 0;
                }
            } else {
                FurnaceFuelModule.updateBlockState(this.entity, this.entity.getLevel(), false);
                burnTime = 0;
                cookTime = 0;
            }

            // 更新燃烧状态
            this.updateFurnaceData(inventory, this.cookModule, cookTime,
                    this.getBurnDuration(burnTime, this.furnaceFuelModule.getMaxFuelTime()));
            // 存储数据
            this.furnaceFuelModule.setBurnTime(burnTime);
            this.cookModule.setCookTime(cookTime);
            nbtTagCompound.setShort("CookTime", (short) cookTime);
            nbtTagCompound.setShort("BurnTime", burnTime);
        }

        /**
         * 获取燃烧刻度
         *
         * @param burnTime
         * @param maxTime
         * @return
         */
        private int getBurnDuration(short burnTime, short maxTime) {
            if (maxTime == 0) {
                return 0;
            }
            return (int) Math.ceil(burnTime * 200 / maxTime);
        }

        /**
         * 更新熔炉的燃烧数据
         *
         * @param inventory
         * @param cookModule
         * @param cookTime
         * @param burnDuration
         */
        private void updateFurnaceData(Inventory inventory, CookModule cookModule, int cookTime, int burnDuration) {
            Set<Player> players = inventory.getViewers();
            if (players != null && !players.isEmpty()) {
                for (Player player : players) {
                    int containerId = inventoryManager.getMapIdFromMultiOwned(player, inventory);
                    if (containerId <= 0) {
                        continue;
                    }
                    ContainerSetDataPacket fuelLitTimePacket = new ContainerSetDataPacket();
                    fuelLitTimePacket.setProperty(ContainerSetDataPacket.PROPERTY_FURNACE_LIT_TIME);
                    fuelLitTimePacket.setValue(burnDuration);
                    fuelLitTimePacket.setContainerId(containerId);
                    networkManager.sendMessage(player.getClientAddress(), fuelLitTimePacket);

                    // 只有在cookTime生效的时候，才发这个包
                    if (cookTime != 0 || cookModule.getCookTime() != 0) {
                        ContainerSetDataPacket fuelTotalDataPacket = new ContainerSetDataPacket();
                        fuelTotalDataPacket.setProperty(ContainerSetDataPacket.PROPERTY_FURNACE_TICK_COUNT);
                        fuelTotalDataPacket.setValue(cookTime);
                        fuelTotalDataPacket.setContainerId(containerId);
                        networkManager.sendMessage(player.getClientAddress(), fuelTotalDataPacket);
                    }
                }
            }
        }
    }

    @Override
    public Class<? extends ECSModule>[] getRequestServices() {
        return new Class[]{FurnaceFuelModule.class, CookModule.class, NBTTagCompoundModule.class, SingleContainerModule.class};
    }

    @Override
    public FurnaceSystem buildECSSystem(GameObject gameObject) {
        if (gameObject instanceof Entity) {
            return new FurnaceSystem((Entity) gameObject);
        }

        return null;
    }

    @Override
    public Class<FurnaceSystem> getSystemClass() {
        return FurnaceSystem.class;
    }
}
