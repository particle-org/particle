package com.particle.model.inventory.recipe;

import com.particle.model.exception.ErrorCode;
import com.particle.model.exception.ProphetException;
import com.particle.model.inventory.common.RecipeType;
import com.particle.model.item.ItemStack;
import com.particle.model.item.types.ItemPrototype;

import java.util.*;

public class ShapedRecipe extends Recipe {

    /**
     * 合成配方，其余输出
     */
    private List<ItemStack> extraOutput = new ArrayList<>();

    /**
     * 该配方的唯一性标志
     */
    private UUID uuid;

    /**
     * 配方位置
     */
    private String[] shape;

    /**
     * 输入物品
     */
    private Map<Character, ItemStack> inputs = new HashMap<>();
    private List<ItemStack> inputCost = new LinkedList<>();

    /**
     * 配方需要的格子宽度
     */
    private int width;

    /**
     * 配方需要格子高度
     */
    private int height;

    public ShapedRecipe() {
        this.setType(RecipeType.ShapedRecipe);
    }


    public List<ItemStack> getExtraOutput() {
        return extraOutput;
    }

    public void addExtraOutput(List<ItemStack> extraOutput) {
        this.extraOutput.addAll(extraOutput);
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String[] getShape() {
        return shape;
    }

    public Map<Character, ItemStack> getInputs() {
        return inputs;
    }

    public List<ItemStack> getInputCost() {
        return inputCost;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /**
     * 设置具体配方
     *
     * @param shapes
     * @param inputs
     */
    public void setInputs(String[] shapes, Map<Character, ItemStack> inputs) {
        this.checkInputParam(shapes, inputs);
        this.shape = shapes;
        for (Map.Entry<Character, ItemStack> entry : inputs.entrySet()) {
            char key = entry.getKey();
            if (String.join("", this.shape).indexOf(key) < 0) {
                throw new ProphetException(ErrorCode.PARAM_ERROR, "配方有多余材料");
            }
            ItemStack value = entry.getValue();
            this.inputs.put(key, value);
        }
        this.width = this.shape[0].length();
        this.height = this.shape.length;

        // 构造物品消耗列表
        for (String line : this.shape) {
            for (char key : line.toCharArray()) {
                ItemStack itemStack = this.inputs.get(key);
                if (itemStack != null) {
                    this.inputCost.add(itemStack);
                }
            }

        }
    }

    /**
     * 检测配方参数
     *
     * @param shapes
     * @param inputs
     */
    private void checkInputParam(String[] shapes, Map<Character, ItemStack> inputs) {
        int rowCount = shapes.length;
        if (rowCount > 3 || rowCount <= 0) {
            throw new ProphetException(ErrorCode.PARAM_ERROR, "配方不合法");
        }
        int columnCount = shapes[0].length();
        if (columnCount > 3 || columnCount <= 0) {
            throw new ProphetException(ErrorCode.PARAM_ERROR, "配方不合法");
        }
        for (int y = 0; y < rowCount; y++) {
            String row = shapes[y];
            if (row.length() != columnCount) {
                throw new ProphetException(ErrorCode.PARAM_ERROR, "配方不合法");
            }
            for (int x = 0; x < columnCount; x++) {
                char inputFlag = row.charAt(x);
                if (inputFlag != ' ' && !inputs.containsKey(inputFlag)) {
                    throw new ProphetException(ErrorCode.PARAM_ERROR, "配方不合法");
                }
            }
        }
    }

    /**
     * 获取指定位置的配方
     *
     * @param x
     * @param y
     * @return
     */
    public ItemStack getItemByPosition(int x, int y) {
        ItemStack itemStack = this.inputs.get(this.shape[y].charAt(x));
        return itemStack != null ? itemStack.clone() :
                ItemStack.getItem(ItemPrototype.AIR, 0);
    }
}
