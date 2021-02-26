package com.particle.game.entity.ai.service;

import com.google.inject.Inject;
import com.particle.api.ai.behavior.EStatus;
import com.particle.game.entity.ai.components.EntityDecisionModule;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.game.entity.ai.factory.*;
import com.particle.game.entity.ai.model.ActionTree;
import com.particle.game.entity.attack.EntityAttackedHandleService;
import com.particle.game.entity.movement.MovementServiceProxy;
import com.particle.game.player.interactive.EntityInteractiveService;
import com.particle.model.entity.Entity;

public class EntityDecisionService {

    @Inject
    private static EntityAttackedHandleService entityAttackedHandleService;
    @Inject
    private static EntityInteractiveService entityInteractiveService;
    @Inject
    private static MovementServiceProxy movementServiceProxy;

    /**
     * 更新决策器
     *
     * @param entity
     * @param decision
     */
    public static void updateDecision(Entity entity, EntityDecisionModule entityDecisionModule, String decision) {
        // 查找decision
        ActionTree decisionTree = DecisionTreeFactory.getDecision(decision);

        if (decisionTree != null) {
            entityDecisionModule.setDecision(decisionTree);

            makeDecision(entity, entityDecisionModule, false);
        }
    }

    /**
     * 绑定反应器
     *
     * @param entity
     * @param actionId
     */
    public static void updateResponse(Entity entity, EntityDecisionModule entityDecisionModule, String actionId) {
        // TODO: 2019/4/19 优化成基于事件处理的方式
        ActionTree underAttackReponse = UnderAttackTreeFactory.getUnderAttackReponse(actionId);
        if (underAttackReponse != null) {
            // 伤害相关业务
            entityAttackedHandleService.initEntityAttackedComponent(entity, (interactor) -> {
                entityDecisionModule.addKnowledge(Knowledge.ENTITY_CRIMINAL.getKey(), interactor);

                underAttackReponse.getRoot().tick(entity);
            }, true);
        }

        ActionTree interactiveReponse = InteractiveTreeFactory.getInteractiveReponse(actionId);
        if (interactiveReponse != null) {
            entityInteractiveService.initEntityInteractived(entity, (interactor, item) -> {
                entityDecisionModule.addKnowledge(Knowledge.ENTITY_INTERACTOR.getKey(), interactor);

                interactiveReponse.getRoot().tick(entity);
            });
        }
    }

    /**
     * 执行生物死亡的反馈行为树
     *
     * @param entity
     * @param actionId
     */
    public static void tickEntityDeathTree(Entity entity, String actionId) {
        ActionTree deathReponse = DeathTreeFactory.getDeathResponse(actionId);
        if (deathReponse != null) {
            deathReponse.getRoot().tick(entity);
        }
    }

    /**
     * 生物之间通信的行为树
     *
     * @param from
     * @param to
     * @param actionId
     * @param messageId
     */
    public static EStatus sendMessageToEntity(Entity from, Entity to, EntityDecisionModule entityToDecisionModule, String actionId, String messageId) {
        entityToDecisionModule.addKnowledge(Knowledge.ENTITY_SPEAKER.getKey(), from);

        ActionTree messageResponse = MessageTreeFactory.getMessageResponse(actionId, messageId);
        if (messageResponse != null) {
            return messageResponse.getRoot().tick(to);
        }

        // TODO: 2019/6/6 确认这边的默认值返回success还是fail
        return EStatus.SUCCESS;
    }

    /**
     * 执行决策器
     *
     * @param entity
     */
    public static EStatus makeDecision(Entity entity, EntityDecisionModule entityDecisionModule, boolean force) {
        // 判断是否可以进行决策
        if (force || System.currentTimeMillis() - entityDecisionModule.getLastDecisionTimestamp() > 2000) {

            entityDecisionModule.setLastDecisionTimestamp(System.currentTimeMillis());

            // 执行决策
            ActionTree decision = entityDecisionModule.getDecision();

            if (decision != null) {
                return decision.getRoot().tick(entity);
            }
        }

        return EStatus.ABORTED;
    }

    /**
     * 更新行为控制器
     *
     * @param entity
     * @param decisionId
     */
    public static void updateActionDecision(Entity entity, EntityDecisionModule entityDecisionModule, String decisionId) {
        entityDecisionModule.setAction(ActionTreeFactory.getActionTree(decisionId));
    }

    /**
     * 执行行为控制器
     *
     * @param entity
     */
    public static EStatus makeActionDecision(Entity entity, EntityDecisionModule entityDecisionModule) {
        ActionTree actionDecision = entityDecisionModule.getAction();

        if (actionDecision != null) {
            return actionDecision.getRoot().tick(entity);
        } else {
            return EStatus.ABORTED;
        }
    }

    /**
     * 重置行为控制器
     *
     * @param entity
     */
    public static void resetActionDecision(Entity entity, EntityDecisionModule entityDecisionModule) {
        movementServiceProxy.setRunning(entity, false);

        entityDecisionModule.setAction(null);
    }

    /**
     * 查询上下文
     *
     * @param entity
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getKnowledge(Entity entity, EntityDecisionModule entityDecisionModule, String key, Class<T> clazz) {
        T knowledge = entityDecisionModule.getKnowledge(key, clazz);

        if (knowledge == null) {
            ActionTree action = entityDecisionModule.getAction();

            if (action != null) {
                knowledge = action.getKnowledgeDatabase().getKnowlodge(key, clazz);
            }
        }

        if (knowledge == null) {
            knowledge = entityDecisionModule.getDecision().getKnowledgeDatabase().getKnowlodge(key, clazz);
        }

        return knowledge;
    }

}
