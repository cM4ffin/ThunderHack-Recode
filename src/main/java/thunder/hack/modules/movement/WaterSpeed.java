package thunder.hack.modules.movement;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.render.entity.model.ParrotEntityModel;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import thunder.hack.events.impl.EventMove;
import thunder.hack.events.impl.EventPlayerTravel;
import thunder.hack.modules.Module;
import thunder.hack.setting.Setting;
import thunder.hack.utility.math.MathUtility;
import thunder.hack.utility.player.MovementUtility;

import static net.minecraft.entity.EntityPose.STANDING;

public class WaterSpeed extends Module {
    public WaterSpeed() {
        super("WaterSpeed", Category.MOVEMENT);
    }

    public final Setting<Mode> mode = new Setting<>("Mode", Mode.DolphinGrace);

    private float acceleration = 0f;

    public enum Mode {
        DolphinGrace, Intave
    }

    @Override
    public void onUpdate() {
        if (mode.getValue() == Mode.DolphinGrace) {
            if (mc.player.isSwimming()) mc.player.addStatusEffect(new StatusEffectInstance(StatusEffects.DOLPHINS_GRACE, 2, 2));
            else mc.player.removeStatusEffect(StatusEffects.DOLPHINS_GRACE);
        }
    }

    @EventHandler
    public void onMove(EventMove e) {
        if (mode.getValue() == Mode.Intave) {
            if (mc.player.isSwimming()) {
                double[] dirSpeed = MovementUtility.forward(acceleration / (mc.player.input.movementSideways != 0 ? 2.2f : 2f));
                e.setX(e.getX() + dirSpeed[0]);
                e.setZ(e.getZ() + dirSpeed[1]);
                e.cancel();
                acceleration += 0.05f;
                acceleration = MathUtility.clamp(acceleration, 0f, 1f);
            } else acceleration = 0f;
            if (!MovementUtility.isMoving()) acceleration = 0f;
        }
    }

    @Override
    public void onDisable() {
        if (mode.getValue() == Mode.DolphinGrace)
            mc.player.removeStatusEffect(StatusEffects.DOLPHINS_GRACE);
    }
}
