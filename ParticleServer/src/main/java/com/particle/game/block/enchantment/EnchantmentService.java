package com.particle.game.block.enchantment;

import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.entity.attribute.explevel.ExperienceService;
import com.particle.game.item.ItemAttributeService;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.InventoryAPIProxy;
import com.particle.game.player.inventory.service.impl.ContainerInventoryAPI;
import com.particle.model.inventory.Inventory;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.item.ItemStack;
import com.particle.model.item.enchantment.Enchantment;
import com.particle.model.item.enchantment.Enchantments;
import com.particle.model.item.types.ItemPrototype;
import com.particle.model.item.types.ItemTag;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EnchantmentService {

    private static final ECSModuleHandler<EnchantmentModule> ENCHANTMENT_MODULE_HANDLER = ECSModuleHandler.buildHandler(EnchantmentModule.class);

    @Inject
    private ExperienceService experienceService;

    @Inject
    private InventoryAPIProxy inventoryAPIProxy;

    @Inject
    private InventoryManager inventoryManager;

    @Inject
    private ContainerInventoryAPI containerInventoryAPI;

    private EnchantmentsRandom enchantmentsRandom = new EnchantmentsRandom();

    /**
     * 初始化组件
     *
     * @param player
     */
    public void initComponents(Player player) {
        ENCHANTMENT_MODULE_HANDLER.bindModule(player);
    }

    /**
     * 标记客户端的附魔物品，以在后面的步骤中清除
     *
     * @param player
     * @param itemStack
     */
    public void markEnchantItem(Player player, ItemStack itemStack) {
        EnchantmentModule enchantmentModule = ENCHANTMENT_MODULE_HANDLER.getModule(player);

        if (enchantmentModule != null) {
            enchantmentModule.setCurrentEnchantItem(itemStack);
        }
    }

    /**
     * 判断物品是否为附魔物品
     *
     * @param player
     * @param itemStack
     * @return
     */
    public boolean checkEnchantItem(Player player, ItemStack itemStack) {
        EnchantmentModule enchantmentModule = ENCHANTMENT_MODULE_HANDLER.getModule(player);

        if (enchantmentModule != null) {
            if (enchantmentModule.getCurrentEnchantItem() == null) {
                return false;
            }

            return itemStack.equalsAll(enchantmentModule.getCurrentEnchantItem());
        }

        return false;
    }

    public void resetEnchantItem(Player player) {
        EnchantmentModule enchantmentModule = ENCHANTMENT_MODULE_HANDLER.getModule(player);

        if (enchantmentModule != null) {
            enchantmentModule.setCurrentEnchantItem(null);
        }
    }

    public void enchant(Player player, short level) {
        // 校验是否有经验消耗
        this.experienceService.confirmLevelSpaend(player);

        player.getLevel().getLevelSchedule().scheduleSimpleTask("EnchantItem", () -> {
            Inventory enchantInventory = this.inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_ENCHANT);

            ItemStack itemStack = this.inventoryAPIProxy.getItem(enchantInventory, 0);
            ItemAttributeService.removeEnchantments(itemStack);

            // 附魔书
            if (itemStack.getItemType() == ItemPrototype.BOOK) {
                itemStack = ItemStack.getItem(ItemPrototype.ENCHANTED_BOOK);
            }

            // 执行附魔操作
            this.enchantItem(itemStack, level);

            // 添加物品到背包
            this.inventoryAPIProxy.setItem(enchantInventory, 0, itemStack);

            // 关闭界面
            this.containerInventoryAPI.removeView(player, enchantInventory, true);
        });
    }

    /**
     * 附魔物品
     *
     * @param itemStack 物品
     * @param level     等级
     */
    public void enchantItem(ItemStack itemStack, short level) {
        // 获得第一个附魔
        Enchantment enchantment1 = this.getEnchantment(itemStack.getItemType(), level);
        if (enchantment1 != null) {
            ItemAttributeService.addEnchantments(itemStack, enchantment1);
        }

        // 附魔数只允许一次附魔
        if (itemStack.getItemType() == ItemPrototype.ENCHANTED_BOOK) {
            return;
        }

        // 70% 概率获得第二个附魔
        if (Math.random() < 0.5) {
            this.addAdditationEnchantment(itemStack, level);
        }

        // 30% 概率获得第三个附魔
        if (Math.random() < 0.1) {
            this.addAdditationEnchantment(itemStack, level);
        }
    }

    private void addAdditationEnchantment(ItemStack itemStack, short level) {
        Enchantment additationEnchantment = this.getEnchantment(itemStack.getItemType(), level);
        if (additationEnchantment != null) {
            if (ItemAttributeService.hasEnchantment(itemStack, additationEnchantment.getType())) {
                Enchantment enchantment = ItemAttributeService.getEnchantment(itemStack, additationEnchantment.getType());

                enchantment.setLevel((short) (enchantment.getLevel() + 1));

                ItemAttributeService.addEnchantments(itemStack, enchantment);
            } else {
                ItemAttributeService.addEnchantments(itemStack, additationEnchantment);
            }
        }
    }

    private Enchantment getEnchantment(ItemPrototype itemPrototype, short level) {
        if (itemPrototype.hasTag(ItemTag.TOOL_AXE)) {
            Enchantments enchantment = this.enchantmentsRandom.getEnchantment(ItemTag.TOOL_AXE);

            if (enchantment != null) {
                return new Enchantment(enchantment, level);
            }
        } else if (itemPrototype.hasTag(ItemTag.TOOL_SWORD)) {
            Enchantments enchantment = this.enchantmentsRandom.getEnchantment(ItemTag.TOOL_SWORD);

            if (enchantment != null) {
                return new Enchantment(enchantment, level);
            }
        } else if (itemPrototype.hasTag(ItemTag.TOOL_PICKAXE)) {
            Enchantments enchantment = this.enchantmentsRandom.getEnchantment(ItemTag.TOOL_PICKAXE);

            if (enchantment != null) {
                return new Enchantment(enchantment, level);
            }
        } else if (itemPrototype.hasTag(ItemTag.TOOL_SHOVEL)) {
            Enchantments enchantment = this.enchantmentsRandom.getEnchantment(ItemTag.TOOL_SHOVEL);

            if (enchantment != null) {
                return new Enchantment(enchantment, level);
            }
        } else if (itemPrototype.hasTag(ItemTag.TOOL_BOW)) {
            Enchantments enchantment = this.enchantmentsRandom.getEnchantment(ItemTag.TOOL_BOW);

            if (enchantment != null) {
                return new Enchantment(enchantment, level);
            }
        } else if (itemPrototype.hasTag(ItemTag.TOOL_CROSSBOW)) {
            Enchantments enchantment = this.enchantmentsRandom.getEnchantment(ItemTag.TOOL_CROSSBOW);

            if (enchantment != null) {
                return new Enchantment(enchantment, level);
            }
        } else if (itemPrototype.hasTag(ItemTag.TOOL_TRIDENT)) {
            Enchantments enchantment = this.enchantmentsRandom.getEnchantment(ItemTag.TOOL_TRIDENT);

            if (enchantment != null) {
                return new Enchantment(enchantment, level);
            }
        } else if (itemPrototype.hasTag(ItemTag.ARMOR_HELMET)) {
            Enchantments enchantment = this.enchantmentsRandom.getEnchantment(ItemTag.ARMOR_HELMET);

            if (enchantment != null) {
                return new Enchantment(enchantment, level);
            }
        } else if (itemPrototype.hasTag(ItemTag.ARMOR_CHESTPLATE)) {
            Enchantments enchantment = this.enchantmentsRandom.getEnchantment(ItemTag.ARMOR_CHESTPLATE);

            if (enchantment != null) {
                return new Enchantment(enchantment, level);
            }
        } else if (itemPrototype.hasTag(ItemTag.ARMOR_LEGGINGS)) {
            Enchantments enchantment = this.enchantmentsRandom.getEnchantment(ItemTag.ARMOR_LEGGINGS);

            if (enchantment != null) {
                return new Enchantment(enchantment, level);
            }
        } else if (itemPrototype.hasTag(ItemTag.ARMOR_BOOTS)) {
            Enchantments enchantment = this.enchantmentsRandom.getEnchantment(ItemTag.ARMOR_BOOTS);

            if (enchantment != null) {
                return new Enchantment(enchantment, level);
            }
        } else if (itemPrototype.hasTag(ItemTag.ENCHANTMENT_BOOK)) {
            Enchantments enchantment = this.enchantmentsRandom.getEnchantment(ItemTag.ENCHANTMENT_BOOK);

            if (enchantment != null) {
                return new Enchantment(enchantment, level);
            }
        }

        return null;
    }
}
