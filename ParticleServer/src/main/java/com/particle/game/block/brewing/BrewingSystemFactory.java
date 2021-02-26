package com.particle.game.block.brewing;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.module.ECSModule;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.core.ecs.system.ECSSystem;
import com.particle.core.ecs.system.ECSSystemFactory;
import com.particle.game.block.common.components.CookComponent;
import com.particle.game.block.common.modules.CookModule;
import com.particle.game.block.tile.TileEntityService;
import com.particle.game.common.modules.NBTTagCompoundModule;
import com.particle.game.player.craft.RecipeManager;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.modules.SingleContainerModule;
import com.particle.game.player.inventory.service.InventoryAPIProxy;
import com.particle.model.entity.Entity;
import com.particle.model.entity.model.tile.TileEntity;
import com.particle.model.inventory.BrewingInventory;
import com.particle.model.inventory.Inventory;
import com.particle.model.item.ItemStack;
import com.particle.model.item.types.ItemPrototype;
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
public class BrewingSystemFactory implements ECSSystemFactory<BrewingSystemFactory.BrewingSystem> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BrewingSystemFactory.class);

    private static final ECSModuleHandler<BrewingFuelModule> BREWING_FUEL_MODULE_HANDLER = ECSModuleHandler.buildHandler(BrewingFuelModule.class);
    private static final ECSModuleHandler<CookModule> COOK_MODULE_HANDLER = ECSModuleHandler.buildHandler(CookModule.class);
    private static final ECSModuleHandler<NBTTagCompoundModule> NBT_TAG_COMPOUND_MODULE_HANDLER = ECSModuleHandler.buildHandler(NBTTagCompoundModule.class);
    private static final ECSModuleHandler<SingleContainerModule> SINGLE_CONTAINER_MODULE_HANDLER = ECSModuleHandler.buildHandler(SingleContainerModule.class);


    @Inject
    private NetworkManager networkManager;

    @Inject
    private InventoryAPIProxy inventoryServiceProxy;

    @Inject
    private InventoryManager inventoryManager;

    @Inject
    private RecipeManager recipeManager;

    @Inject
    private TileEntityService tileEntityService;

    public class BrewingSystem implements ECSSystem {

        private Entity entity;
        private BrewingFuelModule brewingFuelModule;
        private CookModule cookModule;
        private NBTTagCompoundModule nbtTagCompoundModule;
        private SingleContainerModule singleContainerModule;

        BrewingSystem(Entity entity) {
            this.entity = entity;
            this.brewingFuelModule = BREWING_FUEL_MODULE_HANDLER.getModule(entity);
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
            if (!this.checkFuel((TileEntity) this.entity, inventory, this.brewingFuelModule)) {
                return;
            }

            // 配方
            if (this.cookModule.getStatus() == CookComponent.STATUS_END) {
                return;
            }
            int cookTime = this.cookModule.getCookTime();
            if (this.cookModule.getStatus() == CookComponent.STATUS_INVALID) {
                // 立刻结束
                this.startBrewing(inventory, 0);
                this.cookModule.setStatus(CookComponent.STATUS_END);
                return;
            } else if (this.cookModule.getStatus() == CookComponent.STATUS_START) {
                ItemStack result = recipeManager.matchBrewingRecipe(inventory);
                if (result != null) {
                    cookTime = CookComponent.MAX_BREWING_TIME;

                    this.startBrewing(inventory, cookTime);
                    this.cookModule.setStatus(CookComponent.STATUS_RUNNING);
                } else {
                    this.cookModule.setStatus(CookComponent.STATUS_END);
                    return;
                }
            }

            int tickRate = (int) (deltaTime / 50);
            if (tickRate > 40) tickRate = 40;


            cookTime -= tickRate;
            this.cookModule.setCookTime(Math.max(cookTime, 0));

            LOGGER.debug("-{}---cooktime[{}]", System.currentTimeMillis(), cookTime);
            // 酿造完成
            if (cookTime <= 0) {
                ItemStack result = recipeManager.matchBrewingRecipe(inventory);
                if (result == null) {
                    // 去除component
                    // 每一次完成后更新燃料
                    this.brewingFuelModule.setFuelAmount(this.brewingFuelModule.getFuelAmount() - 1);
                    this.updateFuel(inventory, this.brewingFuelModule);
                    this.cookModule.setStatus(CookComponent.STATUS_END);
                    return;
                }
                // 输入
                ItemStack ingredient = inventoryServiceProxy.getItem(inventory, BrewingInventory.IngredientIndex);
                ingredient.setCount(ingredient.getCount() - 1);
                inventoryServiceProxy.setItem(inventory, BrewingInventory.IngredientIndex, ingredient);

                // 药水
                ItemStack potion1 = inventoryServiceProxy.getItem(inventory, BrewingInventory.POTION1);
                if (potion1.getItemType() == ItemPrototype.POTION) {
                    inventoryServiceProxy.setItem(inventory, BrewingInventory.POTION1, result);
                }
                ItemStack potion2 = inventoryServiceProxy.getItem(inventory, BrewingInventory.POTION2);
                if (potion2.getItemType() == ItemPrototype.POTION) {
                    inventoryServiceProxy.setItem(inventory, BrewingInventory.POTION2, result);
                }
                ItemStack potion3 = inventoryServiceProxy.getItem(inventory, BrewingInventory.POTION3);
                if (potion3.getItemType() == ItemPrototype.POTION) {
                    inventoryServiceProxy.setItem(inventory, BrewingInventory.POTION3, result);
                }
                LOGGER.info("brewing succeed! result:{}", result);
            }
        }

        /**
         * 检查燃料
         *
         * @param entity
         * @param inventory
         * @param brewingFuelModule
         * @return
         */
        private boolean checkFuel(TileEntity entity, Inventory inventory, BrewingFuelModule brewingFuelModule) {
            if (brewingFuelModule != null && brewingFuelModule.getFuelAmount() <= 0) {
                // 燃料处理
                ItemStack fuel = inventoryServiceProxy.getItem(inventory, BrewingInventory.FuelIndex);
                if (fuel.getItemType() == ItemPrototype.BLAZE_POWDER && fuel.getCount() > 0) {
                    brewingFuelModule.setFuelAmount(BrewingFuelComponent.MAX_FUEL_BE_USED_COUNTS);
                    // 将燃料减1
                    fuel.setCount(fuel.getCount() - 1);
                    inventoryServiceProxy.setItem(inventory, BrewingInventory.FuelIndex, fuel);
                    this.updateFuel(inventory, brewingFuelModule);
                    return true;
                } else if (fuel.getItemType() == ItemPrototype.AIR || fuel.getCount() <= 0) {
                    // 去除component
                    BREWING_FUEL_MODULE_HANDLER.removeModule(entity);
                    return false;
                }
            } else if (brewingFuelModule == null) {
                return false;
            }
            return true;
        }

        /**
         * 开始酿造
         * ACTION: 发包的时候,如果有数据更改，必须在循环中必须每一次new出来
         *
         * @param inventory
         * @param cookTime
         */
        private void startBrewing(Inventory inventory, int cookTime) {
            Set<Player> views = inventory.getViewers();
            if (views != null && !views.isEmpty()) {
                for (Player player : views) {
                    int containerId = inventoryManager.getMapIdFromMultiOwned(player, inventory);
                    if (containerId <= 0) {
                        continue;
                    }
                    ContainerSetDataPacket containerSetDataPacket = new ContainerSetDataPacket();
                    containerSetDataPacket.setProperty(ContainerSetDataPacket.PROPERTY_BREWING_STAND_BREW_TIME);
                    containerSetDataPacket.setValue(cookTime);
                    containerSetDataPacket.setContainerId(containerId);
                    networkManager.sendMessage(player.getClientAddress(), containerSetDataPacket);
                }
            }
        }

        /**
         * 更新燃料
         *
         * @param inventory
         * @param brewingFuelModule
         */
        private void updateFuel(Inventory inventory, BrewingFuelModule brewingFuelModule) {
            Set<Player> views = inventory.getViewers();
            if (views != null && !views.isEmpty()) {
                for (Player player : views) {
                    int containerId = inventoryManager.getMapIdFromMultiOwned(player, inventory);
                    if (containerId <= 0) {
                        continue;
                    }
                    ContainerSetDataPacket fuelTotalDataPacket = new ContainerSetDataPacket();
                    fuelTotalDataPacket.setProperty(ContainerSetDataPacket.PROPERTY_BREWING_STAND_FUEL_TOTAL);
                    fuelTotalDataPacket.setValue(brewingFuelModule.getFuelTotal());
                    fuelTotalDataPacket.setContainerId(containerId);
                    networkManager.sendMessage(player.getClientAddress(), fuelTotalDataPacket);

                    ContainerSetDataPacket fuelAmountDataPacket = new ContainerSetDataPacket();
                    fuelAmountDataPacket.setProperty(ContainerSetDataPacket.PROPERTY_BREWING_STAND_FUEL_AMOUNT);
                    fuelAmountDataPacket.setValue(brewingFuelModule.getFuelAmount());
                    fuelAmountDataPacket.setContainerId(containerId);
                    networkManager.sendMessage(player.getClientAddress(), fuelAmountDataPacket);
                }
            }

        }
    }

    @Override
    public Class<? extends ECSModule>[] getRequestServices() {
        return new Class[]{BrewingFuelModule.class, CookModule.class, NBTTagCompoundModule.class, SingleContainerModule.class};
    }

    @Override
    public BrewingSystem buildECSSystem(GameObject gameObject) {
        if (gameObject instanceof Entity) {
            return new BrewingSystem((Entity) gameObject);
        }

        return null;
    }

    @Override
    public Class<BrewingSystem> getSystemClass() {
        return BrewingSystem.class;
    }
}
