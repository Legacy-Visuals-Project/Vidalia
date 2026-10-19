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

package org.visuals.legacy.animatium.handler.rendering.camera

import net.minecraft.client.Options
import net.minecraft.util.Mth
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.Pose
import net.minecraft.world.entity.player.Player
import org.visuals.legacy.animatium.config.AnimatiumConfig
import org.visuals.legacy.animatium.mixins.accessor.PlayerAccessor
import org.visuals.legacy.animatium.util.getScale

// TODO: Fix going under a slab
class SneakAnimationState {
    private var previousDecay = 0.0F
    private var decay = 0.0F

    @get:JvmName("isApplicable")
    var applicable = false
    var scale = 1.0F
    var vanillaEyeHeight = 0.0F
    var standingEyeHeight = 0.0F
    var crouchingEyeHeight = 0.0F
    var currentEyeHeight = 0.0F
    var sneaking = false

    fun extract(entity: Entity, options: Options) {
        this.applicable = entity.hasPose(Pose.CROUCHING) || entity.hasPose(Pose.STANDING);
        if (this.applicable) {
            this.scale = entity.getScale()
            this.vanillaEyeHeight = entity.eyeHeight
            this.standingEyeHeight = entity.getDimensions(Pose.STANDING).eyeHeight
            this.crouchingEyeHeight = entity.getDimensions(Pose.CROUCHING).eyeHeight
            this.currentEyeHeight = entity.getDimensions(entity.pose).eyeHeight

            val possiblySneaking = options.keyShift.isDown
            if (entity is Player) {
                this.sneaking = possiblySneaking || !(entity as PlayerAccessor).`animatium$canChangeIntoPose`(Pose.STANDING)
            } else {
                this.sneaking = possiblySneaking
            }
        }
    }

    fun tick() {
        this.previousDecay = this.decay
        if (this.sneaking) {
            this.decay = if (AnimatiumConfig.instance().movement.fakeOldSneakEyeHeight)
                0.08F
            else
                this.standingEyeHeight - this.crouchingEyeHeight
        } else {
            this.decay *= 0.4F
        }
    }

    fun getEyeHeight(tickDelta: Float): Float {
        val movement = AnimatiumConfig.instance().movement
        return if (movement.sneakAnimation.hasSneakDecay()) {
            (this.standingEyeHeight - this.decay(tickDelta)) * this.scale
        } else if (this.sneaking) {
            if (movement.fakeOldSneakEyeHeight) {
                1.54F * this.scale
            } else {
                this.crouchingEyeHeight * this.scale
            }
        } else {
            this.currentEyeHeight
        }
    }

    private fun decay(tickDelta: Float) = Mth.lerp(tickDelta, this.previousDecay, this.decay)
}