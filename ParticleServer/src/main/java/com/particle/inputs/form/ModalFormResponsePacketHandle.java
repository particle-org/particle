package com.particle.inputs.form;

import com.particle.api.ui.FormServiceAPI;
import com.particle.inputs.PlayerPacketHandle;
import com.particle.model.network.packets.ProtocolInfo;
import com.particle.model.network.packets.data.ModalFormResponsePacket;
import com.particle.model.player.Player;
import com.particle.model.ui.form.layout.LayoutContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ModalFormResponsePacketHandle extends PlayerPacketHandle<ModalFormResponsePacket> {

    private static final Logger logger = LoggerFactory.getLogger(ModalFormResponsePacket.class);

    @Inject
    private FormServiceAPI formServiceAPI;

    @Override
    protected void handlePacket(Player player, ModalFormResponsePacket packet) {
        logger.debug("---formId[{}],response:{}", packet.getFormId(), packet.getJsonResponse());

        LayoutContext context = this.formServiceAPI.getFormLayoutByFormId(player, packet.getFormId());
        if (context != null && !StringUtils.isEmpty(packet.getJsonResponse())) {
            // 删除管理的form
            this.formServiceAPI.removeFormLayout(player, packet.getFormId());
            context.handleResult(player, packet.getJsonResponse().trim());
        }
    }

    @Override
    public int getTargetPacketID() {
        return ProtocolInfo.MODAL_FORM_RESPONSE_PACKET;
    }
}
