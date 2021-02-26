package com.particle.game.block.interactor.processor.world.direction;

import com.particle.api.entity.IEntityNBTComponentServiceApi;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.block.attribute.BlockAttributeService;
import com.particle.game.block.common.modules.BannerModule;
import com.particle.game.block.interactor.processor.world.BreakableBlockProcessor;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.player.inventory.service.impl.PlayerInventoryAPI;
import com.particle.game.sound.SoundService;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.entity.model.item.ItemEntity;
import com.particle.model.entity.model.tile.TileEntity;
import com.particle.model.inventory.PlayerInventory;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.item.ItemStack;
import com.particle.model.item.types.ItemPrototype;
import com.particle.model.level.Level;
import com.particle.model.math.BlockDetailFace;
import com.particle.model.math.BlockWallSignFace;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;
import com.particle.model.nbt.NBTTagCompound;
import com.particle.model.nbt.NBTTagList;
import com.particle.model.player.Player;
import com.particle.model.sound.SoundId;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BlockBannerWorldProcessor extends BreakableBlockProcessor {

    private static final ECSModuleHandler<BannerModule> BANNER_MODULE_ECS_MODULE_HANDLER = ECSModuleHandler.buildHandler(BannerModule.class);

    @Inject
    private PositionService positionService;

    @Inject
    private LevelService levelService;

    @Inject
    private BlockAttributeService blockAttributeService;

    @Inject
    private IEntityNBTComponentServiceApi entityNBTComponentServiceApi;

    @Inject
    private PlayerInventoryAPI inventoryService;

    @Inject
    private SoundService soundService;

    @Override
    public boolean onBlockPrePlaced(Level level, Player player, Block targetBlock, Vector3 targetPosition, Vector3 clickPosition, Vector3f clickOffsetPosition) {
        if (targetBlock.getType() == BlockPrototype.WALL_BANNER) {
            BlockWallSignFace playerDirection = positionService.get4FaceDirection(targetPosition, clickPosition, BlockWallSignFace.class);
            BlockPrototype blockPrototype = levelService.getBlockTypeAt(level, targetPosition);
            int meta = 0;
            // 空氣 且可被覆蓋
            if (playerDirection != null && this.blockAttributeService.isCanBeCover(blockPrototype)) {
                meta = playerDirection.getIndex();
                targetBlock.setMeta(meta);
                return super.onBlockPrePlaced(level, player, targetBlock, targetPosition, clickPosition, clickOffsetPosition);
            }
        }

        // 站旗
        if (targetBlock.getType() == BlockPrototype.STANDING_BANNER) {
            BlockDetailFace playerDirection = positionService.get16FaceDirectionByPlayer(player);
            BlockPrototype blockPrototype = levelService.getBlockTypeAt(level, targetPosition);
            int meta = 0;
            if (playerDirection != null && this.blockAttributeService.isCanBeCover(blockPrototype)) {
                meta = playerDirection.getIndex();
                targetBlock.setMeta(meta);
                return super.onBlockPrePlaced(level, player, targetBlock, targetPosition, clickPosition, clickOffsetPosition);
            }
        }

        return false;
    }


    @Override
    public boolean onBlockPlaced(Level level, Player player, Block targetBlock, Vector3 targetPosition) {
        //刷新BlockEntity
        TileEntity entity = tileEntityService.createEntity(targetBlock.getType(), targetPosition);
        if (entity != null) {
            PlayerInventory playerInventory = (PlayerInventory) this.inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_PLAYER);
            ItemStack holdItem = inventoryService.getItem(playerInventory, playerInventory.getItemInHandle());
            if (holdItem.getItemType() != ItemPrototype.BANNER && holdItem.getItemType() != ItemPrototype.WALL_BANNER && holdItem.getItemType() != ItemPrototype.STANDING_BANNER) {
                return false;
            }

            BannerModule bannerModule = BANNER_MODULE_ECS_MODULE_HANDLER.getModule(entity);
            if (bannerModule != null) {
                // 將item nbt 轉到 block
                bannerModule.setBase(holdItem.getMeta());
                bannerModule.setPatterns(holdItem.getNbt());
            }

            this.entitySpawnService.spawn(level, entity);
            soundService.broadcastLevelSound(level, new Vector3f(targetPosition), SoundId.Place, 128);
            return true;
        }

        return false;
    }

    @Override
    public boolean onBlockPreDestroy(Level level, Player player, Block targetBlock, Vector3 targetPosition) {
        TileEntity entity = tileEntityService.getEntityAt(level, targetPosition);
        if (entity == null) {
            return false;
        }

        NBTTagCompound nbtTagCompound = entityNBTComponentServiceApi.getNBTData(entity);
        if (nbtTagCompound == null) {
            return false;
        }

        // 掉落物種類抽取，並設定 meta 值
        int base = nbtTagCompound.getInteger("Base");
        NBTTagList nbtTagList = nbtTagCompound.getTagList("Patterns", 10);
        targetBlock.setMeta(base);

        //方块掉落
        ItemStack itemStack = ItemStack.getItem(ItemPrototype.BANNER);
        itemStack.setCount(1);
        itemStack.setMeta(base);
        if (nbtTagList.tagCount() > 0) {
            itemStack.setNbtData("Patterns", nbtTagList);
        }

        ItemEntity itemEntity = this.itemEntityService.createEntity(itemStack, targetPosition, new Vector3f((float) Math.random() - 0.5f, 4f, (float) Math.random() - 0.5f));
        this.entitySpawnService.spawn(level, itemEntity);

        return true;
    }
}
