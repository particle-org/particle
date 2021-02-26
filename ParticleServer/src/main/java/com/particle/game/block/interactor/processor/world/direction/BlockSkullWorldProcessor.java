package com.particle.game.block.interactor.processor.world.direction;

import com.particle.api.entity.IEntityNBTComponentServiceApi;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.block.common.modules.SkullModule;
import com.particle.game.block.interactor.processor.world.BreakableBlockProcessor;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.player.inventory.service.impl.PlayerInventoryAPI;
import com.particle.game.sound.SoundService;
import com.particle.model.block.Block;
import com.particle.model.entity.model.tile.TileEntity;
import com.particle.model.inventory.PlayerInventory;
import com.particle.model.inventory.common.InventoryConstants;
import com.particle.model.item.ItemStack;
import com.particle.model.item.types.ItemPrototype;
import com.particle.model.level.Level;
import com.particle.model.math.BlockDetailFace;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;
import com.particle.model.nbt.NBTTagCompound;
import com.particle.model.player.Player;
import com.particle.model.sound.SoundId;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BlockSkullWorldProcessor extends BreakableBlockProcessor {

    private static final ECSModuleHandler<SkullModule> SKULL_MODULE_ECS_MODULE_HANDLER = ECSModuleHandler.buildHandler(SkullModule.class);


    @Inject
    private PositionService positionService;

    @Inject
    private PlayerInventoryAPI inventoryService;

    @Inject
    private IEntityNBTComponentServiceApi entityNBTComponentServiceApi;

    @Inject
    private SoundService soundService;

    @Override
    public boolean onBlockPrePlaced(Level level, Player player, Block targetBlock, Vector3 targetPosition, Vector3 clickPosition, Vector3f clickOffsetPosition) {
        // 轉向 meta 為 1 或 2 , 然後用 nbt 裡的 Rot 值控制
        targetBlock.setMeta(1);
        return super.onBlockPrePlaced(level, player, targetBlock, targetPosition, clickPosition, clickOffsetPosition);
    }

    @Override
    public boolean onBlockPlaced(Level level, Player player, Block targetBlock, Vector3 targetPosition) {
        //刷新BlockEntity
        TileEntity entity = tileEntityService.createEntity(targetBlock.getType(), targetPosition);
        if (entity != null) {
            PlayerInventory playerInventory = (PlayerInventory) this.inventoryManager.getInventoryByContainerId(player, InventoryConstants.CONTAINER_ID_PLAYER);
            ItemStack holdItem = inventoryService.getItem(playerInventory, playerInventory.getItemInHandle());
            if (holdItem.getItemType() != ItemPrototype.SKULL_ITEM) {
                return false;
            }

            BlockDetailFace playerDirection = positionService.get16FaceDirectionByPlayer(player);
            if (playerDirection != null) {
                SkullModule skullModule = SKULL_MODULE_ECS_MODULE_HANDLER.getModule(entity);
                if (skullModule != null) {
                    skullModule.setRot((byte) playerDirection.getOppositeIndex());
                    skullModule.setSkullType((byte) holdItem.getMeta());
                }

                this.entitySpawnService.spawn(level, entity);
                soundService.broadcastLevelSound(level, new Vector3f(targetPosition), SoundId.Place, 128);
                return true;
            }
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
        int skullType = nbtTagCompound.getByte("SkullType");
        targetBlock.setMeta(skullType);

        return super.onBlockPreDestroy(level, player, targetBlock, targetPosition);
    }
}
