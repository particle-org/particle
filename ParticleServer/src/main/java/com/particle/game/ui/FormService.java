package com.particle.game.ui;

import com.particle.api.ui.FormServiceAPI;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.model.entity.component.ui.FormLayoutModule;
import com.particle.model.exception.ErrorCode;
import com.particle.model.exception.ProphetException;
import com.particle.model.network.packets.data.ModalFormRequestPacket;
import com.particle.model.player.Player;
import com.particle.model.ui.form.FormXmlAttr;
import com.particle.model.ui.form.FormXmlTag;
import com.particle.model.ui.form.layout.CustomLayout;
import com.particle.model.ui.form.layout.LayoutContext;
import com.particle.model.ui.form.layout.ModalLayout;
import com.particle.model.ui.form.layout.SimpleLayout;
import com.particle.model.ui.form.view.*;
import com.particle.network.NetworkManager;
import com.particle.util.xml.ViewNode;
import com.particle.util.xml.XmlParseUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Singleton
public class FormService implements FormServiceAPI {

    private static final Logger logger = LoggerFactory.getLogger(FormService.class);

    // ----- 功能组件 -----
    private static final ECSModuleHandler<FormLayoutModule> FORM_LAYOUT_MODULE_HANDLER = ECSModuleHandler.buildHandler(FormLayoutModule.class);

    @Inject
    private NetworkManager networkManager;

    /**
     * 将xml解析成对应的布局控件
     *
     * @param inputStream
     * @return
     */
    @Override
    public LayoutContext inflate(InputStream inputStream) {
        if (inputStream == null) {
            logger.error("inputStream为空！");
            return null;
        }
        try {
            ViewNode rootNode = XmlParseUtils.parse(inputStream);
            LayoutContext context = this.parseRootNode(rootNode);
            if (context == null) {
                return null;
            }
            // 检查是否合法
            context.check();
            return context;
        } catch (Exception e) {
            logger.error("inflate failed!", e);
            return null;
        }
    }

    /**
     * 添加表单
     *
     * @param player
     * @param context
     * @return
     */
    @Override
    public int openFormLayout(Player player, LayoutContext context) {
        FormLayoutModule formLayoutModule = FORM_LAYOUT_MODULE_HANDLER.getModule(player);
        if (formLayoutModule == null) {
            return -1;
        }
        int formId = formLayoutModule.addFormLayout(context);
        String json = context.toJson();
        ModalFormRequestPacket dataPacket = new ModalFormRequestPacket();
        dataPacket.setFormId(formId);
        logger.debug(String.format("open menuId:%s, id : %s, json:%s", context.getId(), dataPacket.getFormId(), json));
        dataPacket.setFormUiJson(json);
        networkManager.sendMessage(player.getClientAddress(), dataPacket);
        return formId;
    }

    /**
     * 删除表单
     *
     * @param player
     * @param formId
     */
    @Override
    public void removeFormLayout(Player player, int formId) {
        FormLayoutModule formLayoutModule = FORM_LAYOUT_MODULE_HANDLER.getModule(player);
        if (formLayoutModule == null) {
            return;
        }
        formLayoutModule.removeFormLayout(formId);
    }

    /**
     * 根据id获取formlayout
     *
     * @param player
     * @param formId
     * @return
     */
    @Override
    public LayoutContext getFormLayoutByFormId(Player player, int formId) {
        FormLayoutModule formLayoutModule = FORM_LAYOUT_MODULE_HANDLER.getModule(player);
        if (formLayoutModule == null) {
            return null;
        }
        return formLayoutModule.getFormLayoutByFormId(formId);
    }

    /**
     * 解析根节点
     *
     * @param rootNode
     * @return
     */
    private LayoutContext parseRootNode(ViewNode rootNode) {
        if (rootNode == null) {
            return null;
        }
        String rootTag = rootNode.getViewName();
        if (StringUtils.isEmpty(rootTag)) {
            return null;
        }
        if (!rootTag.equals(FormXmlTag.formLayout)) {
            return null;
        }

        HashMap<String, String> attr = rootNode.getAttributes();
        if (attr == null) {
            return null;
        }
        LayoutContext context = null;
        String type = attr.get(FormXmlAttr.type);
        if (type.equals("custom_form")) {
            context = new CustomLayout();
            context.setTitle(attr.get(FormXmlAttr.title));
            context.setId(attr.get(FormXmlAttr.id));
            ((CustomLayout) context).setImageType(attr.get(FormXmlAttr.imageType));
            ((CustomLayout) context).setImageUrl(attr.get(FormXmlAttr.imageValue));
            context.setId(attr.get(FormXmlAttr.id));
            ((CustomLayout) context).setOnClose(attr.getOrDefault(FormXmlAttr.onClose, ""));
            ((CustomLayout) context).setOnSubmit(attr.getOrDefault(FormXmlAttr.onSubmit, ""));
            this.parseCustomChildren((CustomLayout) context, rootNode);
            return context;
        } else if (type.equals("modal")) {
            context = new ModalLayout();
            context.setTitle(attr.get(FormXmlAttr.title));
            context.setId(attr.get(FormXmlAttr.id));
            ((ModalLayout) context).setText(attr.get(FormXmlAttr.text));
            this.parseModalChildren((ModalLayout) context, rootNode);
            return context;
        } else if (type.equals("simple_form")) {
            context = new SimpleLayout();
            context.setTitle(attr.get(FormXmlAttr.title));
            context.setId(attr.get(FormXmlAttr.id));
            ((SimpleLayout) context).setText(attr.get(FormXmlAttr.text));
            ((SimpleLayout) context).setOnClose(attr.getOrDefault(FormXmlAttr.onClose, ""));
            this.parseSimpleChildren((SimpleLayout) context, rootNode);
            return context;
        }
        return null;
    }

