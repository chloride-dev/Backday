package cn.backday.manager;

import cn.backday.Client;
import cn.backday.api.event.impl.game.TickEvent;
import cn.backday.module.impl.client.Target;
import cn.backday.utils.MinecraftInterface;
import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.entity.EntityLivingBase;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public class TargetManager extends ConcurrentLinkedQueue<EntityLivingBase> implements MinecraftInterface {
    public boolean players = true;
    public boolean invisibles = false;
    public boolean animals = false;
    public boolean mobs = false;
    public boolean teams = false;

    private int loadedEntitySize;

    public void init() {
        EventManager.register(this);
    }

    @EventTarget
    private void onTick(TickEvent event) {
        if (mc.thePlayer.ticksExisted % 150 == 0 || loadedEntitySize != mc.theWorld.loadedEntityList.size()) {
            this.updateTargets();
            loadedEntitySize = mc.theWorld.loadedEntityList.size();
        }
    }

    public void updateTargets() {
        try {
            Target target = Target.INSTANCE;
            players = target.getPlayer().get();
            invisibles = target.getInvisibles().get();
            animals = target.getAnimals().get();
            mobs = target.getMobs().get();
            teams = target.getTeams().get();
        } catch (Exception e) {
            // Don't give crackers clues...
            if (Client.INSTANCE.isDev()) throw new RuntimeException(e);
        }
    }

    public List<EntityLivingBase> getTargets(double range) {
        return this.stream()
                .filter(entity -> mc.thePlayer.getDistanceToEntity(entity) < range)
                .filter(entity -> mc.theWorld.loadedEntityList.contains(entity))
                .sorted(Comparator.comparingDouble(entity -> mc.thePlayer.getDistanceSqToEntity(entity)))
                .collect(Collectors.toList());
    }
}
