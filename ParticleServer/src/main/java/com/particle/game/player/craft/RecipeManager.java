package com.particle.game.player.craft;

import com.alibaba.fastjson.JSONObject;
import com.particle.game.player.inventory.service.InventoryAPIProxy;
import com.particle.model.inventory.BrewingInventory;
import com.particle.model.inventory.FurnaceInventory;
import com.particle.model.inventory.Inventory;
import com.particle.model.inventory.recipe.*;
import com.particle.model.item.ItemStack;
import com.particle.model.item.types.ItemPrototype;
import com.particle.model.item.types.ItemPrototypeDictionary;
import com.particle.model.nbt.NBTBase;
import com.particle.model.nbt.NBTTagCompound;
import com.particle.model.nbt.NBTToHexString;
import com.particle.model.network.packets.data.CraftingDataPacket;
import com.particle.model.player.Player;
import com.particle.network.NetworkManager;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Singleton
public class RecipeManager {

    private static final Logger logger = LoggerFactory.getLogger(RecipeManager.class);

    private int recipeCounts = 0;

    /**
     * 管理所有合成台的recipe
     */
    private Map<UUID, Recipe> allWorkBenchRecipe = new HashMap<>();

    /**
     * 管理所有的熔炉的配方
     */
    private Map<String, Recipe> allFurnaceRecipes = new HashMap<>();

    /**
     * 管理所有酿造台的配方
     */
    private Map<String, Recipe> allBrewingRecipes = new HashMap<>();

    /**
     * 快速合成路由
     */
    private Map<String, List<Recipe>> quickShapedRecipesMapping = new HashMap<>();
    private Map<String, List<Recipe>> quickShapedlessRecipesMapping = new HashMap<>();

    private boolean isLoaded = false;

    @Inject
    private NetworkManager networkManager;

    @Inject
    private InventoryAPIProxy inventoryServiceProxy;

    /**
     * 加载配方
     */
    public void loadDefault() {
        if (isLoaded) {
            return;
        }
        try {
            String result = IOUtils.resourceToString("new_recipes.json", Charsets.toCharset("utf-8"), this.getClass().getClassLoader());
            if (StringUtils.isEmpty(result)) {
                logger.error("找不到配方文件");
                return;
            }
            JSONObject jsonResult = JSONObject.parseObject(result);
            List<Map<String, Object>> recipes = (List<Map<String, Object>>) jsonResult.get("recipes");
            if (recipes == null) {
                logger.error("配方文件错误");
                return;
            }
            for (Map<String, Object> recipe : recipes) {
                Integer type = (Integer) recipe.get("type");
                if (type == 0) {
                    this.addShapelessRecipe(recipe);
                } else if (type == 1) {
                    this.addShapeRecipe(recipe);
                } else if (type == 2 || type == 3) {
                    this.addFurnaceRecipe(recipe);
                } else if (type == 6) {
                    this.addBrewingRecipe(recipe);
                }

            }
            isLoaded = true;
        } catch (Exception e) {
            logger.error("加载配方错误", e);
        }
    }

    /**
     * 发送配方数据给客户端
     *
     * @param player
     * @return
     */
    public boolean sendRecipes(Player player) {
        if (!isLoaded) {
            logger.warn("配方尚未加载完成，请稍后发送配方数据至客户端");
            this.loadDefault();
        }

        CraftingDataPacket craftingDataPacket = new CraftingDataPacket();
        craftingDataPacket.setClearRecipes(false);
        Collection<Recipe> recipes = this.allWorkBenchRecipe.values();
        craftingDataPacket.addAllRecipes(recipes);

        recipes = this.allFurnaceRecipes.values();
        craftingDataPacket.addAllRecipes(recipes);

        return networkManager.sendMessage(player.getClientAddress(), craftingDataPacket);
    }