    /**
     * 解析customLayout
     *
     * @param context
     * @param rootNode
     */
    private void parseCustomChildren(CustomLayout context, ViewNode rootNode) {
        List<ViewNode> children = rootNode.getChildren();
        if (children == null || children.isEmpty()) {
            throw new ProphetException(ErrorCode.PARAM_ERROR, "必须包含子组件");
        }
        for (ViewNode child : children) {
            if (child == null) {
                continue;
            }
            String childTag = child.getViewName();
            if (StringUtils.isEmpty(childTag)) {
                continue;
            }
            if (childTag.equals(FormXmlTag.dropDown)) {
                DropDownView dropDown = this.parseDropDown(child);
                if (dropDown != null) {
                    context.addContent(dropDown);
                }
            } else if (childTag.equals(FormXmlTag.inputTag)) {
                InputView input = this.parseInput(child);
                if (input != null) {
                    context.addContent(input);
                }
            } else if (childTag.equals(FormXmlTag.label)) {
                LabelView label = this.parLabel(child);
                if (label != null) {
                    context.addContent(label);
                }
            } else if (childTag.equals(FormXmlTag.slider)) {
                SliderView slider = this.parseSlider(child);
                if (slider != null) {
                    context.addContent(slider);
                }
            } else if (childTag.equals(FormXmlTag.stepSlider)) {
                StepSliderView stepSlider = this.parseStepSlider(child);
                if (stepSlider != null) {
                    context.addContent(stepSlider);
                }
            } else if (childTag.equals(FormXmlTag.toggle)) {
                ToggleView toggle = this.parseToggle(child);
                if (toggle != null) {
                    context.addContent(toggle);
                }
            }
        }
    }

    /**
     * 解析modal layout
     *
     * @param context
     * @param rootNode
     */
    private void parseModalChildren(ModalLayout context, ViewNode rootNode) {
        List<ViewNode> children = rootNode.getChildren();
        if (children == null || children.isEmpty()) {
            throw new ProphetException(ErrorCode.PARAM_ERROR, "必须包含子组件");
        }
        if (children.size() != 2) {
            throw new ProphetException(ErrorCode.PARAM_ERROR, "只支持两个按钮组件");
        }

        ButtonView confirm = this.parseButtonView(children.get(0));
        ButtonView cancel = this.parseButtonView(children.get(1));
        context.setConform(confirm);
        context.setCancel(cancel);
        return;
    }

    /**
     * 解析simpleLayout
     *
     * @param context
     * @param rootNode
     */
    private void parseSimpleChildren(SimpleLayout context, ViewNode rootNode) {
        List<ViewNode> children = rootNode.getChildren();
        if (children == null || children.isEmpty()) {
            return;
        }
        for (ViewNode child : children) {
            ButtonView buttonView = this.parseButtonView(child);
            context.addButtonView(buttonView);
        }
        return;
    }

    /**
     * 解析按钮
     *
     * @param child
     * @return
     */
    private ButtonView parseButtonView(ViewNode child) {
        String childTag = child.getViewName();

        if (!childTag.equals(FormXmlTag.buttonView)) {
            throw new ProphetException(ErrorCode.PARAM_ERROR, "只支持按钮组件");
        }
        HashMap<String, String> attrs = child.getAttributes();
        ButtonView firstBtn = new ButtonView();
        firstBtn.setText(attrs.get(FormXmlAttr.text));
        firstBtn.setId(attrs.get(FormXmlAttr.id));
        firstBtn.setOnClick(attrs.getOrDefault(FormXmlAttr.onClick, ""));
        firstBtn.setIsShow(attrs.getOrDefault(FormXmlAttr.isShow, "true"));
        firstBtn.setPermission(attrs.getOrDefault(FormXmlAttr.permission, ""));
        firstBtn.setImageType(attrs.getOrDefault(FormXmlAttr.imageType, ""));
        firstBtn.setImagePath(attrs.getOrDefault(FormXmlAttr.imageValue, ""));
        return firstBtn;
    }

