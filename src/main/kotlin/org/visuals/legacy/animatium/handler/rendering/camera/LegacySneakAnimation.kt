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
class LegacySneakAnimation {
    private var previousDecay = 0.0F
    private var decay = 0.0F

    private var lastModifiedEyeHeight = 0.0F
    private var modifiedEyeHeight = 0.0F

    @get:JvmName("isApplicable")
    var applicable = false
        private set
    private var scale = 1.0F
    private var standingEyeHeight = 0.0F
    private var crouchingEyeHeight = 0.0F
    private var currentEyeHeight = 0.0F
    private var sneaking = false

    fun extract(entity: Entity, options: Options) {
        this.applicable = entity.hasPose(Pose.CROUCHING) || entity.hasPose(Pose.STANDING);
        if (this.applicable) {
            this.scale = entity.getScale()
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

    fun tick(tickDelta: Float) {
        val movement = AnimatiumConfig.instance().movement

        // Tick Decay
        this.previousDecay = this.decay
        if (this.sneaking) {
            this.decay = if (movement.fakeOldSneakEyeHeight)
                0.08F
            else
                this.standingEyeHeight - this.crouchingEyeHeight
        } else {
            this.decay *= 0.4F
        }

        // Update Eye Height
        this.lastModifiedEyeHeight = this.modifiedEyeHeight
        this.modifiedEyeHeight = if (movement.sneakAnimation.hasSneakDecay()) {
            (this.standingEyeHeight - this.getDecayHeight(tickDelta)) * this.scale
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

    fun getPreviousEyeHeight() = this.lastModifiedEyeHeight

    fun getEyeHeight() = this.modifiedEyeHeight

    private fun getDecayHeight(tickDelta: Float) = Mth.lerp(tickDelta, this.previousDecay, this.decay)
}