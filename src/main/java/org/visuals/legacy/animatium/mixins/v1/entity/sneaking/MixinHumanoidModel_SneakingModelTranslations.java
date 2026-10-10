package org.visuals.legacy.animatium.mixins.v1.entity.sneaking;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;
import org.visuals.legacy.animatium.util.enums.SneakAnimationSetting;

@Mixin(HumanoidModel.class)
public abstract class MixinHumanoidModel_SneakingModelTranslations {
    @Shadow
    @Final
    public ModelPart body;

    @Shadow
    @Final
    public ModelPart rightArm;

    @Shadow
    @Final
    public ModelPart leftArm;

    @Shadow
    @Final
    public ModelPart rightLeg;

    @Shadow
    @Final
    public ModelPart leftLeg;

    @Shadow
    @Final
    public ModelPart head;

    @ModifyExpressionValue(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;isCrouching:Z"))
    private boolean animatium$sneakingModelOffsets(final boolean original) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().movement.sneakAnimation == SneakAnimationSetting.V1_7 && original) {
            // Values sourced from older versions
            this.body.xRot = 0.5F;
            this.rightArm.xRot += 0.4F;
            this.leftArm.xRot += 0.4F;
            this.rightLeg.z = 4.0F;
            this.leftLeg.z = 4.0F;
            this.rightLeg.y = 9.0F;
            this.leftLeg.y = 9.0F;
            this.head.y = 1.0F;
            return false;
        } else {
            return original;
        }
    }
}
