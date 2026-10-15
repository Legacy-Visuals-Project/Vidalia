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

package org.visuals.legacy.animatium.handler.screen.debug

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.debug.DebugScreenDisplayer
import net.minecraft.client.gui.components.debug.DebugScreenEntry
import net.minecraft.world.level.Level
import net.minecraft.world.level.chunk.LevelChunk
import org.visuals.legacy.animatium.Animatium
import org.visuals.legacy.animatium.AnimatiumConstants

class AnimatiumEyeHeightDebugEntry : DebugScreenEntry {
    companion object {
        val GROUP = Animatium.location("eye_height")
    }

    override fun display(
        displayer: DebugScreenDisplayer,
        serverOrClientLevel: Level?,
        clientChunk: LevelChunk?,
        serverChunk: LevelChunk?
    ) {
        val entity = Minecraft.getInstance().player
        if (entity != null) {
            val list = arrayListOf<String>()
            list.add("Eye Height: ${entity.eyeHeight}")
            list.add("Holding Sneak: ${Minecraft.getInstance().options.keyShift.isDown}")
            displayer.addToGroup(GROUP, list)
        }
    }

    override fun isAllowed(reducedDebugInfo: Boolean) = true

    override fun category() = AnimatiumConstants.DEBUG_CATEGORY
}