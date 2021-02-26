package com.particle.game.server.command.defaults;

import com.particle.api.command.BaseCommand;
import com.particle.api.command.CommandSource;
import com.particle.api.entity.IEntityInteractivedHandle;
import com.particle.game.ui.VirtualButtonService;
import com.particle.model.command.annotation.ParentCommand;
import com.particle.model.command.annotation.RegisterCommand;
import com.particle.model.command.annotation.SubCommand;
import com.particle.model.entity.Entity;
import com.particle.model.item.ItemStack;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;

@RegisterCommand
@Singleton
@ParentCommand("dev-virtual")
@Deprecated
public class VirtualButtonCmd extends BaseCommand {

    @Inject
    private VirtualButtonService virtualButtonService;

    @SubCommand("create")
    public void create(CommandSource commandSource) {
        if (!commandSource.isConsole()) {
            Player player = commandSource.getPlayer();
            this.virtualButtonService.createButton(player, "cmd测试按钮", new IEntityInteractivedHandle() {
                @Override
                public void handle(Entity interactor, ItemStack itemOnHand) {
                }
            });
        }
    }

    @SubCommand("close")
    public void close(CommandSource commandSource) {
        if (!commandSource.isConsole()) {
            Player player = commandSource.getPlayer();
            this.virtualButtonService.closeButton(player);
        }
    }
}