    /**
     * 添加shapelessRecipe配方
     *
     * @param recipe
     * @return
     */
    private void addShapelessRecipe(Map<String, Object> recipe) {
        Map<String, Object> first = ((List<Map<String, Object>>) recipe.get("output")).get(0);
        List<Map<String, Object>> inputs = (List<Map<String, Object>>) recipe.get("input");

        // 缓存合成配方
        ShapelessRecipe shapelessRecipe = new ShapelessRecipe();
        shapelessRecipe.setOutput(fromJson(first));
        shapelessRecipe.setUuid(this.getUuid(shapelessRecipe));
        for (Map<String, Object> input : inputs) {
            shapelessRecipe.addInputs(fromJson(input));
        }
        // 排序物品
        this.allWorkBenchRecipe.put(shapelessRecipe.getUuid(), shapelessRecipe);

        // 缓存快速合成配方
        String inputIdentified = getInputIdentified(shapelessRecipe.getInputs());
        List<Recipe> recipes = this.quickShapedlessRecipesMapping.get(inputIdentified);
        if (recipes == null) {
            recipes = new LinkedList<>();
            this.quickShapedlessRecipesMapping.put(inputIdentified, recipes);
        }
        recipes.add(shapelessRecipe);
    }

    /**
     * 添加shapedRecipe
     *
     * @param recipe
     */
    private void addShapeRecipe(Map<String, Object> recipe) {
        List<Map> output = (List<Map>) recipe.get("output");
        // 主输出
        Map<String, Object> first = output.remove(0);
        ItemStack outputItem = fromJson(first);

        // 配方
        String[] shape = ((List<String>) recipe.get("shape")).stream().toArray(String[]::new);

        // 额外输出
        List<ItemStack> extraOutput = new ArrayList<>();

        // 输入
        Map<Character, ItemStack> inputs = new HashMap<>();

        Map<String, Map<String, Object>> inputJsons = (Map) recipe.get("input");
        for (Map.Entry<String, Map<String, Object>> entry : inputJsons.entrySet()) {
            char ingredientChar = entry.getKey().charAt(0);
            ItemStack input = fromJson(entry.getValue());
            inputs.put(ingredientChar, input);
        }
        for (Map<String, Object> data : output) {
            extraOutput.add(fromJson(data));
        }

        this.addShapeRecipe(shape, inputs, outputItem, extraOutput);
    }

    public void addShapeRecipe(String[] shape, Map<Character, ItemStack> inputs, ItemStack outputItem, List<ItemStack> extraOutput) {
        // 缓存合成配方
        ShapedRecipe shapedRecipe = new ShapedRecipe();
        shapedRecipe.setOutput(outputItem);
        shapedRecipe.addExtraOutput(extraOutput);
        shapedRecipe.setInputs(shape, inputs);
        shapedRecipe.setUuid(this.getUuid(shapedRecipe));
        this.allWorkBenchRecipe.put(shapedRecipe.getUuid(), shapedRecipe);

        // 缓存快速合成配方
        ItemStack[] inputCache = new ItemStack[9];
        for (int i = 0; i < shape.length; i++) {
            char[] chars = shape[i].toCharArray();
            for (int j = 0; j < chars.length; j++) {
                inputCache[i * 3 + j] = inputs.get(chars[j]);
            }
        }
        String inputIdentified = getInputIdentified(inputCache);

        List<Recipe> recipes = this.quickShapedRecipesMapping.get(inputIdentified);
        if (recipes == null) {
            recipes = new LinkedList<>();
            this.quickShapedRecipesMapping.put(inputIdentified, recipes);
        }
        recipes.add(shapedRecipe);
    }

    /**
     * 添加熔炉的配方
     *
     * @param recipe
     */
    private void addFurnaceRecipe(Map<String, Object> recipe) {
        Map<String, Object> outputMap = (Map) recipe.get("output");
        ItemStack output = fromJson(outputMap);

        String name = (String) recipe.get("inputName");
        Integer id = (Integer) recipe.get("inputId");
        Integer meta = (Integer) recipe.get("inputDamage");
        ItemStack input = ItemStack.getItem(name);

        if (input != null) {
            input.setCount(1);
            input.setMeta(meta == null ? -1 : meta);

            FurnaceRecipe furnaceRecipe = new FurnaceRecipe();
            furnaceRecipe.setOutput(output);
            furnaceRecipe.setInput(input);

            this.allFurnaceRecipes.put(this.getFurnaceRecipe(input), furnaceRecipe);
        }
    }

