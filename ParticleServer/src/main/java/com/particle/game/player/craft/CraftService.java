package com.particle.game.player.craft;

import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.event.dispatcher.EventDispatcher;
import com.particle.game.player.craft.components.CraftModule;
import com.particle.game.player.inventory.InventoryManager;
import com.particle.game.player.inventory.service.InventoryAPIProxy;
import com.particle.model.events.level.player.PlayerCraftEvent;
import com.particle.model.inventory.PlayerInventory;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.inventory.recipe.Recipe;
import com.particle.model.inventory.recipe.ShapedRecipe;
import com.particle.model.inventory.recipe.ShapelessRecipe;
import com.particle.model.item.ItemStack;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.LinkedList;
import java.util.List;

@Singleton
public class CraftService {

    private static final ECSModuleHandler<CraftModule> CRAFT_MODULE_HANDLER = ECSModuleHandler.buildHandler(CraftModule.class);

    @Inject
    private RecipeManager recipeManager;

    @Inject
    private InventoryAPIProxy inventoryServiceProxy;

    @Inject
    private InventoryManager inventoryManager;

    private EventDispatcher eventDispatcher = EventDispatcher.getInstance();

    private static final Logger logger = LoggerFactory.getLogger(CraftService.class);

    public void init(Player player) {
        CRAFT_MODULE_HANDLER.bindModule(player);
    }

    public void addInputItem(Player player, ItemStack itemStack, int slot) {
        CraftModule craftModule = CRAFT_MODULE_HANDLER.getModule(player);
        if (craftModule == null) {
            return;
        }

        craftModule.addCraftInputs(itemStack, slot);
    }

    /**
     * 获取输出物品
     * <p>
     * // TODO: 2019/6/23 对额外返还物品的处理
     *
     * @param player
     * @return
     */
    public ItemStack getOutput(Player player) {
        CraftModule craftModule = CRAFT_MODULE_HANDLER.getModule(player);
        if (craftModule == null) {
            return null;
        }

        return craftModule.getCraftOutput();
    }

    public void resetCraftData(Player player) {
        CraftModule craftModule = CRAFT_MODULE_HANDLER.getModule(player);
        if (craftModule == null) {
            return;
        }

        craftModule.clearInputs();
        craftModule.clearOutputs();
    }

    /**
     * 处理合成操作
     *
     * @param player
     * @return
     */
    public boolean processCrafting(Player player, int type) {
        CraftModule craftModule = CRAFT_MODULE_HANDLER.getModule(player);
        if (craftModule == null) {
            return false;
        }

        ItemStack[] havedInputs = craftModule.getCraftInputs();

        // 如果是四宫格，则转成9宫格
        if (type == 0) {
            havedInputs[4] = havedInputs[3];
            havedInputs[3] = havedInputs[2];
            havedInputs[2] = null;
        }

        // 缓存数据
        List<ItemStack> inputList = new LinkedList<>();
        for (ItemStack itemStack : havedInputs) {
            if (itemStack != null) {
                inputList.add(itemStack);
            }
        }

        // 搜索该合成可能的产出
        List<Recipe> craftingOutputs = this.recipeManager.getCraftingOutputs(havedInputs);
        if (craftingOutputs == null) {
            return false;
        }

        // 对比是否有合适的产出
        for (Recipe recipe : craftingOutputs) {
            boolean state = this.checkRecipe(craftModule, recipe, inputList);

            // 若对比成过则返回成功
            if (state) {
                PlayerCraftEvent playerCraftEvent = new PlayerCraftEvent(player, recipe, inputList, craftModule.getCraftOutput());

                this.eventDispatcher.dispatchEvent(playerCraftEvent);

                return !playerCraftEvent.isCancelled();
            }
        }

        return false;
    }

    private boolean checkRecipe(CraftModule craftModule, Recipe workbenchRecipe, List<ItemStack> inputList) {
        if (workbenchRecipe == null) {
            return false;
        }

        List<ItemStack> requireInputs = new LinkedList<>();

        // 配置输入数据要求
        if (workbenchRecipe instanceof ShapedRecipe) {
            ShapedRecipe shapedRecipe = (ShapedRecipe) workbenchRecipe;
            requireInputs.addAll(shapedRecipe.getInputCost());
        } else if (workbenchRecipe instanceof ShapelessRecipe) {
            ShapelessRecipe shapelessRecipe = (ShapelessRecipe) workbenchRecipe;
            requireInputs.addAll(shapelessRecipe.getInputs());
        }

        // 校验输入
        int checkState = this.check(requireInputs, inputList);

        // 校验通过，返回true
        if (checkState > 0) {
            ItemStack targetItem = workbenchRecipe.getOutput().clone();
            // 设置物品数量
            targetItem.setCount(checkState * targetItem.getCount());
            logger.info("targetItem:" + targetItem.getCount());
            craftModule.setCraftOutputs(targetItem);
            if (workbenchRecipe instanceof ShapedRecipe) {
                ShapedRecipe shapedRecipe = (ShapedRecipe) workbenchRecipe;
                for (ItemStack itemStack : shapedRecipe.getExtraOutput()) {
                    craftModule.addExternalCraftOutputs(itemStack);
                }
            }
        }

        return checkState > 0;
    }

    /**
     * 校验输入
     *
     * @param requireInputs
     * @param havedInputs
     * @return
     */
    private int check(List<ItemStack> requireInputs, List<ItemStack> havedInputs) {
        havedInputs = new LinkedList<>(havedInputs);

        // 合成的倍数
        int multiple = -1;
        for (ItemStack requireItem : requireInputs) {
            boolean state = true;
            for (int i = 0; i < havedInputs.size(); i++) {
                ItemStack input = havedInputs.get(i);
                if ((requireItem.getMeta() == -1 && requireItem.getItemType() == input.getItemType()) || input.equals(requireItem)) {
                    havedInputs.remove(i);

                    state = false;
                    int currentMul = input.getCount() / requireItem.getCount();
                    if (multiple == -1) {
                        multiple = currentMul;
                    } else {
                        multiple = currentMul < multiple ? currentMul : multiple;
                    }
                    break;
                }
            }

            if (state) {
                // 合成表中存在物品无法通过校验，返回false
                return -1;
            }
        }

        return multiple;
    }

    /**
     * 回收合成输入框中的物品
     *
     * @param player
     * @return
     */
    public boolean recycleCompoundInput(Player player) {
        CraftModule craftModule = CRAFT_MODULE_HANDLER.getModule(player);
        if (craftModule == null) {
            return false;
        }

        ItemStack[] havedInputs = craftModule.getCraftInputs();
        PlayerInventory playerInventory = (PlayerInventory) inventoryManager.
                getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_PLAYER);
        for (ItemStack itemStack : havedInputs) {
            if (itemStack != null) {
                inventoryServiceProxy.addItem(playerInventory, itemStack);
            }
        }
        return true;
    }
}
