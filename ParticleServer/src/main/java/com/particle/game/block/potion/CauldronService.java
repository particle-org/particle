package com.particle.game.block.potion;

import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.block.brewing.CauldronComponent;
import com.particle.game.block.brewing.CauldronModule;
import com.particle.game.common.modules.TransformModule;
import com.particle.game.item.ItemAttributeService;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.Block;
import com.particle.model.entity.Entity;
import com.particle.model.item.ItemStack;
import com.particle.model.item.types.DyeType;
import com.particle.model.item.types.ItemPrototype;
import com.particle.model.math.Vector3;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class CauldronService {

    private static final ECSModuleHandler<TransformModule> TRANSFORM_MODULE_HANDLER = ECSModuleHandler.buildHandler(TransformModule.class);

    private static final ECSModuleHandler<CauldronModule> CAULDRON_MODULE_HANDLER = ECSModuleHandler.buildHandler(CauldronModule.class);


    @Inject
    private LevelService levelService;

    /**
     * 判断酿药锅是否空的
     *
     * @param cauldron
     * @return
     */
    public boolean isEmpty(Block cauldron) {
        return cauldron.getMeta() == 0;
    }

    /**
     * 判断酿药锅是否滿的
     *
     * @param cauldron
     * @return
     */
    public boolean isFull(Block cauldron) {
        return cauldron.getMeta() == 6;
    }

    /**
     * 获取药水id
     *
     * @param entity
     * @return
     */
    public int getPotionId(Entity entity) {
        CauldronModule cauldronModule = CAULDRON_MODULE_HANDLER.getModule(entity);

        if (cauldronModule == null) {
            return -1;
        }
        return cauldronModule.getPotionId();
    }

    /**
     * 处理桶
     *
     * @param player
     * @param block
     * @param entity
     * @param bucket
     * @return
     */
    public ItemStack handleBucket(Player player, Block block, Entity entity, ItemStack bucket) {
        CauldronModule cauldronModule = CAULDRON_MODULE_HANDLER.getModule(entity);
        if (cauldronModule == null) {
            return null;
        }

        TransformModule transformModule = TRANSFORM_MODULE_HANDLER.getModule(entity);
        if (transformModule == null) {
            return null;
        }

        Vector3 position = new Vector3(transformModule.getPosition());


        if (bucket.getMeta() == 0 && this.isWater(block, cauldronModule) && isFull(block)) {
            // 空桶装水
            this.clear(entity);
            block.setMeta(0);
            this.levelService.setBlockAt(player.getLevel(), block, position);
            // todo 水的声音

            // 消耗一个桶
            bucket.setCount(bucket.getCount() - 1);

            // 返回一个水桶
            ItemStack resultBucket = ItemStack.getItem(ItemPrototype.BUCKET, 1);
            resultBucket.setMeta(8);
            return resultBucket;
        } else if (bucket.getMeta() == 8) {
            // 水桶装坩埚
            if (!isFull(block)) {
                block.setMeta(6);
                this.levelService.setBlockAt(player.getLevel(), block, position);
            }

            cauldronModule.setExistCustomColor(false);
            cauldronModule.setCustomColor(CauldronComponent.DEFAULT_VALUE);
            // todo 水的声音

            // 消耗一个桶
            bucket.setCount(bucket.getCount() - 1);

            // 返回一个空桶
            ItemStack resultBucket = ItemStack.getItem(ItemPrototype.BUCKET, 1);
            resultBucket.setMeta(0);
            return resultBucket;
        } else if (bucket.getMeta() == 10) {
            // 岩漿桶
            if (!isFull(block)) {
                block.setMeta(6);
                this.levelService.setBlockAt(player.getLevel(), block, position);
            }
            cauldronModule.setExistCustomColor(false);
            cauldronModule.setCustomColor(CauldronComponent.DEFAULT_VALUE);

            // 消耗一个桶
            bucket.setCount(bucket.getCount() - 1);

            // 返回一个空桶
            ItemStack resultBucket = ItemStack.getItem(ItemPrototype.BUCKET, 1);
            resultBucket.setMeta(0);
            return resultBucket;
        }
        return null;
    }

    /**
     * 处理染料
     *
     * @param player
     * @param block
     * @param entity
     * @param dye
     * @return
     */
    public ItemStack handleDye(Player player, Block block, Entity entity, ItemStack dye) {
        CauldronModule cauldronModule = CAULDRON_MODULE_HANDLER.getModule(entity);
        if (cauldronModule == null) {
            return null;
        }

        if (this.isEmpty(block)) {
            return null;
        } else if (!this.isWater(block, cauldronModule)) {
            return null;
        }

        DyeType dyeType = DyeType.from(dye.getMeta());
        if (dyeType == null) {
            return null;
        }
        int color = dyeType.getColorHex();
        this.setCustomColor(entity, color);

        dye.setCount(dye.getCount() - 1);

        return null;
    }

    /**
     * 处理皮革
     *
     * @param player
     * @param block
     * @param entity
     * @param leather
     * @return
     */
    public ItemStack handleLeather(Player player, Block block, Entity entity, ItemStack leather) {
        CauldronModule cauldronModule = CAULDRON_MODULE_HANDLER.getModule(entity);
        if (cauldronModule == null) {
            return null;
        }

        if (this.isEmpty(block)) {
            return null;
        } else if (!this.isWater(block, cauldronModule)) {
            return null;
        }

        TransformModule transformModule = TRANSFORM_MODULE_HANDLER.getModule(entity);
        if (transformModule == null) {
            return null;
        }

        Vector3 position = new Vector3(transformModule.getPosition());
        block.setMeta(block.getMeta() - 2);
        if (block.getMeta() == 0) {
            cauldronModule.reset();
        }
        this.levelService.setBlockAt(player.getLevel(), block, position);

        // 消耗原物品
        leather.setCount(0);

        int itemColor = ItemAttributeService.getColor(leather);
        // 染色
        if (cauldronModule.isExistCustomColor()) {
            int cauldronColor = cauldronModule.getCustomColor();
            if (itemColor != cauldronColor) {
                ItemStack resultItem = leather.clone();
                ItemAttributeService.setColor(resultItem, cauldronColor);
                resultItem.setCount(1);
                return resultItem;
            }
        } else if (itemColor != -1) {
            // 洗掉颜色
            ItemStack resultItem = leather.clone();
            ItemAttributeService.removeColor(resultItem);
            resultItem.setCount(1);
            return resultItem;
        }

        return null;
    }

    /**
     * 处理空瓶子
     *
     * @param player
     * @param block
     * @param entity
     * @param glassBottle
     * @return
     */
    public ItemStack handleGlassBottle(Player player, Block block, Entity entity, ItemStack glassBottle) {
        CauldronModule cauldronModule = CAULDRON_MODULE_HANDLER.getModule(entity);
        if (cauldronModule == null) {
            return null;
        }

        if (this.isEmpty(block)) {
            return null;
        }

        TransformModule transformModule = TRANSFORM_MODULE_HANDLER.getModule(entity);
        if (transformModule == null) {
            return null;
        }
        Vector3 position = new Vector3(transformModule.getPosition());
        block.setMeta(block.getMeta() - 2);
        if (block.getMeta() == 0) {
            cauldronModule.reset();
        }
        this.levelService.setBlockAt(player.getLevel(), block, position);

        glassBottle.setCount(glassBottle.getCount() - 1);

        // 药水
        if (this.isPotion(block, cauldronModule)) {
            int potionId = cauldronModule.getPotionId();
            ItemStack resultItem = ItemStack.getItem(ItemPrototype.POTION, 1);
            resultItem.setMeta(potionId);
            return resultItem;
        } else {
            // 水或者染料的水
            return ItemStack.getItem(ItemPrototype.POTION, 1);
        }
    }

    /**
     * 处理弓箭
     *
     * @param player
     * @param block
     * @param entity
     * @param arrow
     * @return
     */
    public ItemStack handleArrow(Player player, Block block, Entity entity, ItemStack arrow) {
        CauldronModule cauldronModule = CAULDRON_MODULE_HANDLER.getModule(entity);
        if (cauldronModule == null) {
            return null;
        }

        if (!this.isPotion(block, cauldronModule)) {
            return null;
        }

        TransformModule transformModule = TRANSFORM_MODULE_HANDLER.getModule(entity);
        if (transformModule == null) {
            return null;
        }
        Vector3 position = new Vector3(transformModule.getPosition());


        int potionId = cauldronModule.getPotionId();
        potionId = potionId + 1 > 0 ? potionId + 1 : 0;
        if (potionId < 6) {
            return null;
        }

        ItemStack resultItem = arrow.clone();
        resultItem.setMeta(potionId);

        arrow.setCount(0);

        int meta = block.getMeta();
        int arrowCounts = cauldronModule.getArrowPotionCounts();
        if (arrowCounts-- <= 0) {
            meta = meta - 2;
            block.setMeta(meta);
            cauldronModule.setArrowPotionCounts(CauldronComponent.DEFAULT_ARROW_COUNTS);
            this.levelService.setBlockAt(player.getLevel(), block, position);
        } else {
            cauldronModule.setArrowPotionCounts(arrowCounts);
        }

        return resultItem;
    }


    /**
     * 添加药水
     *
     * @param player
     * @param block
     * @param entity
     * @param potion
     * @return
     */
    public ItemStack handlePotion(Player player, Block block, Entity entity, ItemStack potion) {
        CauldronModule cauldronModule = CAULDRON_MODULE_HANDLER.getModule(entity);
        if (cauldronModule == null) {
            return null;
        }

        int potionId = potion.getMeta();
        if (this.isWater(block, cauldronModule) && potionId > 0 ||
                this.isPotion(block, cauldronModule) && potionId == 0) {
            this.clear(entity);

            potion.setCount(0);

            return ItemStack.getItem(ItemPrototype.GLASS_BOTTLE, 1);
        }

        int originMeta = block.getMeta();
        int meta = originMeta + 2;
        meta = meta > 6 ? 6 : meta;
        block.setMeta(meta);
        // 只有在有变化的时候，才通知
        if (meta != originMeta) {
            TransformModule transformModule = TRANSFORM_MODULE_HANDLER.getModule(entity);
            if (transformModule == null) {
                return null;
            }
            Vector3 position = new Vector3(transformModule.getPosition());
            this.levelService.setBlockAt(player.getLevel(), block, position);
        } else if (meta == 6) {
            cauldronModule.setArrowPotionCounts(CauldronComponent.DEFAULT_ARROW_COUNTS);
        }

        if (potionId > 0) {
            cauldronModule.setPotionId(potion.getMeta());
            if (potion.getItemType() != ItemPrototype.POTION) {
                cauldronModule.setSplashPotion(true);
            }
            // todo 添加药水的声音
        } else {
            cauldronModule.setExistCustomColor(false);
            cauldronModule.setCustomColor(CauldronComponent.DEFAULT_VALUE);
            // todo 添加水的声音
        }

        potion.setCount(0);
        potion = ItemStack.getItem(ItemPrototype.GLASS_BOTTLE, 1);
        return potion;
    }

    /**
     * 是否是水
     *
     * @param block
     * @param cauldronModule
     * @return
     */
    private boolean isWater(Block block, CauldronModule cauldronModule) {
        int originPotionId = cauldronModule.getPotionId();
        if (!this.isEmpty(block) && originPotionId == CauldronComponent.DEFAULT_VALUE) {
            return true;
        }
        return false;
    }

    /**
     * 是否是药水
     *
     * @param block
     * @param cauldronModule
     * @return
     */
    private boolean isPotion(Block block, CauldronModule cauldronModule) {
        int originPotionId = cauldronModule.getPotionId();
        if (!this.isEmpty(block) && originPotionId != CauldronComponent.DEFAULT_VALUE) {
            return true;
        }
        return false;
    }

    /**
     * 是否喷溅药水
     *
     * @param entity
     * @return
     */
    public boolean isSplashPotion(Entity entity) {
        CauldronModule cauldronModule = CAULDRON_MODULE_HANDLER.getModule(entity);
        if (cauldronModule == null) {
            return false;
        }
        return cauldronModule.isSplashPotion();
    }

    /**
     * 可否被活塞推动
     *
     * @param entity
     * @return
     */
    public boolean isMovable(Entity entity) {
        CauldronModule cauldronModule = CAULDRON_MODULE_HANDLER.getModule(entity);
        if (cauldronModule == null) {
            return false;
        }
        return cauldronModule.isMovable();
    }

    /**
     * 设置是否可被活塞推动
     *
     * @param entity
     * @param movable
     */
    public void setMovable(Entity entity, boolean movable) {
        CauldronModule cauldronModule = CAULDRON_MODULE_HANDLER.getModule(entity);
        if (cauldronModule == null) {
            return;
        }
        cauldronModule.setMovable(movable);
    }

    /**
     * 获得物品列表
     *
     * @param entity
     * @return
     */
    public List<ItemStack> getItems(Entity entity) {
        CauldronModule cauldronModule = CAULDRON_MODULE_HANDLER.getModule(entity);
        if (cauldronModule == null) {
            return new ArrayList<>();
        }
        return cauldronModule.getItems();
    }

    /**
     * 添加物品
     *
     * @param entity
     * @param itemStack
     */
    public void addItem(Entity entity, ItemStack itemStack) {
        CauldronModule cauldronModule = CAULDRON_MODULE_HANDLER.getModule(entity);
        if (cauldronModule == null) {
            return;
        }
        cauldronModule.addItem(itemStack);
    }

    /**
     * 删除物品
     *
     * @param entity
     * @param itemStack
     */
    public void removeItem(Entity entity, ItemStack itemStack) {
        CauldronModule cauldronModule = CAULDRON_MODULE_HANDLER.getModule(entity);
        if (cauldronModule == null) {
            return;
        }
        cauldronModule.removeItem(itemStack);
    }

    /**
     * 获取颜色
     *
     * @param entity
     * @return
     */
    public int getCustomColor(Entity entity) {
        CauldronModule cauldronModule = CAULDRON_MODULE_HANDLER.getModule(entity);
        if (cauldronModule == null) {
            return -1;
        }
        if (cauldronModule.isExistCustomColor()) {
            return cauldronModule.getCustomColor();
        } else {
            return -1;
        }

    }

    /**
     * 设置颜色
     *
     * @param entity
     * @param customColor
     */
    public void setCustomColor(Entity entity, int customColor) {
        CauldronModule cauldronModule = CAULDRON_MODULE_HANDLER.getModule(entity);
        if (cauldronModule == null) {
            return;
        }
        cauldronModule.setExistCustomColor(true);
        cauldronModule.setCustomColor(customColor);
    }

    /**
     * 是否存在颜色
     *
     * @param entity
     * @return
     */
    public boolean isExistCustomColor(Entity entity) {
        CauldronModule cauldronModule = CAULDRON_MODULE_HANDLER.getModule(entity);
        if (cauldronModule == null) {
            return false;
        }
        return cauldronModule.isExistCustomColor();
    }

    /**
     * 清理
     *
     * @param entity
     */
    public void clear(Entity entity) {
        CauldronModule cauldronModule = CAULDRON_MODULE_HANDLER.getModule(entity);
        if (cauldronModule == null) {
            return;
        }
        cauldronModule.reset();
    }
}
