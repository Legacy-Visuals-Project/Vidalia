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

import net.minecraft.client.gui.font.FontSet
import net.minecraft.client.gui.font.glyphs.BakedGlyph
import net.minecraft.util.RandomSource
import java.util.function.IntFunction

abstract class CharacterSet {
    abstract fun populate(glyphGetter: IntFunction<FontSet.SelectedGlyphs>, codepoint: Int)

    abstract fun reset()

    abstract fun pick(original: BakedGlyph, random: RandomSource, codepoint: Int): BakedGlyph?
}