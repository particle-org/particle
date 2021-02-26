package com.particle.game.entity.attack;

import com.particle.api.attack.IEntityAttackServiceApi;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.event.dispatcher.EventDispatcher;
import com.particle.game.entity.attack.component.EntityAttackModule;
import com.particle.game.entity.attribute.health.HealthServiceProxy;
import com.particle.game.entity.movement.MovementServiceProxy;
import com.particle.game.entity.state.EntityStateService;
import com.particle.game.entity.state.model.EntityStateType;
import com.particle.game.item.DurabilityService;
import com.particle.game.item.ItemAttributeService;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.impl.PlayerInventoryAPI;
import com.particle.model.effect.EffectBaseType;
import com.particle.model.entity.Entity;
import com.particle.model.entity.model.tags.EntityTag;
import com.particle.model.entity.type.EntityStateRecorder;
import com.particle.model.events.level.entity.EntityAttackEvent;
import com.particle.model.events.level.entity.EntityDamageByEntityEvent;
import com.particle.model.events.level.entity.EntityDamageType;
import com.particle.model.inventory.ArmorInventory;
import com.particle.model.inventory.PlayerInventory;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.item.ItemStack;
import com.particle.model.item.enchantment.Enchantment;
import com.particle.model.item.enchantment.Enchantments;
import com.particle.model.item.types.ItemPrototype;
import com.particle.model.math.Vector3f;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class EntityAttackService implements IEntityAttackServiceApi {

    private static final ECSModuleHandler<EntityAttackModule> ENTITY_ATTACK_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityAttackModule.class);

    @Inject
    private EntityStateService entityStateService;
    @Inject
    private EntityKnockBackService entityKnockBackService;
    @Inject
    private MovementServiceProxy movementServiceProxy;
    @Inject
    private HealthServiceProxy healthServiceProxy;
    @Inject
    private DurabilityService durabilityService;
    @Inject
    private EntityDefenseService entityDefenseService;

    private EventDispatcher eventDispatcher = EventDispatcher.getInstance();

    @Inject
    private PlayerInventoryAPI playerInventoryAPI;
    @Inject
    private InventoryManager inventoryManager;


    // 攻击处理器

    private Map<ItemPrototype, Float> weaponDamageRecorder = new HashMap<>();

    @Inject
    private void init() {
        // 武器伤害
        this.weaponDamageRecorder.put(ItemPrototype.WOODEN_SWORD, 4f);
        this.weaponDamageRecorder.put(ItemPrototype.GOLDEN_SWORD, 4f);
        this.weaponDamageRecorder.put(ItemPrototype.STONE_SWORD, 5f);
        this.weaponDamageRecorder.put(ItemPrototype.IRON_SWORD, 6f);
        this.weaponDamageRecorder.put(ItemPrototype.DIAMOND_SWORD, 7f);

        this.weaponDamageRecorder.put(ItemPrototype.WOODEN_SHOVEL, 2f);
        this.weaponDamageRecorder.put(ItemPrototype.GOLDEN_SHOVEL, 2f);
        this.weaponDamageRecorder.put(ItemPrototype.STONE_SHOVEL, 3f);
        this.weaponDamageRecorder.put(ItemPrototype.IRON_SHOVEL, 4f);
        this.weaponDamageRecorder.put(ItemPrototype.DIAMOND_SHOVEL, 5f);

        this.weaponDamageRecorder.put(ItemPrototype.WOODEN_PICKAXE, 3f);
        this.weaponDamageRecorder.put(ItemPrototype.GOLDEN_PICKAXE, 3f);
        this.weaponDamageRecorder.put(ItemPrototype.STONE_PICKAXE, 4f);
        this.weaponDamageRecorder.put(ItemPrototype.IRON_PICKAXE, 5f);
        this.weaponDamageRecorder.put(ItemPrototype.DIAMOND_PICKAXE, 6f);

        this.weaponDamageRecorder.put(ItemPrototype.WOODEN_AXE, 4f);
        this.weaponDamageRecorder.put(ItemPrototype.GOLDEN_AXE, 4f);
        this.weaponDamageRecorder.put(ItemPrototype.STONE_AXE, 5f);
        this.weaponDamageRecorder.put(ItemPrototype.IRON_AXE, 6f);
        this.weaponDamageRecorder.put(ItemPrototype.DIAMOND_AXE, 7f);

        this.weaponDamageRecorder.put(ItemPrototype.TRIDENT, 8f);
    }

    /**
     * 初始化生物攻击组件
     *
     * @param entity
     */
    @Override
    public void initEntityAttackComponent(Entity entity) {
        this.initEntityAttackComponent(entity, 1, 1000);
    }

    /**
     * 初始化生物攻击组件
     *
     * @param entity
     * @param baseDamage
     * @param attackInterval
     */
    @Override
    public void initEntityAttackComponent(Entity entity, float baseDamage, long attackInterval) {
        EntityAttackModule entityAttackModule = ENTITY_ATTACK_MODULE_HANDLER.bindModule(entity);
        entityAttackModule.setBaseDamage(baseDamage);
        entityAttackModule.setAttackInterval(attackInterval);
    }

    @Override
    public boolean canAttack(Entity entity) {
        EntityAttackModule entityAttackModule = ENTITY_ATTACK_MODULE_HANDLER.getModule(entity);
        if (entityAttackModule == null) {
            return false;
        }

        return entityAttackModule.canAttack();
    }

    /**
     * 近战攻击
     *
     * @param source
     * @param victim
     */
    @Override
    public boolean entityCloseAttack(Entity source, Entity victim) {
        // Step 0 : 检查是否可以攻击
        EntityAttackModule entityAttackModule = ENTITY_ATTACK_MODULE_HANDLER.getModule(source);
        if (entityAttackModule == null) {
            return false;
        }

        entityAttackModule.setLastAttackTimestamp(System.currentTimeMillis());

        // 获取武器
        ItemStack weapon = ItemStack.getItem(ItemPrototype.AIR);
        PlayerInventory inventory = (PlayerInventory) this.inventoryManager.getInventoryByContainerId(source, InventoryConstants.CONTAINER_ID_PLAYER);
        if (inventory != null) {
            weapon = this.playerInventoryAPI.getItem(inventory, inventory.getItemInHandle());
        }

        // Step 1 : 伤害计算
        float damage = this.caculateCloseCombatDamage(source, victim, weapon);

        // Step 2 : 击退计算
        Vector3f knockback = this.entityKnockBackService.getCloseCombatKnockback(source, victim, weapon);

        // Step 3 : 构造Level层同步事件
        EntityAttackEvent entityAttackEvent = new EntityAttackEvent(source, victim, damage);
        this.eventDispatcher.dispatchEvent(entityAttackEvent);
        if (entityAttackEvent.isCancelled()) {
            return false;
        }

        EntityDamageByEntityEvent entityDamageByEntityEvent = new EntityDamageByEntityEvent(victim);
        entityDamageByEntityEvent.setDamage(damage);
        entityDamageByEntityEvent.setKnockback(knockback);
        entityDamageByEntityEvent.setDamager(source);
        entityDamageByEntityEvent.overrideAfterEventExecuted(() -> {
            // Step 4 : 物品耐久计算
            if (source instanceof Player) {
                this.durabilityService.consumptionItem(source);
            }

            if (victim instanceof Player) {
                this.durabilityService.consumptionEquipments(victim);
            }

            // Step 5 : 伤害应用
            this.healthServiceProxy.damageEntity(victim, entityDamageByEntityEvent.getDamage(), EntityDamageType.EntityAttack, source);
            this.movementServiceProxy.setMotion(victim, knockback);

            // Step6 : 伤害后续处理
            this.appleCloseCombatEffects(source, victim);
        });

        this.eventDispatcher.dispatchEvent(entityDamageByEntityEvent);

        return true;
    }

    /**
     * 计算攻击伤害
     *
     * @param source
     * @param victim
     * @return
     */
    private float caculateCloseCombatDamage(Entity source, Entity victim, ItemStack weapon) {
        EntityAttackModule entityAttackModule = ENTITY_ATTACK_MODULE_HANDLER.getModule(source);

        // 获取生物基础攻击力
        float damage = entityAttackModule.getBaseDamage();

        // 计算武器伤害
        float weaponDamage = this.caculateWeaponDamage(victim, weapon);
        if (weaponDamage != 0) {
            damage += weaponDamage;
        }

        // 计算状态效果加成
        damage += this.caculateCloseCombatBufferEnchant(source);

        // 计算护甲减伤效果
        // 获取背包
        ArmorInventory armorInventory = (ArmorInventory) inventoryManager.getInventoryByContainerId(victim, InventoryConstants.CONTAINER_ID_ARMOR);
        if (armorInventory != null) {
            // 计算头盔防护
            ItemStack helmet = this.playerInventoryAPI.getItem(armorInventory, ArmorInventory.HELMET);
            ItemStack chestplate = this.playerInventoryAPI.getItem(armorInventory, ArmorInventory.CHESTPLATE);
            ItemStack leggings = this.playerInventoryAPI.getItem(armorInventory, ArmorInventory.LEGGINGS);
            ItemStack boots = this.playerInventoryAPI.getItem(armorInventory, ArmorInventory.BOOTS);

            damage = this.entityDefenseService.caculateArmorDefense(helmet, chestplate, leggings, boots, damage);
        }


        return damage;
    }

    /**
     * 处理伤害后的相关效果
     *
     * @param source
     * @param victim
     */
    private void appleCloseCombatEffects(Entity source, Entity victim) {
        // 获取手持武器
        PlayerInventory inventory = (PlayerInventory) this.inventoryManager.getInventoryByContainerId(source, InventoryConstants.CONTAINER_ID_PLAYER);
        if (inventory != null) {
            ItemStack weapon = this.playerInventoryAPI.getItem(inventory, inventory.getItemInHandle());

            // 火焰附加效果
            Enchantment fireAspectEnchantment = ItemAttributeService.getEnchantment(weapon, Enchantments.FIRE_ASPECT);
            if (fireAspectEnchantment != null) {
                this.entityStateService.enableState(victim, EntityStateType.ON_FIRE.getName(), EntityStateType.ON_FIRE.getDefaultUpdateInterval(), (fireAspectEnchantment.getLevel() * 4 - 1) * 1000);
            }
        }

        // 获取护甲
        ArmorInventory armorInventory = (ArmorInventory) inventoryManager.getInventoryByContainerId(victim, InventoryConstants.CONTAINER_ID_ARMOR);
        if (armorInventory != null) {
            // 荆棘附加效果检测
            ItemStack chestplate = this.playerInventoryAPI.getItem(armorInventory, ArmorInventory.CHESTPLATE);
            Enchantment thornsEnchantment = ItemAttributeService.getEnchantment(chestplate, Enchantments.THORNS);
            if (thornsEnchantment != null && Math.random() < 0.15 * thornsEnchantment.getLevel()) {
                int damage = ((int) Math.random() * 3) + 1;

                // 伤害来源生物
                this.healthServiceProxy.damageEntity(source, damage, EntityDamageType.EntityAttack, source);
            }
        }
    }

    /**
     * 计算武器及附带附魔的伤害
     *
     * @param victim
     * @param weapon
     * @return
     */
    private float caculateWeaponDamage(Entity victim, ItemStack weapon) {
        // 获取武器基础攻击力
        Float weaponDamage = this.weaponDamageRecorder.get(weapon.getItemType());
        if (weaponDamage == null) {
            return 0;
        }

        // 计算锋利附魔加成
        Enchantment enchantment = ItemAttributeService.getEnchantment(weapon, Enchantments.SHARPNESS);
        if (enchantment != null) {
            weaponDamage = weaponDamage + 1.25f * enchantment.getLevel();
        }

        // 计算截肢杀手加成
        if (victim.hasTag(EntityTag.ARTHROPODS)) {
            enchantment = ItemAttributeService.getEnchantment(weapon, Enchantments.BANE_OF_ARTHROPODS);
            if (enchantment != null) {
                weaponDamage = weaponDamage + 2.5f * enchantment.getLevel();
            }
        }

        // 计算亡灵杀手加成
        if (victim.hasTag(EntityTag.UNDEAD)) {
            enchantment = ItemAttributeService.getEnchantment(weapon, Enchantments.SMITE);
            if (enchantment != null) {
                weaponDamage = weaponDamage + 2.5f * enchantment.getLevel();
            }
        }

        // 计算穿刺加成
        if (victim.hasTag(EntityTag.AQUATIC)) {
            enchantment = ItemAttributeService.getEnchantment(weapon, Enchantments.IMPALING);
            if (enchantment != null) {
                weaponDamage = weaponDamage + 2.5f * enchantment.getLevel();
            }
        }

        return weaponDamage;
    }

    /**
     * 计算近战附魔伤害加成
     *
     * @param source
     * @return
     */
    private float caculateCloseCombatBufferEnchant(Entity source) {
        EntityStateRecorder stateRecorder = this.entityStateService.getState(source, EffectBaseType.STRENGTH.getName());
        if (stateRecorder != null) {
            return stateRecorder.getLevel() * 3;
        }

        stateRecorder = this.entityStateService.getState(source, EffectBaseType.WEAKNESS.getName());
        if (stateRecorder != null) {
            return -stateRecorder.getLevel() * 4;
        }

        return 0;
    }
}
