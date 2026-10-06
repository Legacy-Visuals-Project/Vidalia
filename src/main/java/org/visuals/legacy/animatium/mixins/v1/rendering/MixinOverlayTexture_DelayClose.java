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

package org.visuals.legacy.animatium.mixins.v1.rendering;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.mojang.renderpearl.api.textures.GpuTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OverlayTexture.class)
public abstract class MixinOverlayTexture_DelayClose {
    @Unique
    private GpuTexture animatium$texture = null;

    @WrapWithCondition(method = "<init>", at = @At(value = "INVOKE", target = "Lcom/mojang/renderpearl/api/textures/GpuTexture;close()V"))
    private boolean animatium$delayClose(final GpuTexture instance) {
        this.animatium$texture = instance;
        return false;
    }

    @Inject(method = "close", at = @At("HEAD"))
    private void animatium$closeOverlayTexture(final CallbackInfo ci) {
        if (this.animatium$texture != null) {
            this.animatium$texture.close();
        }
    }
}
