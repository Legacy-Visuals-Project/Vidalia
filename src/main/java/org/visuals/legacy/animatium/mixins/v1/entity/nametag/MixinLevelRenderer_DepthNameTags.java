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
import com.mojang.renderpearl.api.commands.CommandEncoder;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.pipeline.RenderPassLayout;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderPassLayouts;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;

import java.util.Objects;
import java.util.function.Supplier;

@Mixin(LevelRenderer.class)
public class MixinLevelRenderer_DepthNameTags {
    @WrapOperation(method = "executeSeeThrough", at = @At(value = "INVOKE", target = "Lcom/mojang/renderpearl/api/commands/CommandEncoder;createRenderPass(Ljava/util/function/Supplier;Lcom/mojang/renderpearl/api/pipeline/RenderPassLayout;Lcom/mojang/renderpearl/api/textures/GpuTextureView;[Lcom/mojang/renderpearl/api/textures/GpuTextureView;)Lcom/mojang/renderpearl/api/commands/RenderPass;"))
    private RenderPass animatium$addDepthTexture(final CommandEncoder instance, final Supplier<String> label, final RenderPassLayout layout, final GpuTextureView firstAttachmentView, final GpuTextureView[] additionalAttachmentViews, final Operation<RenderPass> original, @Local(argsOnly = true, name = "mainTarget") final RenderTarget renderTarget) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().screen.legacyNameTagRendering && renderTarget.hasDepth()) {
            return original.call(instance, label, RenderPassLayouts.COLOR_DEPTH, firstAttachmentView, new GpuTextureView[]{Objects.requireNonNull(renderTarget.getDepthTextureView())});
        } else {
            return original.call(instance, label, layout, firstAttachmentView, additionalAttachmentViews);
        }
    }
}
