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
import net.minecraft.client.renderer.SubmitNodeCollection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;

@Mixin(SubmitNodeCollection.class)
public abstract class MixinSubmitNodeCollection_LegacyNameTag {
    @ModifyConstant(method = "submitNameTag", constant = @Constant(floatValue = 0.025F))
    private static float animatium$legacyNameTagScale(final float original) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().screen.legacyNameTagRendering) {
            return 0.02666667F;
        } else {
            return original;
        }
    }

    // TODO: Figure out why 1.21.2 produces a +1 making the name offset left too much
    @ModifyExpressionValue(method = "submitNameTag", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Font;width(Lnet/minecraft/network/chat/FormattedText;)I"))
    private int animatium$offsetNameTextWidth(final int original) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().screen.legacyNameTagRendering) {
            return original - 1;
        } else {
            return original;
        }
    }

    @ModifyExpressionValue(method = "submitNameTag", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ARGB;color(FI)I", ordinal = 1))
    private int animatium$legacyCrouchingNameColor(final int original) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().screen.legacyNameTagRendering) {
            return 553648127;
        } else {
            return original;
        }
    }
}
