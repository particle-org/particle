package com.particle.game.player.hud;

import com.particle.core.ecs.module.BehaviorModule;
import com.particle.model.entity.attribute.EntityAttribute;
import com.particle.model.entity.attribute.EntityAttributeType;
import com.particle.util.placeholder.CompiledModel;

import java.awt.*;

public class BossBarModule extends BehaviorModule {

    private static final long BOSS_ENTITY_ID = Long.MAX_VALUE - 10;

    private String template = "UnSet";
    private String renderData = "Unset";
    private long lastRenderDate = 0;
    private Color color = new Color(66, 204, 255);
    private float progress = 0;

    private CompiledModel compiledTemplate = CompiledModel.compile("Unset");
    private EntityAttribute healthAttribute = new EntityAttribute(EntityAttributeType.HEALTH, 0f, 100f, 100f, 20f);

    /**
     * 刷新BossBar的文字
     *
     * @param template
     */
    public void updateTemplate(String template) {
        this.template = template;

        this.compiledTemplate = CompiledModel.compile(template);
    }

    /**
     * 刷新Boss的血量
     *
     * @param health
     */
    public void updateHealth(float health) {
        this.progress = health;

        this.healthAttribute.setCurrentValue(health);
    }

    public void updateTexture(Color color) {
        this.color = color;
    }

    public String getRenderedData() {
        return this.renderData;
    }

    public long getLastRenderDate() {
        return this.lastRenderDate;
    }

    public void render(String playerName) {
        this.renderData = this.compiledTemplate.doCompile(playerName);
        this.lastRenderDate = System.currentTimeMillis();
    }

    public EntityAttribute[] getEntityAttributes() {
        return new EntityAttribute[]{this.healthAttribute};
    }
}
