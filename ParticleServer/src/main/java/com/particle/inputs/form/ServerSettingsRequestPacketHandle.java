package com.particle.inputs.form;

import com.particle.api.ui.FormServiceAPI;
import com.particle.inputs.PlayerPacketHandle;
import com.particle.model.network.packets.ProtocolInfo;
import com.particle.model.network.packets.data.ServerSettingsRequestPacket;
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

@Singleton
public class ServerSettingsRequestPacketHandle extends PlayerPacketHandle<ServerSettingsRequestPacket> {

    private static final Logger logger = LoggerFactory.getLogger(ServerSettingsRequestPacketHandle.class);

    @Inject
    private FormServiceAPI formServiceAPI;

    @Override
    protected void handlePacket(Player player, ServerSettingsRequestPacket packet) {
    }

    @Override
    public int getTargetPacketID() {
        return ProtocolInfo.SERVER_SETTINGS_REQUEST_PACKET;
    }

    // 测试代码
    @Deprecated
    private void formxmltest(Player player) {
        InputStream inputStream = null;
        try {
            inputStream = this.getClass().getResourceAsStream("custom_form.xml");
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
                });
            } else if (context instanceof ModalLayout) {
                ((ModalLayout) context).setOnClickListener(new OnClickListener() {

                    @Override
                    public void onModalSubmit(Player player, LayoutContext context, boolean result) {
                        logger.info("form result :" + result);
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
            }
            this.formServiceAPI.openFormLayout(player, context);
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
