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

package org.visuals.legacy.animatium.mixins.v1.general.server_features;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.visuals.legacy.animatium.handler.server_features.ServerFeatureManager;
import org.visuals.legacy.animatium.handler.server_features.ServerFeatures;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity_ClientSideMovement {
    @ModifyExpressionValue(method = "aiStep", at = {
            @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;canSimulateMovement()Z", ordinal = 1),
            @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isEffectiveAi()Z", ordinal = 1)
    })
    private boolean animatium$clientSideEntityMovement(final boolean original) {
        return ServerFeatureManager.isPresent(ServerFeatures.CLIENTSIDE_ENTITIES) || original;
    }
}
