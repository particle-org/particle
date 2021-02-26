package com.particle.game.sound.task;

import com.particle.game.sound.SoundService;
import com.particle.game.ui.task.TaskType;
import com.particle.game.ui.task.UITask;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.player.Player;
import com.particle.model.sound.SoundBinder;

import java.util.ArrayList;
import java.util.List;

public class SoundTask extends UITask {

    private SoundService soundService;

    /**
     * 存储一序列的声音
     */
    private List<SoundBinder> sounds = new ArrayList<>();

    private boolean finish;

    private int runTick = 0;

    public SoundTask(SoundService soundService, Player player, List<SoundBinder> soundBinders) {
        super(player, TaskType.SOUND_PLAYER, 0);
        this.soundService = soundService;
        for (SoundBinder soundBundle : soundBinders) {
            if (soundBundle != null) {
                for (int i = 0; i < soundBundle.getDelay(); i++) {
                    sounds.add(null);
                }
                sounds.add(soundBundle);
            }
        }
    }

    @Override
    public boolean isValid() {
        return !this.finish;
    }

    @Override
    protected void onTick() {
        if (this.runTick >= sounds.size()) {
            this.finish = true;
            return;
        }
        SoundBinder sound = this.sounds.get(this.tick);
        if (sound != null) {
            DataPacket packet = sound.getSoundPacket();
            this.soundService.playSound(player, packet);
        }
        this.runTick++;
    }
}
