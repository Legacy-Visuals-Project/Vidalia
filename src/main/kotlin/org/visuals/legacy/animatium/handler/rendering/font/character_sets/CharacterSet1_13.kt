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

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap
import net.minecraft.client.gui.font.FontSet
import net.minecraft.client.gui.font.glyphs.BakedGlyph
import net.minecraft.util.Mth
import net.minecraft.util.RandomSource
import net.minecraft.util.Util
import java.util.function.IntFunction

class CharacterSet1_13 : CharacterSet() {
    private val glyphsByWidth = Int2ObjectArrayMap<List<BakedGlyph>>()

    override fun populate(glyphGetter: IntFunction<FontSet.SelectedGlyphs>, codepoint: Int) {
        val glyph = glyphGetter.apply(codepoint).any.get()
        val key = Mth.floor(glyph.info().advance)
        (this.glyphsByWidth.computeIfAbsent(key) { arrayListOf() } as MutableList<BakedGlyph>).add(glyph)
    }

    override fun reset() {
        this.glyphsByWidth.clear()
    }

    override fun pick(original: BakedGlyph, random: RandomSource, codepoint: Int): BakedGlyph? {
        if (codepoint != 32) {
            val info = original.info()
            val glyphs = this.glyphsByWidth.get(Mth.floor(info.advance))
            return if (glyphs != null && glyphs.isNotEmpty()) {
                glyphs[((Util.getMillis() % 100L).toInt() + info.hashCode()) % glyphs.size]
            } else {
                null
            }
        } else {
            return original
        }
    }
}