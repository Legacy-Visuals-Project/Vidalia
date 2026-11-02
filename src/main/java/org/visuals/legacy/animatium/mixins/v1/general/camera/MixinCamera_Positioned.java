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

package org.visuals.legacy.animatium.mixins.v1.general.camera;

import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;
import org.visuals.legacy.animatium.util.enums.CameraVersionSetting;

@Mixin(Camera.class)
public abstract class MixinCamera_Positioned {
    @Shadow
    private Entity entity;

    @Shadow
    private boolean detached;

    @Shadow
    protected abstract void move(final float forwards, final float up, final float right);

    // TODO: Fix bed/sleeping position

    @Inject(method = "alignWithEntity", at = @At(value = "TAIL"))
    private void animatium$cameraVersion(final float partialTicks, final CallbackInfo ci) {
        final CameraVersionSetting cameraVersion = AnimatiumConfig.instance().screen.cameraVersion;
        if (Animatium.isEnabled() && cameraVersion != CameraVersionSetting.VANILLA && !this.detached && !(this.entity instanceof final LivingEntity livingEntity && livingEntity.isSleeping())) {
            if (cameraVersion == CameraVersionSetting.V1_8) {
                this.move(-0.1F, 0.0F, 0.0F);
            } else if (cameraVersion == CameraVersionSetting.V1_9_TO_V1_13_2) {
                this.move(0.05F, 0.0F, 0.0F);
            } else if (cameraVersion == CameraVersionSetting.V1_14_TO_V1_14_3) {
                this.move(-0.05F, 0.0F, 0.0F);
            }
        }
    }
}
