package com.particle.game.entity.ai.service;

import com.google.inject.Singleton;
import com.particle.api.ai.EntityDecisionServiceApi;
import com.particle.api.ai.behavior.EStatus;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.entity.ai.components.EntityDecisionModule;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.model.entity.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class EntityDecisionServiceProxy implements EntityDecisionServiceApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntityDecisionServiceProxy.class);

    private static final ECSModuleHandler<EntityDecisionModule> ENTITY_DECISION_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityDecisionModule.class);

    /**
     * 更新决策器
     *
     * @param entity
     * @param decision
     */
    @Override
    public void updateDecision(Entity entity, String decision) {
        EntityDecisionModule entityDecisionModule = ENTITY_DECISION_MODULE_HANDLER.bindModule(entity);

        EntityDecisionService.updateDecision(entity, entityDecisionModule, decision);
    }

    /**
     * 绑定反应器
     *
     * @param entity
     * @param actionId
     */
    @Override
    public void updateResponse(Entity entity, String actionId) {
        EntityDecisionModule entityDecisionModule = ENTITY_DECISION_MODULE_HANDLER.bindModule(entity);

        EntityDecisionService.updateResponse(entity, entityDecisionModule, actionId);
    }

    /**
     * 生物之间通信的行为树
     *
     * @param from
     * @param to
     * @param actionId
     * @param messageId
     */
    public EStatus sendMessageToEntity(Entity from, Entity to, String actionId, String messageId) {
        EntityDecisionModule entityDecisionModule = ENTITY_DECISION_MODULE_HANDLER.getModule(to);
        if (entityDecisionModule != null) {
            return EntityDecisionService.sendMessageToEntity(from, to, entityDecisionModule, actionId, messageId);
        }

        return EStatus.SUCCESS;
    }

    /**
     * 执行决策器
     *
     * @param entity
     */
    public EStatus makeDecision(Entity entity, boolean force) {
        EntityDecisionModule entityDecisionModule = ENTITY_DECISION_MODULE_HANDLER.getModule(entity);
        if (entityDecisionModule == null) {
            throw new RuntimeException("Decision component not founded");
        }

        return EntityDecisionService.makeDecision(entity, entityDecisionModule, force);
    }

    /**
     * 更新行为控制器
     *
     * @param entity
     * @param decisionId
     */
    public void updateActionDecision(Entity entity, String decisionId) {
        EntityDecisionModule entityDecisionModule = ENTITY_DECISION_MODULE_HANDLER.getModule(entity);
        if (entityDecisionModule == null) {
            throw new RuntimeException("Decision component not founded");
        }

        EntityDecisionService.updateActionDecision(entity, entityDecisionModule, decisionId);
    }

    /**
     * 执行行为控制器
     *
     * @param entity
     */
    public EStatus makeActionDecision(Entity entity) {
        EntityDecisionModule entityDecisionModule = ENTITY_DECISION_MODULE_HANDLER.getModule(entity);
        if (entityDecisionModule == null) {
            throw new RuntimeException("Decision component not founded");
        }

        return EntityDecisionService.makeActionDecision(entity, entityDecisionModule);
    }

    /**
     * 重置行为控制器
     *
     * @param entity
     */
    public void resetActionDecision(Entity entity) {
        EntityDecisionModule entityDecisionModule = ENTITY_DECISION_MODULE_HANDLER.getModule(entity);
        if (entityDecisionModule != null) {
            EntityDecisionService.resetActionDecision(entity, entityDecisionModule);
        } else {
            LOGGER.error("Fail to reset action decision because entity decision module not founded.");
        }
    }

    /**
     * 设置上下文
     *
     * @param entity
     * @param knowledge
     * @param val
     */
    public void addKnowledge(Entity entity, Knowledge knowledge, Object val) {
        this.addKnowledge(entity, knowledge.getKey(), val);
    }

    public void addKnowledge(Entity entity, String key, Object val) {
        EntityDecisionModule entityDecisionModule = ENTITY_DECISION_MODULE_HANDLER.getModule(entity);
        if (entityDecisionModule == null) {
            return;
        }

        entityDecisionModule.addKnowledge(key, val);
    }

    /**
     * 移除知识点
     *
     * @param entity
     * @param knowledge
     */
    public void removeKnowledge(Entity entity, Knowledge knowledge) {
        this.removeKnowledge(entity, knowledge.getKey());
    }

    public void removeKnowledge(Entity entity, String key) {
        EntityDecisionModule entityDecisionModule = ENTITY_DECISION_MODULE_HANDLER.getModule(entity);
        if (entityDecisionModule == null) {
            return;
        }

        entityDecisionModule.removeKnowledge(key);
    }

    /**
     * 查询上下文
     *
     * @param entity
     * @param knowledge
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getKnowledge(Entity entity, Knowledge knowledge, Class<T> clazz) {
        return this.getKnowledge(entity, knowledge.getKey(), clazz);
    }

    public <T> T getKnowledge(Entity entity, String key, Class<T> clazz) {
        EntityDecisionModule entityDecisionModule = ENTITY_DECISION_MODULE_HANDLER.getModule(entity);
        if (entityDecisionModule == null) {
            return null;
        }

        return EntityDecisionService.getKnowledge(entity, entityDecisionModule, key, clazz);
    }
}
