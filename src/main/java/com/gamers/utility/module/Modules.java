package com.gamers.utility.module;
import com.gamers.utility.setting.BooleanSetting;
import com.gamers.utility.setting.NumberSetting;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public final class Modules {
    private Modules() {}

    public static class KillAura extends Module {
        private final NumberSetting range = add(new NumberSetting("Range", 3.0, 1.0, 6.0, 0.1));
        private final BooleanSetting playersOnly = add(new BooleanSetting("Players only", true));
        private final BooleanSetting waitCooldown = add(new BooleanSetting("Wait for cooldown", true));
        private final NumberSetting delay = add(new NumberSetting("Delay (ticks)", 10, 1, 20, 1));
        private int ticks;
        public KillAura() { super("KillAura", "Attacks nearby targets", Category.COMBAT); }
        @Override public void onTick() {
            if (mc.player == null || mc.level == null || mc.gameMode == null) return;
            ticks++;
            Entity target = null;
            double best = range.get() * range.get();
            for (Entity e : mc.level.entitiesForRendering()) {
                if (e == mc.player || !(e instanceof LivingEntity) || !e.isAlive()) continue;
                if (playersOnly.get() && !(e instanceof Player)) continue;
                if (e instanceof Player p && p.isSpectator()) continue;
                double d = mc.player.distanceToSqr(e);
                if (d <= best) { best = d; target = e; }
            }
            if (target == null) return;
            if (waitCooldown.get()) { if (mc.player.getAttackStrengthScale(0.0f) < 1.0f) return; }
            else if (ticks < delay.getInt()) return;
            ticks = 0;
            mc.gameMode.attack(mc.player, target);
        }
    }

    public static class Sprint extends Module {
        public Sprint() { super("Sprint", "Always sprint while moving forward", Category.MOVEMENT); }
        @Override public void onTick() {
            if (mc.player == null) return;
            if (mc.player.input.keyPresses.forward()) mc.player.setSprinting(true);
        }
    }

    public static class Speed extends Module {
        private final NumberSetting speed = add(new NumberSetting("Speed", 0.35, 0.1, 1.5, 0.05));
        public Speed() { super("Speed", "Move faster on the ground and in the air", Category.MOVEMENT); }
        @Override public void onTick() {
            if (mc.player == null) return;
            var in = mc.player.input.keyPresses;
            double f = (in.forward() ? 1 : 0) - (in.backward() ? 1 : 0);
            double s = (in.right() ? 1 : 0) - (in.left() ? 1 : 0);
            if (f == 0 && s == 0) return;
            double len = Math.sqrt(f * f + s * s);
            f /= len; s /= len;
            double yaw = Math.toRadians(mc.player.getYRot());
            double sin = Math.sin(yaw), cos = Math.cos(yaw);
            double dx = (f * -sin + s * -cos) * speed.get();
            double dz = (f * cos + s * -sin) * speed.get();
            Vec3 v = mc.player.getDeltaMovement();
            mc.player.setDeltaMovement(v.multiply(0.0, 1.0, 0.0).add(dx, 0.0, dz));
        }
    }

    public static class Fly extends Module {
        private final NumberSetting speed = add(new NumberSetting("Speed", 1.0, 0.5, 5.0, 0.5));
        private boolean savedMayFly;
        private float savedSpeed;
        public Fly() { super("Fly", "Creative-style flight", Category.MOVEMENT); }
        @Override public void onEnable() {
            if (mc.player == null) return;
            var a = mc.player.getAbilities();
            savedMayFly = a.mayfly;
            savedSpeed = a.getFlyingSpeed();
        }
        @Override public void onTick() {
            if (mc.player == null) return;
            var a = mc.player.getAbilities();
            a.mayfly = true;
            a.flying = true;
            a.setFlyingSpeed((float) (speed.get() * 0.05));
        }
        @Override public void onDisable() {
            if (mc.player == null) return;
            var a = mc.player.getAbilities();
            a.mayfly = savedMayFly;
            if (!savedMayFly) a.flying = false;
            a.setFlyingSpeed(savedSpeed);
        }
    }

    public static class Fullbright extends Module {
        private Double saved;
        public Fullbright() { super("Fullbright", "Maximum brightness", Category.VISUAL); }
        @Override public void onEnable() {
            saved = mc.options.gamma().get();
            mc.options.gamma().set(1.0);
        }
        @Override public void onDisable() {
            if (saved != null) mc.options.gamma().set(saved);
        }
    }
}
