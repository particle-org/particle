package com.particle.model.entity.component.ui;

import com.particle.core.ecs.module.BehaviorModule;
import com.particle.model.ui.form.layout.LayoutContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FormLayoutModule extends BehaviorModule {

    /**
     * 当前打开的表单
     */
    private Map<Integer, LayoutContext> openFormList = new ConcurrentHashMap<>();

    /**
     * 当前打开表单的最大id
     */
    private int currentFormId = 0;

    /**
     * 添加表单
     *
     * @param context
     * @return
     */
    public int addFormLayout(LayoutContext context) {
        synchronized (this.openFormList) {
            this.openFormList.put(this.currentFormId++, context);
        }
        return this.currentFormId - 1;
    }

    /**
     * 删除表单
     */
    public void removeFormLayout(int formId) {
        if (this.openFormList.containsKey(formId)) {
            this.openFormList.remove(formId);
        }
    }

    /**
     * 根据id获取formlayout
     *
     * @param formId
     * @return
     */
    public LayoutContext getFormLayoutByFormId(int formId) {
        return this.openFormList.get(formId);
    }
}
