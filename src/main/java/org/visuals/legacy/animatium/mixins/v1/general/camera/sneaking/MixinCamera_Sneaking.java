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

package org.visuals.legacy.animatium.mixins.v1.general.camera.sneaking;

import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;
import org.visuals.legacy.animatium.handler.rendering.camera.LegacySneakAnimation;

@Mixin(Camera.class)
public abstract class MixinCamera_Sneaking {
    @Shadow
    private Entity entity;

    @Shadow
    private float eyeHeightOld;

    @Shadow
    private float eyeHeight;

    @Unique
    private final LegacySneakAnimation animatium$animation = new LegacySneakAnimation();

    @Unique
    private float animatium$tickDelta;

    @Inject(method = "update", at = @At("HEAD"))
    private void animatium$fetchTickDelta(final DeltaTracker deltaTracker, final CallbackInfo ci) {
        this.animatium$tickDelta = deltaTracker.getGameTimeDeltaPartialTick(false);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/attribute/EnvironmentAttributeProbe;tick(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/phys/Vec3;)V"))
    private void animatium$sneakAnimation(final CallbackInfo ci) {
        if (Animatium.isEnabled()) {
            this.animatium$animation.extract(this.entity, Minecraft.getInstance().options);
            if (AnimatiumConfig.instance().movement.sneakAnimation.overridesInterpolation() && this.animatium$animation.isApplicable()) {
                this.eyeHeightOld = this.animatium$animation.getPreviousEyeHeight();
                this.eyeHeight = this.animatium$animation.getEyeHeight();
            }

            this.animatium$animation.tick(this.animatium$tickDelta);
        }
    }
}
