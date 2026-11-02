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
import net.minecraft.client.gui.Font;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;

@Mixin(Font.PreparedTextBuilder.class)
public abstract class MixinFont$PreparedTextBuilder_LegacyBackgroundHeight {
    @ModifyExpressionValue(method = "markBackground", at = @At(value = "CONSTANT", args = "floatValue=9.0"))
    private float animatium$legacyHeightOffset(final float original) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().screen.legacyNameTagRendering) {
            return 8.5F; // NOTE: It was actually 8.0F but some font height thing changed in 1.21.4-1.21.11, and I can't be bothered
        } else {
            return original;
        }
    }
}
