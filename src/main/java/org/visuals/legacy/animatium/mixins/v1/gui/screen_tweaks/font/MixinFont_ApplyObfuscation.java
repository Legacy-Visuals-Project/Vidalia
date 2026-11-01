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

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GlyphSource;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.util.RandomSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;
import org.visuals.legacy.animatium.handler.rendering.font.RandomGlyphHelper;
import org.visuals.legacy.animatium.handler.rendering.font.character_sets.CharacterSet;
import org.visuals.legacy.animatium.util.enums.TextObfuscationSetting;

import java.util.Objects;

@Mixin(Font.class)
public abstract class MixinFont_ApplyObfuscation {
    @Definition(id = "codepoint", local = @Local(type = int.class, name = "codepoint", argsOnly = true))
    @Expression("codepoint != 32")
    @ModifyExpressionValue(method = "getGlyph", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean animatium$removeSpaceCheck(final boolean original) {
        return (Animatium.isEnabled() && AnimatiumConfig.instance().screen.textObfuscation != TextObfuscationSetting.VANILLA) || original;
    }

    @WrapOperation(method = "getGlyph", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GlyphSource;getRandomGlyph(Lnet/minecraft/util/RandomSource;I)Lnet/minecraft/client/gui/font/glyphs/BakedGlyph;"))
    private BakedGlyph animatium$applyTextObfuscation(final GlyphSource instance, final RandomSource random, final int targetWidth, final Operation<BakedGlyph> original, @Local(name = "glyph") final BakedGlyph glyph, @Local(name = "codepoint", argsOnly = true) final int codepoint) {
        final CharacterSet activeSet = RandomGlyphHelper.activeSet();
        if (Animatium.isEnabled() && activeSet != null) {
            return Objects.requireNonNullElse(activeSet.pick(glyph, random, codepoint), FontSet.INVISIBLE_MISSING_GLYPH);
        } else {
            return original.call(instance, random, targetWidth);
        }
    }
}
