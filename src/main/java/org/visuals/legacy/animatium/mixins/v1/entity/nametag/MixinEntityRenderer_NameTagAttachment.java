/**
 * Animatium
 * The all-you-could-want legacy animations mod for modern minecraft versions.
 * Brings back animations from the 1.7/1.8 era and more.
 * <p>
 * Copyright (C) 2024-2027 lowercasebtw
 * Copyright (C) 2024-2027 mixces
 * Copyright (C) 2024-2027 Contributors to the project retain their copyright
 * <p>
 * Licensed under the PolyForm Shield License 1.0.0.
 * You may obtain a copy of the license at
 * https://polyformproject.org/licenses/shield/1.0.0
 */

package org.visuals.legacy.animatium.mixins.v1.entity.nametag;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer_NameTagAttachment<T extends Entity, S extends EntityRenderState> {
    @ModifyExpressionValue(method = "submitNameDisplay(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;I)V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/entity/state/EntityRenderState;nameTagAttachment:Lnet/minecraft/world/phys/Vec3;"))
    private Vec3 animatium$changeNameTagAttachment(final Vec3 original, @Local(argsOnly = true, name = "state") final S state) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().screen.legacyNameTagRendering) {
            float yOffset = state.boundingBoxHeight;
            if (AnimatiumConfig.instance().movement.fakeOldSneakEyeHeight) {
                yOffset = 1.8F;
                if (state.isDiscrete) {
                    yOffset -= 0.25F;
                }

                yOffset *= state instanceof final LivingEntityRenderState livingEntityRenderState ? livingEntityRenderState.scale : 1.0F;
            }

            return new Vec3(0.0D, yOffset, 0.0D);
        } else {
            return original;
        }
    }
}
