package com.particle.api.proxy.goods;

import com.particle.model.player.Player;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IGoodsProvider {
    /**
     * 获取唯一标识
     *
     * @return 商品唯一标识
     */
    String getName();

    /**
     * 给予玩家指定类型商品
     *
     * @param player    目标玩家
     * @param goodsType 商品类型
     * @param amount    商品数量
     * @return 操作结果
     */
    CompletableFuture<OperationResult> addGoods(Player player, String goodsType, int amount);

    /**
     * 给予目标玩家指定商品的所有权
     *
     * @param player  目标玩家
     * @param goodsId 商品唯一ID
     * @param amount  操作数量
     * @return 操作结果
     */
    CompletableFuture<OperationResult> transactGoods(Player player, String goodsId, int amount);

    /**
     * 扣除目标玩家指定商品
     *
     * @param player  目标玩家
     * @param goodsId 商品唯一ID
     * @param amount  操作数量
     * @return 操作结果
     */
    CompletableFuture<OperationResult> removeGoods(Player player, String goodsId, int amount);

    /**
     * 锁定指定商品, 锁定后玩家无法再操作该商品
     *
     * @param player  目标玩家
     * @param goodsId 商品唯一ID
     * @param amount  操作数量
     * @return 操作结果
     */
    CompletableFuture<OperationResult> lockGoods(Player player, String goodsId, int amount);

    /**
     * 解锁指定商品
     *
     * @param player  目标玩家
     * @param goodsId 商品唯一ID
     * @param amount  操作数量
     * @return 操作结果
     */
    CompletableFuture<OperationResult> unlockGoods(Player player, String goodsId, int amount);

    /**
     * 获取指定ID商品信息
     * <p>
     * 同步接口
     *
     * @param goodsId 商品唯一ID
     * @return 商品信息
     */
    IGoodsData requireGoods(String goodsId);

    /**
     * 获取玩家拥有商品列表
     * <p>
     * 同步接口
     *
     * @param player 目标玩家
     * @return 有序的商品列表
     */
    List<IGoodsData> requireGoodsList(Player player);

    /**
     * 获取商品类型
     * <p>
     * 同步接口
     *
     * @param type 类型名
     * @return 商品类型
     */
    IGoodsTypeData requireGoodsType(String type);

    /**
     * 获取全部商品类型列表
     * <p>
     * 同步接口
     *
     * @return 商品类型列表
     */
    List<IGoodsTypeData> requireGoodsTypeList();
}
