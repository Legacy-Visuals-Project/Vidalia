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

package org.visuals.legacy.animatium.handler.rendering.font

import org.visuals.legacy.animatium.config.AnimatiumConfig
import org.visuals.legacy.animatium.handler.rendering.font.character_sets.CharacterSet1_12
import org.visuals.legacy.animatium.handler.rendering.font.character_sets.CharacterSet1_13
import org.visuals.legacy.animatium.util.enums.TextObfuscationSetting

object RandomGlyphHelper {
    private val charset1_12 = CharacterSet1_12()
    private val charset1_13 = CharacterSet1_13()

    private var requestedReload = false

    @JvmStatic
    fun reload() {
        this.requestedReload = true
    }

    @JvmStatic
    fun activeSet() = when (AnimatiumConfig.instance().screen.textObfuscation) {
        TextObfuscationSetting.V1_12_2 -> this.charset1_12
        TextObfuscationSetting.V1_13 -> this.charset1_13
        else -> null
    }

    @JvmStatic
    fun requestedReload(): Boolean {
        val requestedReload = this.requestedReload
        this.requestedReload = false
        return requestedReload
    }
}