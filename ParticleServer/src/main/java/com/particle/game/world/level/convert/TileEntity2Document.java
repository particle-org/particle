package com.particle.game.world.level.convert;

import com.particle.api.chunk.ITileEntity2DocumentApi;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.core.ecs.serialization.SerializationTool;
import com.particle.game.block.tile.TileEntityService;
import com.particle.game.common.modules.TransformModule;
import com.particle.game.utils.ecs.ECSComponentManager;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.entity.component.ECSComponent;
import com.particle.model.entity.model.tile.TileEntity;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class TileEntity2Document implements ITileEntity2DocumentApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(TileEntity2Document.class);

    private static final ECSModuleHandler<TransformModule> TRANSFORM_MODULE_HANDLER = ECSModuleHandler.buildHandler(TransformModule.class);

    @Inject
    private ECSComponentManager ecsComponentManager;

    @Inject
    private TileEntityService tileEntityService;


    public Document toDocument(TileEntity tileEntity) {
        //创建entity的tag
        Document entityData = new Document();

        //提取并保存位置信息
        Vector3f position = TRANSFORM_MODULE_HANDLER.getModule(tileEntity).getPosition();
        entityData.put("x", ((int) (position.getX())) & 15);
        entityData.put("y", (int) (position.getY()));
        entityData.put("z", ((int) (position.getZ())) & 15);

        //提取保存其它信息
        List<Document> componentsData = new ArrayList<>();
        for (ECSComponent component : tileEntity.getComponents()) {
            //跳过位置信息存储
            if (component == null)
                continue;

            String data = ecsComponentManager.exportComponent(component);

            if (data != null) {
                Document componentTag = new Document();
                componentTag.put("v", ECSComponentManager.VERSION);
                componentTag.put("id", component.getName());
                componentTag.put("data", data);
                componentsData.add(componentTag);
            }
        }
        entityData.put("Components", componentsData);

        entityData.put("ecs", SerializationTool.exportModuleData(tileEntity));

        return entityData;
    }

    public TileEntity fromDocument(BlockPrototype blockPrototype, Document entityData) {
        //只有有位置信息的tile entity才算是合法的tile entity
        Integer x = entityData.getInteger("x");
        Integer y = entityData.getInteger("y");
        Integer z = entityData.getInteger("z");

        if (x != null && y != null && z != null) {
            //通过blockPrototype来构造tile entity，以保证非法的tile entity被正确清理掉
            TileEntity tileEntity = this.tileEntityService.createEntity(blockPrototype, new Vector3(x, y, z));

            if (tileEntity != null) {
                //提取component信息
                List<Document> components = entityData.get("Components", ArrayList.class);
                // 解析组件
                if (components != null && !components.isEmpty()) {
                    for (Document component : components) {
                        int v = component.getInteger("v");
                        String componentData = component.getString("data");

                        ecsComponentManager.importECSComponent(tileEntity, componentData);
                    }
                }

                // 提取component信息
                Document componentsData = entityData.get("ecs", Document.class);
                // 解析组件
                if (componentsData != null && !componentsData.isEmpty()) {
                    for (String key : componentsData.keySet()) {
                        SerializationTool.importModuleData(tileEntity, key, componentsData.getString(key));
                    }
                }

                ecsComponentManager.filterTickedSystem(tileEntity);

                return tileEntity;
            } else {
                LOGGER.error("Tile entity type miss match at ({},{},{}), block id {}", x, y, z, blockPrototype.getName());
                // 提取component信息
                Document componentsData = entityData.get("ecs", Document.class);
                // 解析组件
                if (componentsData != null && !componentsData.isEmpty()) {
                    for (String key : componentsData.keySet()) {
                        LOGGER.error("Tile entity data {} : {}", key, componentsData.getString(key));
                    }
                }
            }
        }

        return null;
    }
}
