package com.particle.game.block.interactor;

import com.particle.api.block.IBlockInteractedProcessor;
import com.particle.api.block.IBlockInteractiveServiceApi;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.event.dispatcher.EventDispatcher;
import com.particle.game.block.interactor.processor.interactive.*;
import com.particle.game.block.tile.TileEntityService;
import com.particle.game.player.interactive.EntityInteractiveService;
import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.entity.model.tile.TileEntity;
import com.particle.model.events.level.player.PlayerInteractiveBlockEvent;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BlockInteractiveService implements IBlockInteractiveServiceApi {

    private static final ECSModuleHandler<BlockInteractedModule> BLOCK_INTERACTED_MODULE_HANDLER = ECSModuleHandler.buildHandler(BlockInteractedModule.class);


    @Inject
    private ContainerBlockAnvilProcessor blockAnvilProcessor;

    @Inject
    private ContainerBlockCraftingTableProcessor blockCraftingTableProcessor;

    @Inject
    private ContainerBlockLoomProcessor containerBlockLoomProcessor;

    @Inject
    private ContainerBlockEnchantTableProcessor blockEnchantTableProcessor;

    @Inject
    private ContainerBlockChestProcessor blockChestProcessor;

    @Inject
    private ContainerBlockButtonProcessor blockButtonProcessor;

    @Inject
    private ContainerBlockDoorProcessor blockDoorProcessor;

    @Inject
    private ContainerBlockLeverProcessor blockLeverProcessor;

    @Inject
    private ContainerBlockFurnaceProcessor blockFurnaceProcessor;

    @Inject
    private ContainerBlockEnderChestProcessor blockEnderChestProcessor;

    @Inject
    private ContainerBlockBrewingStandProcessor blockBrewingStandProcessor;

    @Inject
    private ContainerBlockCauldronInteracted blockCauldronInteractive;

    // 蛋糕
    @Inject
    private BlockCakeInteracted blockCakeInteractive;

    @Inject
    private TileEntityService tileEntityService;

    @Inject
    private EntityInteractiveService entityInteractiveService;


    private EventDispatcher eventDispatcher = EventDispatcher.getInstance();

    @Inject
    public void init() {
        // 铁砧
        BLOCK_INTERACTED_MODULE_HANDLER.bindModule(BlockPrototype.ANVIL.getBindGameObject()).setBlockInteractedProcessor(blockAnvilProcessor);

        // 合成台
        BLOCK_INTERACTED_MODULE_HANDLER.bindModule(BlockPrototype.CRAFTING_TABLE.getBindGameObject()).setBlockInteractedProcessor(blockCraftingTableProcessor);

        // 附魔台
        BLOCK_INTERACTED_MODULE_HANDLER.bindModule(BlockPrototype.ENCHANTING_TABLE.getBindGameObject()).setBlockInteractedProcessor(blockEnchantTableProcessor);

        // 箱子
        BLOCK_INTERACTED_MODULE_HANDLER.bindModule(BlockPrototype.CHEST.getBindGameObject()).setBlockInteractedProcessor(blockChestProcessor);

        // 熔炉
        BLOCK_INTERACTED_MODULE_HANDLER.bindModule(BlockPrototype.FURNACE.getBindGameObject()).setBlockInteractedProcessor(blockFurnaceProcessor);
        BLOCK_INTERACTED_MODULE_HANDLER.bindModule(BlockPrototype.LIT_FURNACE.getBindGameObject()).setBlockInteractedProcessor(blockFurnaceProcessor);

        // 末影箱
        BLOCK_INTERACTED_MODULE_HANDLER.bindModule(BlockPrototype.ENDER_CHEST.getBindGameObject()).setBlockInteractedProcessor(blockEnderChestProcessor);

        // 酿造台
        BLOCK_INTERACTED_MODULE_HANDLER.bindModule(BlockPrototype.BREWING_STAND.getBindGameObject()).setBlockInteractedProcessor(blockBrewingStandProcessor);

        // 煉藥鍋
        BLOCK_INTERACTED_MODULE_HANDLER.bindModule(BlockPrototype.CAULDRON.getBindGameObject()).setBlockInteractedProcessor(blockCauldronInteractive);

        // 按鈕
        BLOCK_INTERACTED_MODULE_HANDLER.bindModule(BlockPrototype.ACACIA_BUTTON.getBindGameObject()).setBlockInteractedProcessor(blockButtonProcessor);
        BLOCK_INTERACTED_MODULE_HANDLER.bindModule(BlockPrototype.BIRCH_BUTTON.getBindGameObject()).setBlockInteractedProcessor(blockButtonProcessor);
        BLOCK_INTERACTED_MODULE_HANDLER.bindModule(BlockPrototype.DARK_OAK_BUTTON.getBindGameObject()).setBlockInteractedProcessor(blockButtonProcessor);
        BLOCK_INTERACTED_MODULE_HANDLER.bindModule(BlockPrototype.JUNGLE_BUTTON.getBindGameObject()).setBlockInteractedProcessor(blockButtonProcessor);
        BLOCK_INTERACTED_MODULE_HANDLER.bindModule(BlockPrototype.SPRUCE_BUTTON.getBindGameObject()).setBlockInteractedProcessor(blockButtonProcessor);
        BLOCK_INTERACTED_MODULE_HANDLER.bindModule(BlockPrototype.STONE_BUTTON.getBindGameObject()).setBlockInteractedProcessor(blockButtonProcessor);
        BLOCK_INTERACTED_MODULE_HANDLER.bindModule(BlockPrototype.WOODEN_BUTTON.getBindGameObject()).setBlockInteractedProcessor(blockButtonProcessor);

        // 門
        BLOCK_INTERACTED_MODULE_HANDLER.bindModule(BlockPrototype.ACACIA_DOOR.getBindGameObject()).setBlockInteractedProcessor(blockDoorProcessor);
        BLOCK_INTERACTED_MODULE_HANDLER.bindModule(BlockPrototype.BIRCH_DOOR.getBindGameObject()).setBlockInteractedProcessor(blockDoorProcessor);
        BLOCK_INTERACTED_MODULE_HANDLER.bindModule(BlockPrototype.DARK_OAK_DOOR.getBindGameObject()).setBlockInteractedProcessor(blockDoorProcessor);
        BLOCK_INTERACTED_MODULE_HANDLER.bindModule(BlockPrototype.IRON_DOOR.getBindGameObject()).setBlockInteractedProcessor(blockDoorProcessor);
        BLOCK_INTERACTED_MODULE_HANDLER.bindModule(BlockPrototype.JUNGLE_DOOR.getBindGameObject()).setBlockInteractedProcessor(blockDoorProcessor);
        BLOCK_INTERACTED_MODULE_HANDLER.bindModule(BlockPrototype.SPRUCE_DOOR.getBindGameObject()).setBlockInteractedProcessor(blockDoorProcessor);
        BLOCK_INTERACTED_MODULE_HANDLER.bindModule(BlockPrototype.WOODEN_DOOR.getBindGameObject()).setBlockInteractedProcessor(blockDoorProcessor);
        BLOCK_INTERACTED_MODULE_HANDLER.bindModule(BlockPrototype.ACACIA_TRAPDOOR.getBindGameObject()).setBlockInteractedProcessor(blockDoorProcessor);
        BLOCK_INTERACTED_MODULE_HANDLER.bindModule(BlockPrototype.TRAPDOOR.getBindGameObject()).setBlockInteractedProcessor(blockDoorProcessor);
        BLOCK_INTERACTED_MODULE_HANDLER.bindModule(BlockPrototype.BIRCH_TRAPDOOR.getBindGameObject()).setBlockInteractedProcessor(blockDoorProcessor);
        BLOCK_INTERACTED_MODULE_HANDLER.bindModule(BlockPrototype.DARK_OAK_TRAPDOOR.getBindGameObject()).setBlockInteractedProcessor(blockDoorProcessor);
        BLOCK_INTERACTED_MODULE_HANDLER.bindModule(BlockPrototype.IRON_TRAPDOOR.getBindGameObject()).setBlockInteractedProcessor(blockDoorProcessor);
        BLOCK_INTERACTED_MODULE_HANDLER.bindModule(BlockPrototype.JUNGLE_TRAPDOOR.getBindGameObject()).setBlockInteractedProcessor(blockDoorProcessor);
        BLOCK_INTERACTED_MODULE_HANDLER.bindModule(BlockPrototype.SPRUCE_TRAPDOOR.getBindGameObject()).setBlockInteractedProcessor(blockDoorProcessor);

        // 拉桿
        BLOCK_INTERACTED_MODULE_HANDLER.bindModule(BlockPrototype.LEVER.getBindGameObject()).setBlockInteractedProcessor(blockLeverProcessor);

        // 蛋糕
        BLOCK_INTERACTED_MODULE_HANDLER.bindModule(BlockPrototype.CAKE.getBindGameObject()).setBlockInteractedProcessor(blockCakeInteractive);

        // 織布機
        BLOCK_INTERACTED_MODULE_HANDLER.bindModule(BlockPrototype.LOOM.getBindGameObject()).setBlockInteractedProcessor(containerBlockLoomProcessor);
    }

    /**
     * 修改方块回调，这里比直接拦截事件效率更高
     *
     * @param blockPrototype
     * @param blockInteractedProcessor
     */
    @Override
    public void registerBlockInteractive(BlockPrototype blockPrototype, IBlockInteractedProcessor blockInteractedProcessor) {
        BLOCK_INTERACTED_MODULE_HANDLER.bindModule(blockPrototype.getBindGameObject()).setBlockInteractedProcessor(blockInteractedProcessor);
    }

    /**
     * 当可交互方块被点击时候
     *
     * @param player
     * @param block
     * @param position
     * @return
     */
    public boolean onBlockInteractive(Player player, Block block, Vector3 position, Vector3f clickPosition) {
        // 先在这里触发测试用，block交互的流程需要重新整理
        PlayerInteractiveBlockEvent playerInteractiveBlockEvent = new PlayerInteractiveBlockEvent(player, player.getLevel());
        playerInteractiveBlockEvent.setBlock(block);
        playerInteractiveBlockEvent.setClickPosition(clickPosition);
        playerInteractiveBlockEvent.setBlockPosition(position);
        this.eventDispatcher.dispatchEvent(playerInteractiveBlockEvent);

        if (playerInteractiveBlockEvent.isCancelled()) {
            // return true代表交互成功，不再执行后面的操作，如放置方块
            return true;
        }

        // 与方块交互
        BlockInteractedModule blockInteractedModule = BLOCK_INTERACTED_MODULE_HANDLER.getModule(block.getType().getBindGameObject());
        if (blockInteractedModule != null) {
            IBlockInteractedProcessor blockInteractedProcessor = blockInteractedModule.getBlockInteractedProcessor();
            if (blockInteractedProcessor != null) {
                blockInteractedProcessor.interactive(player, block, position);

                return true;
            }

            if (blockInteractedModule.isHasTileEntity()) {
                TileEntity entityAt = this.tileEntityService.getEntityAt(player.getLevel(), position);

                if (entityAt != null) {
                    this.entityInteractiveService.onEntityInteractived(entityAt, player, null);

                    return true;
                }
            }
        }

        return false;
    }
}
