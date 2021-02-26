package com.particle.game.server.command.defaults;

import com.particle.api.command.BaseCommand;
import com.particle.api.command.CommandSource;
import com.particle.api.ui.FormServiceAPI;
import com.particle.game.server.command.impl.PlayerCommandSource;
import com.particle.model.command.annotation.CommandPermission;
import com.particle.model.command.annotation.ParentCommand;
import com.particle.model.command.annotation.RegisterCommand;
import com.particle.model.command.annotation.SubCommand;
import com.particle.model.permission.CommandPermissionConstant;
import com.particle.model.player.Player;
import com.particle.model.ui.form.layout.CustomLayout;
import com.particle.model.ui.form.layout.LayoutContext;
import com.particle.model.ui.form.layout.ModalLayout;
import com.particle.model.ui.form.layout.SimpleLayout;
import com.particle.model.ui.form.listener.OnClickListener;
import com.particle.model.ui.form.view.ButtonView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@RegisterCommand
@Singleton
@CommandPermission(CommandPermissionConstant.GIVE)
@ParentCommand("dev")
@Deprecated
public class MenuCommand extends BaseCommand {

    private static final Logger logger = LoggerFactory.getLogger(MenuCommand.class);

    @Inject
    private FormServiceAPI formServiceAPI;

    @SubCommand("menu")
    public void menu(CommandSource source, String target) {
        Player player = null;
        if (source instanceof PlayerCommandSource) {
            PlayerCommandSource playerCommandSource = (PlayerCommandSource) source;
            player = playerCommandSource.getPlayer();
        }

        if (player == null) {
            String message = "玩家对象不存在!";
            source.sendError(message);
            return;
        }

        InputStream inputStream = null;
        try {
            inputStream = this.getClass().getResourceAsStream(target + ".xml");
            LayoutContext context = this.formServiceAPI.inflate(inputStream);
            if (context == null) {
                return;
            }
            if (context instanceof CustomLayout) {
                ((CustomLayout) context).setOnClickListener(new OnClickListener() {

                    @Override
                    public void onCustomSubmit(Player player, LayoutContext context, Map result) {
                        logger.info("form result :" + result);
                    }

                    @Override
                    public void onClose(Player player, LayoutContext context) {
                        logger.info("form {} close :", context);
                    }
                });
            } else if (context instanceof ModalLayout) {
                ((ModalLayout) context).setOnClickListener(new OnClickListener() {

                    @Override
                    public void onModalSubmit(Player player, LayoutContext context, boolean result) {
                        logger.info("form result :" + result);
                    }

                    @Override
                    public void onClose(Player player, LayoutContext context) {
                        logger.info("form {} close :", context);
                    }

                });
            } else if (context instanceof SimpleLayout) {
                SimpleLayout simpleLayout = (SimpleLayout) context;
                ButtonView buttonView1 = simpleLayout.getButtonById("id1");
                ButtonView buttonView2 = simpleLayout.getButtonById("id2");
                ButtonView buttonView3 = simpleLayout.getButtonById("id3");
                buttonView1.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(Player player, ButtonView view) {
                        logger.info("form 按钮1");
                    }
                });
                buttonView2.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(Player player, ButtonView view) {
                        logger.info("form 按钮2");
                    }
                });
                buttonView3.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(Player player, ButtonView view) {
                        logger.info("form 按钮3");
                    }
                });
                simpleLayout.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClose(Player player, LayoutContext context) {
                        logger.info("form {} close :", context);
                    }
                });
            }

            int formId = this.formServiceAPI.openFormLayout(player, context);
        } catch (Exception e) {
            logger.error("failed", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ioe) {

                }

            }
        }
    }

}
