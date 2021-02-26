package com.particle.game.block.furnace;

import com.particle.api.inject.RequestStaticInject;
import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.component.ECSComponent;
import com.particle.core.ecs.component.ECSComponentHandler;
import com.particle.core.ecs.module.ECSModule;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.common.components.NBTTagCompoundComponent;
import com.particle.game.common.modules.TransformModule;
import com.particle.game.player.craft.RecipeManager;
import com.particle.game.player.inventory.service.InventoryAPIProxy;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.entity.Entity;
import com.particle.model.inventory.FurnaceInventory;
import com.particle.model.inventory.Inventory;
import com.particle.model.item.ItemStack;
import com.particle.model.item.types.ItemPrototype;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;
import com.particle.model.nbt.NBTTagCompound;

import javax.inject.Inject;
import java.util.Map;
import java.util.TreeMap;

@RequestStaticInject
public class FurnaceFuelModule extends ECSModule {

    private static final ECSComponentHandler<FurnaceFuelComponent> FURNACE_FUEL_COMPONENT_HANDLER = ECSComponentHandler.buildHandler(FurnaceFuelComponent.class);
    private static final ECSComponentHandler<NBTTagCompoundComponent> NBT_TAG_COMPOUND_COMPONENT_HANDLER = ECSComponentHandler.buildHandler(NBTTagCompoundComponent.class);


    private static final ECSModuleHandler<FurnaceFuelModule> FURNACE_FUEL_MODULE_HANDLER = ECSModuleHandler.buildHandler(FurnaceFuelModule.class);
    private static final ECSModuleHandler<TransformModule> TRANSFORM_MODULE_HANDLER = ECSModuleHandler.buildHandler(TransformModule.class);

    @Inject
    private static LevelService levelService;

    @Inject
    private static RecipeManager recipeManager;

    @Inject
    private static InventoryAPIProxy inventoryServiceProxy;

    private static final Class[] REQUEST_COMPONENTS = new Class[]{FurnaceFuelComponent.class, NBTTagCompoundComponent.class};

    private Entity entity;

    private FurnaceFuelComponent furnaceFuelComponent;
    private NBTTagCompoundComponent nbtTagCompoundComponent;

    private static final Map<ItemPrototype, Short> FUEL_BURN_TIME_DICT = new TreeMap<ItemPrototype, Short>() {{
        put(ItemPrototype.COAL, (short) 1600);
        put(ItemPrototype.COAL_BLOCK, (short) 16000);

        put(ItemPrototype.LOG, (short) 300);
        put(ItemPrototype.PLANKS, (short) 300);
        put(ItemPrototype.SAPLING, (short) 100);

        put(ItemPrototype.WOODEN_AXE, (short) 200);
        put(ItemPrototype.WOODEN_PICKAXE, (short) 200);
        put(ItemPrototype.WOODEN_SWORD, (short) 200);
        put(ItemPrototype.WOODEN_SHOVEL, (short) 200);
        put(ItemPrototype.WOODEN_HOE, (short) 200);

        put(ItemPrototype.STICK, (short) 100);

        put(ItemPrototype.FENCE, (short) 300);
        put(ItemPrototype.FENCE_GATE, (short) 300);
        put(ItemPrototype.SPRUCE_FENCE_GATE, (short) 300);
        put(ItemPrototype.BIRCH_FENCE_GATE, (short) 300);
        put(ItemPrototype.JUNGLE_FENCE_GATE, (short) 300);
        put(ItemPrototype.ACACIA_FENCE_GATE, (short) 300);
        put(ItemPrototype.DARK_OAK_FENCE_GATE, (short) 300);

        put(ItemPrototype.OAK_STAIRS, (short) 300);
        put(ItemPrototype.SPRUCE_STAIRS, (short) 300);
        put(ItemPrototype.BIRCH_STAIRS, (short) 300);
        put(ItemPrototype.JUNGLE_STAIRS, (short) 300);

        put(ItemPrototype.TRAPDOOR, (short) 300);
        put(ItemPrototype.CRAFTING_TABLE, (short) 300);
        put(ItemPrototype.BOOKSHELF, (short) 300);
        put(ItemPrototype.CHEST, (short) 300);

        put(ItemPrototype.BUCKET, (short) 20000);
    }};


    // ----- 业务操作 -----

