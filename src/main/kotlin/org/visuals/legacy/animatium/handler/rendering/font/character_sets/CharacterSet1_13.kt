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

package org.visuals.legacy.animatium.handler.rendering.font.character_sets

import com.mojang.blaze3d.font.GlyphInfo
import net.minecraft.client.gui.font.glyphs.BakedGlyph
import net.minecraft.util.RandomSource
import net.minecraft.util.Util

class CharacterSet1_13 : CharacterSet() {
    override fun decide(info: GlyphInfo, glyphs: List<BakedGlyph>, random: RandomSource) =
        glyphs[((Util.getMillis() % 100L).toInt() + info.hashCode()) % glyphs.size]

    override fun pick(original: BakedGlyph, random: RandomSource, codepoint: Int) = if (codepoint != 32) {
        super.pick(original, random, codepoint)
    } else {
        original
    }
}