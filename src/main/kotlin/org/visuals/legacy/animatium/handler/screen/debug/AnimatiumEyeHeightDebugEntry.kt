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