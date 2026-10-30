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

package org.visuals.legacy.animatium.handler.rendering.lighting

import com.mojang.blaze3d.platform.Lighting
import net.minecraft.util.Mth
import org.joml.Matrix4f
import org.joml.Vector3f
import org.joml.Vector3fc

object LegacyDiffuseLighting {
    private val UI_ENTITY_MATRIX = Matrix4f()
        .rotateZ(180.0F * Mth.DEG_TO_RAD)
        .rotateY(135.0F * Mth.DEG_TO_RAD)
    private val UI_ITEM_MATRIX = Matrix4f()
        .rotateY(-30.0F * Mth.DEG_TO_RAD)
        .rotateX(165.0F * Mth.DEG_TO_RAD)
    private val DIFFUSE_LIGHT_0 = (Vector3f(0.2F, 1.0F, -0.7F)).normalize()
    private val DIFFUSE_LIGHT_1 = (Vector3f(-0.2F, 1.0F, 0.7F)).normalize()

    private var map = hashMapOf<Lighting.Entry, LightDirections>()
    private var snapshot: LightSnapshot? = null

    @JvmStatic
    fun record(entry: Lighting.Entry, light0: Vector3fc, light1: Vector3fc) {
        if (this.snapshot == null) {
            this.map[entry] = LightDirections(light0, light1)
        }
    }

    @JvmStatic
    fun save() {
        if (this.snapshot == null) {
            this.snapshot = LightSnapshot(this.map.toMap())
            this.map.clear()
        }
    }

    @JvmStatic
    fun restore(lighting: Lighting) {
        val snapshot = this.snapshot ?: return
        for ((entry, directions) in snapshot.entries) {
            lighting.updateBuffer(entry, directions.light0, directions.light1)
        }
    }

    @JvmStatic
    fun apply(lighting: Lighting) {
        this.applyLight(lighting, Lighting.Entry.PLAYER_SKIN, UI_ENTITY_MATRIX)
        this.applyLight(lighting, Lighting.Entry.ENTITY_IN_UI, UI_ENTITY_MATRIX)
        this.applyLight(lighting, Lighting.Entry.ITEMS_3D, UI_ITEM_MATRIX)
    }

    private fun applyLight(lighting: Lighting, entry: Lighting.Entry, matrix4f: Matrix4f) {
        val light0 = matrix4f.transformDirection(DIFFUSE_LIGHT_0, Vector3f())
        val light1 = matrix4f.transformDirection(DIFFUSE_LIGHT_1, Vector3f())
        lighting.updateBuffer(entry, light0, light1)
    }

    data class LightSnapshot(val entries: Map<Lighting.Entry, LightDirections>)

    data class LightDirections(val light0: Vector3fc, val light1: Vector3fc)
}