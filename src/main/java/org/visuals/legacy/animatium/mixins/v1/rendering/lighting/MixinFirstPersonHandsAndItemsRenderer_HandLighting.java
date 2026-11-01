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

package org.visuals.legacy.animatium.mixins.v1.rendering.lighting;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FirstPersonHandsAndItemsRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.FirstPersonHandsAndItemsRenderState;
import net.minecraft.client.renderer.state.level.PlayerRenderState;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;
import org.visuals.legacy.animatium.handler.rendering.lighting.LegacyDiffuseLighting;

@Mixin(FirstPersonHandsAndItemsRenderer.class)
public abstract class MixinFirstPersonHandsAndItemsRenderer_HandLighting {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method = "submitHandsWithItems", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;swingAnimation:F", opcode = Opcodes.GETFIELD))
    private void animatium$legacyHandLighting(final float tickDelta, final PoseStack poseStack, final SubmitNodeCollector submitNodeCollector, final PlayerRenderState playerState, final FirstPersonHandsAndItemsRenderState state, final CallbackInfo ci) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().other.legacyDiffuseLighting) {
            final Lighting lighting = this.minecraft.gameRenderer.lighting();
            LegacyDiffuseLighting.updateDiffuse(
                    lighting,
                    Lighting.Entry.PLAYER_SKIN,
                    new Matrix4f(poseStack.last().pose())
                            .rotateX(state.viewXRot * Mth.DEG_TO_RAD)
                            .rotateY(state.viewYRot * Mth.DEG_TO_RAD));
            lighting.setupFor(Lighting.Entry.PLAYER_SKIN);
        }
    }
}
