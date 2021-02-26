package com.particle.game.entity.ai.model;

import com.particle.api.ai.behavior.IBehaviour;
import com.particle.game.entity.ai.components.KnowledgeDatabase;

public class ActionTree {

    private String name;

    private IBehaviour root;

    private KnowledgeDatabase knowledgeDatabase;

    public ActionTree(String name, IBehaviour root, KnowledgeDatabase knowledgeDatabase) {
        this.name = name;
        this.root = root;
        this.knowledgeDatabase = knowledgeDatabase;
    }

    public String getName() {
        return name;
    }

    public IBehaviour getRoot() {
        return root;
    }

    public KnowledgeDatabase getKnowledgeDatabase() {
        return knowledgeDatabase;
    }
}
