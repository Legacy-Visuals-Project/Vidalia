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

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.platform.Lighting;
import net.minecraft.world.level.CardinalLighting;
import org.joml.Vector3fc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;
import org.visuals.legacy.animatium.handler.rendering.lighting.LegacyDiffuseLighting;

@Mixin(Lighting.class)
public abstract class MixinLighting_LegacyDiffuseLighting {
    @Unique
    private boolean animatium$recording = true;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void animatium$legacyDiffuseLighting(final CallbackInfo ci) {
        this.animatium$recording = false;

        LegacyDiffuseLighting.save(); // Save a copy of vanilla lighting data, for use later to restore
        if (AnimatiumConfig.instance().other.legacyDiffuseLighting) {
            LegacyDiffuseLighting.update((Lighting) (Object) this);
        }
    }

    @WrapMethod(method = "updateBuffer")
    private void animatium$snapshot(final Lighting.Entry entry, final Vector3fc light0, final Vector3fc light1, final Operation<Void> original) {
        if (this.animatium$recording) {
            LegacyDiffuseLighting.record(entry, light0, light1);
        }

        original.call(entry, light0, light1);
    }

    @ModifyVariable(method = "updateLevel", at = @At("HEAD"), name = "type", argsOnly = true)
    private CardinalLighting.Type animatium$legacyDiffuseLighting$level(final CardinalLighting.Type type) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().other.legacyDiffuseLighting) {
            return CardinalLighting.Type.DEFAULT;
        } else {
            return type;
        }
    }
}