    /**
     * 解析dropDown控件
     *
     * @param child
     * @return
     */
    private DropDownView parseDropDown(ViewNode child) {
        String childTag = child.getViewName();
        if (StringUtils.isEmpty(childTag)) {
            return null;
        }
        if (!childTag.equals(FormXmlTag.dropDown)) {
            return null;
        }

        List<ViewNode> options = child.getChildren();
        if (options == null || options.isEmpty()) {
            return null;
        }

        HashMap<String, String> attr = child.getAttributes();
        DropDownView dropDown = new DropDownView();
        dropDown.setText(attr.get(FormXmlAttr.text));
        dropDown.setId(attr.get(FormXmlAttr.id));
        dropDown.setDefaultValue(attr.get(FormXmlAttr._default));

        for (ViewNode option : options) {
            String optionName = option.getViewName();
            if (optionName.equals(FormXmlTag.option)) {
                HashMap<String, String> optionAttr = option.getAttributes();
                OptionView optionView = new OptionView();
                optionView.setValue(optionAttr.get(FormXmlAttr.value));
                optionView.setDisplay(optionAttr.get(FormXmlAttr.display));
                dropDown.addOption(optionView);
            }
        }
        return dropDown;
    }

    /**
     * 解析input组件
     *
     * @param child
     * @return
     */
    private InputView parseInput(ViewNode child) {
        String childTag = child.getViewName();
        if (StringUtils.isEmpty(childTag)) {
            return null;
        }
        if (!childTag.equals(FormXmlTag.inputTag)) {
            return null;
        }
        HashMap<String, String> attr = child.getAttributes();
        InputView input = new InputView();
        input.setId(attr.get(FormXmlAttr.id));
        input.setPlaceholder(attr.get(FormXmlAttr.placeholder));
        input.setDefaultValue(attr.get(FormXmlAttr._default));
        input.setText(attr.get(FormXmlAttr.text));
        return input;
    }

    /**
     * 解析label组件
     *
     * @param child
     * @return
     */
    private LabelView parLabel(ViewNode child) {
        String childTag = child.getViewName();
        if (StringUtils.isEmpty(childTag)) {
            return null;
        }
        if (!childTag.equals(FormXmlTag.label)) {
            return null;
        }
        HashMap<String, String> attr = child.getAttributes();
        LabelView label = new LabelView();
        label.setId(attr.get(FormXmlAttr.id));
        label.setText(attr.get(FormXmlAttr.text));
        return label;
    }

    /**
     * 解析slider
     *
     * @param child
     * @return
     */
    private SliderView parseSlider(ViewNode child) {
        String childTag = child.getViewName();
        if (StringUtils.isEmpty(childTag)) {
            return null;
        }
        if (!childTag.equals(FormXmlTag.slider)) {
            return null;
        }
        HashMap<String, String> attr = child.getAttributes();
        SliderView slider = new SliderView();
        slider.setId(attr.get(FormXmlAttr.id));
        slider.setMin(attr.get(FormXmlAttr.min));
        slider.setMax(attr.get(FormXmlAttr.max));
        slider.setStep(attr.get(FormXmlAttr.step));
        slider.set_default(attr.get(FormXmlAttr._default));
        slider.setText(attr.get(FormXmlAttr.text));
        return slider;
    }

    /**
     * 解析stepSlider
     *
     * @param child
     * @return
     */
    private StepSliderView parseStepSlider(ViewNode child) {
        String childTag = child.getViewName();
        if (StringUtils.isEmpty(childTag)) {
            return null;
        }
        if (!childTag.equals(FormXmlTag.stepSlider)) {
            return null;
        }

        List<ViewNode> options = child.getChildren();
        if (options == null || options.isEmpty()) {
            return null;
        }

        HashMap<String, String> attr = child.getAttributes();

        StepSliderView stepSlider = new StepSliderView();
        stepSlider.setId(attr.get(FormXmlAttr.id));
        stepSlider.setText(attr.get(FormXmlAttr.text));
        stepSlider.setDefaultValue(attr.get(FormXmlAttr._default));
        List<String> ops = new ArrayList<>();
        for (ViewNode option : options) {
            String optionName = option.getViewName();
            HashMap<String, String> optionAttr = option.getAttributes();
            if (optionName.equals(FormXmlTag.step)) {
                ops.add(optionAttr.get(FormXmlAttr.text));
            }
        }
        stepSlider.setSteps(ops);
        return stepSlider;
    }

    /**
     * 解析toggle
     */
    private ToggleView parseToggle(ViewNode child) {
        String childTag = child.getViewName();
        if (StringUtils.isEmpty(childTag)) {
            return null;
        }
        if (!childTag.equals(FormXmlTag.toggle)) {
            return null;
        }
        HashMap<String, String> attr = child.getAttributes();
        ToggleView toggle = new ToggleView();
        toggle.setId(attr.get(FormXmlAttr.id));
        toggle.setText(attr.get(FormXmlAttr.text));
        toggle.setDefaultValue(attr.get(FormXmlAttr._default));
        return toggle;
    }

}