    /**
     * 获取furnace的key
     *
     * @param input
     * @return
     */
    private String getFurnaceRecipe(ItemStack input) {
        StringBuilder key = new StringBuilder();
        key.append(input.getItemType().getId()).append(":");
        key.append(input.getMeta() > 0 ? input.getMeta() : "?");
        return key.toString();
    }

    /**
     * 匹配熔炉配方
     *
     * @param inventory
     * @return
     */
    public ItemStack matchFurnaceRecipe(Inventory inventory) {
        ItemStack input = inventoryServiceProxy.getItem(inventory, FurnaceInventory.SmeltingIndex);
        if (input.getItemType() == ItemPrototype.AIR || input.getCount() <= 0) {
            return null;
        }
        String key = this.getFurnaceRecipe(input);
        Recipe furnaceRecipe = this.allFurnaceRecipes.get(key);
        if (furnaceRecipe == null) {
            return null;
        }
        ItemStack result = inventoryServiceProxy.getItem(inventory, FurnaceInventory.ResultIndex);
        ItemStack output = furnaceRecipe.getOutput();
        if (result == null || result.getItemType() == ItemPrototype.AIR) {
            return output;
        } else if (result.equalsAll(output)) {
            if (result.getCount() < result.getMaxStackSize()) {
                result.setCount(result.getCount() + 1);
                return result;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 添加酿造台配方
     *
     * @param recipe
     */
    private void addBrewingRecipe(Map<String, Object> recipe) {
        Map<String, Object> outputMap = (Map) recipe.get("output");
        Map<String, Object> inputMap = (Map) recipe.get("input");
        Map<String, Object> potionMap = (Map) recipe.get("potion");
        ItemStack output = fromJson(outputMap);
        ItemStack input = fromJson(inputMap);
        ItemStack potion = fromJson(potionMap);
        BrewingRecipe brewingRecipe = new BrewingRecipe();
        brewingRecipe.setOutput(output);
        brewingRecipe.setInput(input);
        brewingRecipe.setPotion(potion);

        this.allBrewingRecipes.put(this.getBrewingKey(input, potion), brewingRecipe);
    }

    /**
     * 获取brewing的key
     *
     * @param input
     * @param potion
     * @return
     */
    private String getBrewingKey(ItemStack input, ItemStack potion) {
        StringBuilder key = new StringBuilder();
        key.append(input.getItemType().getId()).append(":");
        key.append(potion.hasMeta() ? potion.getMeta() : 0);
        return key.toString();
    }

    public List<Recipe> getCraftingOutputs(ItemStack[] itemStacks) {
        // 检索有形态的合成
        String inputIdentified = this.getInputIdentified(itemStacks);
        List<Recipe> recipes = this.quickShapedRecipesMapping.get(inputIdentified);
        if (recipes != null) {
            return recipes;
        }

        // 检索无形态的合成
        List<ItemStack> inputs = new LinkedList<>();
        for (ItemStack itemStack : itemStacks) {
            if (itemStack != null) {
                inputs.add(itemStack);
            }
        }
        inputIdentified = this.getInputIdentified(inputs);
        return this.quickShapedlessRecipesMapping.get(inputIdentified);
    }

    /**
     * 随机一个uuid
     *
     * @param recipe
     * @return
     */
    private UUID getUuid(Recipe recipe) {
        ItemStack output = recipe.getOutput();
        StringBuilder sb = new StringBuilder();
        sb.append(recipeCounts++);
        sb.append(output.getItemType().getId());
        sb.append(output.getMeta());
        sb.append(output.getCount());
        return UUID.nameUUIDFromBytes(sb.toString().getBytes(StandardCharsets.UTF_8));
    }

    private String getInputIdentified(ItemStack[] inputs) {
        // 向上平移
        for (int i = 0; i < 3; i++) {
            if (inputs[0] == null && inputs[1] == null && inputs[2] == null) {
                inputs[0] = inputs[3];
                inputs[1] = inputs[4];
                inputs[2] = inputs[5];

                inputs[3] = inputs[6];
                inputs[4] = inputs[7];
                inputs[5] = inputs[8];

                inputs[6] = null;
                inputs[7] = null;
                inputs[8] = null;
            } else {
                break;
            }
        }

        // 向左平移
        for (int i = 0; i < 3; i++) {
            if (inputs[0] == null && inputs[3] == null && inputs[6] == null) {
                inputs[0] = inputs[1];
                inputs[3] = inputs[4];
                inputs[6] = inputs[7];

                inputs[1] = inputs[2];
                inputs[4] = inputs[5];
                inputs[7] = inputs[8];

                inputs[2] = null;
                inputs[5] = null;
                inputs[8] = null;
            } else {
                break;
            }
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            if (inputs[i] == null) {
                stringBuilder.append("NULL");
            } else {
                stringBuilder.append(inputs[i].getItemType().getId());
                stringBuilder.append(":");
            }
        }

        return stringBuilder.toString();
    }

    private String getInputIdentified(List<ItemStack> source) {
        List<ItemStack> inputs = new LinkedList<>(source);
        inputs.sort((o1, o2) -> o1.getItemType().getId() != o2.getItemType().getId() ? o1.getItemType().getId() - o2.getItemType().getId() : o1.getMeta() - o2.getMeta());

        StringBuilder stringBuilder = new StringBuilder();
        for (ItemStack input : inputs) {
            stringBuilder.append(input.getItemType().getId());
            stringBuilder.append(":");
        }

        return stringBuilder.toString();
    }

    /**
     * 是否匹配酿造台的配方
     *
     * @param inventory
     * @return 返回配方的结果
     */
    public ItemStack matchBrewingRecipe(Inventory inventory) {
        // 输入
        ItemStack ingredient = inventoryServiceProxy.getItem(inventory, BrewingInventory.IngredientIndex);
        if (ingredient.getItemType() == ItemPrototype.AIR) {
            return null;
        }
        // potion
        ItemStack potion = null;
        ItemStack potion1 = inventoryServiceProxy.getItem(inventory, BrewingInventory.POTION1);
        if (potion1.getItemType() != ItemPrototype.AIR) {
            potion = potion1;
        }
        ItemStack potion2 = inventoryServiceProxy.getItem(inventory, BrewingInventory.POTION2);
        if (potion != null && potion2.getItemType() != ItemPrototype.AIR && !potion.equalsWithDamage(potion2)) {
            return null;
        } else if (potion2.getItemType() != ItemPrototype.AIR) {
            potion = potion2;
        }
        ItemStack potion3 = inventoryServiceProxy.getItem(inventory, BrewingInventory.POTION3);
        if (potion != null && potion3.getItemType() != ItemPrototype.AIR && !potion.equalsWithDamage(potion3)) {
            return null;
        } else if (potion3.getItemType() != ItemPrototype.AIR) {
            potion = potion3;
        }
        if (potion == null) {
            return null;
        }
        String key = this.getBrewingKey(ingredient, potion);
        Recipe recipe = this.allBrewingRecipes.get(key);
        if (recipe == null) {
            return null;
        } else {
            return recipe.getOutput();
        }
    }

    private static ItemStack fromJson(Map<String, Object> data) {
        String name = (String) data.get("name");
        Integer id = (Integer) data.get("id");
        Integer meta = (Integer) data.get("damage");
        Integer count = (Integer) data.get("count");
        String nbtInfo = (String) data.getOrDefault("nbt_hex", null);

        ItemStack itemStack = ItemStack.getItem(ItemPrototypeDictionary.getDictionary().map(name));
        itemStack.setCount(count != null ? count : 1);
        itemStack.setMeta(meta != null ? meta : 0);

        try {
            NBTBase nbtTagCompound = NBTToHexString.convertToTag(nbtInfo);
            if (nbtTagCompound instanceof NBTTagCompound) {
                itemStack.updateNBT((NBTTagCompound) nbtTagCompound);
            }
        } catch (IOException ioe) {

        }

        return itemStack;
    }
}
