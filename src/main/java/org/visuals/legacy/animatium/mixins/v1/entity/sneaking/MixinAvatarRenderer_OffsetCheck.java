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

package org.visuals.legacy.animatium.mixins.v1.entity.sneaking;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Avatar;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;
import org.visuals.legacy.animatium.util.EntityUtilKt;
import org.visuals.legacy.animatium.util.enums.SneakAnimationSetting;

@Mixin(AvatarRenderer.class)
public abstract class MixinAvatarRenderer_OffsetCheck<AvatarLikeEntity extends Avatar & ClientAvatarEntity> extends LivingEntityRenderer<AvatarLikeEntity, AvatarRenderState, PlayerModel> {
    public MixinAvatarRenderer_OffsetCheck(final EntityRendererProvider.Context context, final PlayerModel model, final float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @ModifyExpressionValue(method = "getRenderOffset(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;)Lnet/minecraft/world/phys/Vec3;", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;isCrouching:Z", opcode = Opcodes.GETFIELD))
    private boolean animatium$onlyOffsetForOthers(final boolean original, @Local(argsOnly = true, name = "state") final AvatarRenderState state) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().movement.sneakAnimation.ordinal() <= SneakAnimationSetting.V1_8.ordinal()) {
            return original && !EntityUtilKt.isSelf(state);
        } else {
            return original;
        }
    }
}
