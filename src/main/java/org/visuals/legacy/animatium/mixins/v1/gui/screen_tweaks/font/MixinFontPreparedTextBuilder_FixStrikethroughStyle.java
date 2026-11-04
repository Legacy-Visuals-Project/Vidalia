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

package org.visuals.legacy.animatium.mixins.v1.gui.screen_tweaks.font;

import net.minecraft.client.gui.Font;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;

/**
 * This changed in 1.13 pre6 due to a (int -> float) change
 * The calculation was (? + fontHeight / 2) where the 2 changed to 2.0F so (9 / 2.0F = 4.5) instead of 4 due to casting.
 * For note, fontHeight is 9
 */
@Mixin(Font.PreparedTextBuilder.class)
public abstract class MixinFontPreparedTextBuilder_FixStrikethroughStyle {
    @Shadow
    @Final
    Font this$0;

    @ModifyConstant(method = "accept(ILnet/minecraft/network/chat/Style;Lnet/minecraft/client/gui/font/glyphs/BakedGlyph;)Z", constant = @Constant(floatValue = 4.5F))
    private float animatium$fixTextStrikethroughStyle(final float original) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().fixes.fixTextStrikethroughStyle) {
            return (float) (this.this$0.lineHeight / 2);
        } else {
            return original;
        }
    }
}