    /**
     * 检查燃料
     *
     * @param inventory
     * @return
     */
    public boolean checkFuel(Inventory inventory) {
        // 当前已经燃烧完毕
        if (this.getBurnTime() <= 0) {
            ItemStack output = recipeManager.matchFurnaceRecipe(inventory);

            // 可以燃烧
            if (output != null) {
                // 燃料处理
                ItemStack fuel = inventoryServiceProxy.getItem(inventory, FurnaceInventory.FuelIndex);
                short maxTime = FurnaceFuelModule.getFuelTime(fuel);
                if (maxTime <= 0) {
                    // 删除组件
                    FURNACE_FUEL_MODULE_HANDLER.removeModule(entity);
                    return false;
                }

                // 更新方块状态
                updateBlockState(this.entity, this.entity.getLevel(), true);

                // 更新component状态
                this.setMaxFuelTime(maxTime);
                this.setBurnTime(maxTime);

                // 消耗燃料
                fuel.setCount(fuel.getCount() - 1);
                // 如果是桶，校验是否是熔岩桶
                if (fuel.getCount() <= 0
                        && fuel.getItemType() == ItemPrototype.BUCKET
                        && fuel.getMeta() == 10) {
                    fuel.setMeta(0);
                    fuel.setCount(1);
                }
                inventoryServiceProxy.setItem(inventory, FurnaceInventory.FuelIndex, fuel);
            }
        }

        return true;
    }

    /**
     * 替换方块
     *
     * @param entity
     * @param level
     * @param lit
     */
    public static void updateBlockState(Entity entity, Level level, boolean lit) {
        // 替换方块
        TransformModule transformModule = TRANSFORM_MODULE_HANDLER.getModule(entity);
        Vector3f positionf = transformModule.getPosition();
        Vector3 position = new Vector3(positionf.getFloorX(),
                positionf.getFloorY(), positionf.getFloorZ());
        BlockPrototype blockPrototype = levelService.getBlockTypeAt(level, position);

        if (lit && blockPrototype == BlockPrototype.FURNACE) {
            Block block = levelService.getBlockAt(level, position);
            Block newBlock = Block.getBlock(BlockPrototype.LIT_FURNACE);
            newBlock.setMeta(block.getMeta());
            levelService.setBlockAt(level, newBlock, position);
        } else if (!lit && blockPrototype == BlockPrototype.LIT_FURNACE) {
            Block block = levelService.getBlockAt(level, position);
            Block newBlock = Block.getBlock(BlockPrototype.FURNACE);
            newBlock.setMeta(block.getMeta());
            levelService.setBlockAt(level, newBlock, position);
        }

    }


    // ----- 属性操作 -----

    /**
     * 获取燃料的燃烧时间
     *
     * @param fuel
     * @return
     */
    public static short getFuelTime(ItemStack fuel) {
        if (!FUEL_BURN_TIME_DICT.containsKey(fuel.getItemType())) {
            return 0;
        } else if (fuel.getItemType() != ItemPrototype.BUCKET || fuel.getMeta() == 10) {
            return FUEL_BURN_TIME_DICT.get(fuel.getItemType());
        } else {
            return 0;
        }
    }

    public short getBurnTime() {
        return this.furnaceFuelComponent.getBurnTime();
    }

    public void setBurnTime(short burnTime) {
        this.furnaceFuelComponent.setBurnTime(burnTime);
    }

    public short getMaxFuelTime() {
        return this.furnaceFuelComponent.getMaxFuelTime();
    }

    public void setMaxFuelTime(short maxFuelTime) {
        this.furnaceFuelComponent.setMaxFuelTime(maxFuelTime);
    }

    @Override
    public Class<? extends ECSComponent>[] requestComponents() {
        return REQUEST_COMPONENTS;
    }

    @Override
    protected void bindGameObject(GameObject gameObject) {
        this.furnaceFuelComponent = FURNACE_FUEL_COMPONENT_HANDLER.getComponent(gameObject);
        this.nbtTagCompoundComponent = NBT_TAG_COMPOUND_COMPONENT_HANDLER.getComponent(gameObject);

        NBTTagCompound nbtTagCompound = this.nbtTagCompoundComponent.getNbtTagCompound();
        nbtTagCompound.setShort("BurnTime", this.furnaceFuelComponent.getBurnTime());

        this.entity = (Entity) gameObject;
    }
}
