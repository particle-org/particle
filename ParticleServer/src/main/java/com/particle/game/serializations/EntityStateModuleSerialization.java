package com.particle.game.serializations;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.serialization.IStringSerialization;
import com.particle.game.entity.state.EntityStateModule;
import com.particle.model.entity.type.EntityStateRecorder;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class EntityStateModuleSerialization implements IStringSerialization<EntityStateModule> {
    @Override
    public String serialization(GameObject gameObject, EntityStateModule entityStateModule) {
        return JSON.toJSONString(entityStateModule.getEntityState()) + "|" + JSON.toJSONString(entityStateModule.getBindState());
    }

    @Override
    public void deserialization(GameObject gameObject, String data, EntityStateModule entityStateModule) {
        if (StringUtils.isNotBlank(data)) {
            String[] split = data.split("\\|");
            if (split.length > 0) {
                Map<String, EntityStateRecorder> stringEntityStateRecorderMap = JSON.parseObject(split[0], new TypeReference<Map<String, EntityStateRecorder>>() {
                });
                for (Map.Entry<String, EntityStateRecorder> entry : stringEntityStateRecorderMap.entrySet()) {
                    entityStateModule.setEntityState(entry.getKey(), entry.getValue());
                }
            }
            if (split.length > 1) {
                Map<String, EntityStateRecorder> stringEntityStateRecorderMap = JSON.parseObject(split[1], new TypeReference<Map<String, EntityStateRecorder>>() {
                });
                for (Map.Entry<String, EntityStateRecorder> entry : stringEntityStateRecorderMap.entrySet()) {
                    entityStateModule.importBindState(entry.getKey(), entry.getValue());
                }
            }
        }
    }
}
