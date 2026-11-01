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

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.font.CodepointMap;
import net.minecraft.client.gui.font.FontSet;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.visuals.legacy.animatium.handler.rendering.font.RandomGlyphHelper;
import org.visuals.legacy.animatium.handler.rendering.font.character_sets.CharacterSet;

import java.util.function.IntFunction;

@Mixin(FontSet.class)
public abstract class MixinFontSet_LegacyObfuscation {
    @Shadow
    @Final
    private CodepointMap<FontSet.SelectedGlyphs> glyphCache;

    @Inject(method = "resetTextures", at = @At("TAIL"))
    private void animatium$clearGlyphs(final CallbackInfo ci) {
        final CharacterSet activeSet = RandomGlyphHelper.activeSet();
        if (activeSet != null) {
            activeSet.reset();
        }
    }

    @ModifyExpressionValue(method = "getGlyph", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/font/FontSet;glyphGetter:Ljava/util/function/IntFunction;", opcode = Opcodes.GETFIELD))
    private IntFunction<FontSet.SelectedGlyphs> animatium$populateGlyphs(final IntFunction<FontSet.SelectedGlyphs> glyphGetter) {
        final CharacterSet activeSet = RandomGlyphHelper.activeSet();
        if (RandomGlyphHelper.requestedReload()) {
            if (activeSet != null) {
                activeSet.reset();
            }

            this.glyphCache.clear();
        }

        return codepoint -> {
            if (activeSet != null) {
                activeSet.populate(glyphGetter, codepoint);
            }

            return glyphGetter.apply(codepoint);
        };
    }
}
