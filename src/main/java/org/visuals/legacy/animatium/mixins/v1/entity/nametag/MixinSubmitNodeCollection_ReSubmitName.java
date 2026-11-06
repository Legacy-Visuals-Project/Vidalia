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

package org.visuals.legacy.animatium.mixins.v1.entity.nametag;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollection;
import net.minecraft.client.renderer.feature.TextFeatureRenderer;
import net.minecraft.client.renderer.feature.phase.TranslucentFeatureRenderPhase;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;

@Mixin(SubmitNodeCollection.class)
public abstract class MixinSubmitNodeCollection_ReSubmitName {
    @Shadow
    @Final
    public TranslucentFeatureRenderPhase seeThrough;

    @Unique
    private TextFeatureRenderer.Submit animatium$cachedNameSubmit;

    @WrapOperation(method = "submitNameTag", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SubmitNodeCollection;submitNameTagPart(Lnet/minecraft/client/renderer/feature/TextFeatureRenderer$Submit;)V", ordinal = 0))
    private void animatium$cacheNameSubmit(final SubmitNodeCollection instance, final TextFeatureRenderer.Submit submit, final Operation<Void> original) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().screen.legacyNameTagRendering) {
            this.animatium$cachedNameSubmit = submit;
        } else {
            original.call(instance, submit);
        }
    }

    @Inject(method = "submitNameTag", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/feature/phase/TranslucentFeatureRenderPhase;submit(Lnet/minecraft/client/renderer/feature/submit/TranslucentSubmit;)V", shift = At.Shift.AFTER))
    private void animatium$resubmitName(final PoseStack poseStack, final Vec3 nameTagAttachment, final int offset, final Component name, final boolean seeThrough, final int lightCoords, final CameraRenderState camera, final CallbackInfo ci) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().screen.legacyNameTagRendering && this.animatium$cachedNameSubmit != null) {
            this.seeThrough.submit(this.animatium$cachedNameSubmit);
            this.animatium$cachedNameSubmit = null;
        }
    }
}
