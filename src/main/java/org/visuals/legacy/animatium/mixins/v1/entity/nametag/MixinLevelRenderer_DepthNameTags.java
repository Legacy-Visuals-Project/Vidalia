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
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.renderpearl.api.commands.RenderPassDescriptor;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;

import java.util.Objects;

@Mixin(LevelRenderer.class)
public class MixinLevelRenderer_DepthNameTags {
    @WrapOperation(method = "executeSeeThrough", at = @At(value = "INVOKE", target = "Lcom/mojang/renderpearl/api/commands/RenderPassDescriptor$Builder;withColorAttachment(Lcom/mojang/renderpearl/api/textures/GpuTextureView;)Lcom/mojang/renderpearl/api/commands/RenderPassDescriptor$Builder;"))
    private RenderPassDescriptor.Builder animatium$addDepthTexture(final RenderPassDescriptor.Builder instance, final GpuTextureView textureView, final Operation<RenderPassDescriptor.Builder> original, @Local(argsOnly = true, name = "mainTarget") final RenderTarget renderTarget) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().screen.legacyNameTagRendering && renderTarget.hasDepth()) {
            instance.withDepthAttachment(Objects.requireNonNull(renderTarget.getDepthTextureView()));
        }

        return original.call(instance, textureView);
    }
}
