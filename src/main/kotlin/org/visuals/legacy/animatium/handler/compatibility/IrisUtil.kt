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

package org.visuals.legacy.animatium.handler.compatibility

import com.mojang.blaze3d.pipeline.RenderPipeline
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import net.fabricmc.loader.api.FabricLoader
import net.irisshaders.iris.Iris
import net.irisshaders.iris.api.v0.IrisApi
import net.irisshaders.iris.api.v0.IrisProgram
import net.irisshaders.iris.pipeline.WorldRenderingPhase
import java.util.*

object IrisUtil {
    private val HAS_IRIS = FabricLoader.getInstance().isModLoaded("iris")

    private val pipelineCache = Object2ObjectOpenHashMap<RenderPipeline, IrisPipeline>()

    init {
        if (HAS_IRIS) {
            val irisProgramEnum = Class.forName("net.irisshaders.iris.api.v0.IrisProgram").asSubclass(Enum::class.java)
            Arrays.stream(IrisPipeline.VALUES).forEach { it.initialize(irisProgramEnum) }

            val irisPhaseEnum = Class.forName("net.irisshaders.iris.pipeline.WorldRenderingPhase").asSubclass(Enum::class.java)
            Arrays.stream(IrisPhase.VALUES).forEach { it.initialize(irisPhaseEnum) }
        }
    }

    @JvmStatic
    fun assignPipeline(pipeline: RenderPipeline, program: IrisPipeline) {
        if (HAS_IRIS) {
            if (pipelineCache.containsKey(pipeline) && pipelineCache[pipeline] == program) {
                return
            }

            pipelineCache[pipeline] = program
            IrisApi.getInstance().assignPipeline(pipeline, program.internal() as IrisProgram)
        }
    }

    @JvmStatic
    fun setPhase(phase: IrisPhase) {
        if (HAS_IRIS) {
            Iris.getPipelineManager().pipeline.ifPresent { it.phase = phase.internal() as WorldRenderingPhase }
        }
    }

    @JvmStatic
    fun isShaderActive() = HAS_IRIS && IrisApi.getInstance().isShaderPackInUse
}