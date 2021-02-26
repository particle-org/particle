package com.particle.game.entity.ai;

import com.particle.api.ai.behavior.IBehaviour;
import com.particle.api.ai.behavior.IBranchNode;
import com.particle.api.ai.behavior.ILeafNode;
import com.particle.game.entity.ai.branch.RootSelecter;
import com.particle.game.entity.ai.components.KnowledgeDatabase;
import com.particle.game.entity.ai.model.ActionTree;

import java.util.List;
import java.util.Stack;

public class ActionTreeBuilder {

    private String name;

    private RootSelecter rootSelecter;
    private Stack<IBranchNode> operatorCache = new Stack<>();

    private KnowledgeDatabase knowledgeDatabase = new KnowledgeDatabase();

    /**
     * 创建行为属构造器
     *
     * @param name
     * @return
     */
    public static ActionTreeBuilder create(String name) {
        return new ActionTreeBuilder(name);
    }

    /**
     * 创建行为树
     *
     * @param name
     */
    private ActionTreeBuilder(String name) {
        this.name = name;

        this.rootSelecter = new RootSelecter();
        this.operatorCache.push(this.rootSelecter);
    }

    /**
     * 添加枝干节点
     *
     * @param branchNode
     * @param weight
     * @return
     */
    public ActionTreeBuilder branch(IBranchNode branchNode, int weight) {
        this.operatorCache.peek().addChild(branchNode, weight);
        this.operatorCache.push(branchNode);

        return this;
    }

    /**
     * 闭合节点
     */
    public ActionTreeBuilder upper() {
        this.operatorCache.pop();

        return this;
    }

    /**
     * 增加叶节点
     *
     * @param leafNode
     */
    public ActionTreeBuilder leaf(ILeafNode leafNode, int weight) {
        this.operatorCache.peek().addChild(leafNode, weight);

        return this;
    }

    /**
     * 设置上下文
     *
     * @param key
     * @param val
     */
    public ActionTreeBuilder addKnowledge(String key, Object val) {
        this.knowledgeDatabase.addKnowledge(key, val);

        return this;
    }

    /**
     * 配置最上层节点
     *
     * @param key
     * @param val
     */
    public ActionTreeBuilder config(String key, Object val) {
        IBranchNode node = this.operatorCache.peek();

        List<IBehaviour> children = node.getChildren();

        if (children.size() == 0) {
            node.config(key, val);
        } else {
            children.get(children.size() - 1).config(key, val);
        }

        return this;
    }

    /**
     * 构造行为树
     *
     * @return
     */
    public ActionTree build() {
        this.rootSelecter.onInitialize();
        return new ActionTree(this.name, this.rootSelecter, this.knowledgeDatabase);
    }

}
