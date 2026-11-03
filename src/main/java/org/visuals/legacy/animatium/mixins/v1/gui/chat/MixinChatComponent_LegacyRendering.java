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

package org.visuals.legacy.animatium.mixins.v1.gui.chat;

import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.components.ChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;

@Mixin(ChatComponent.class)
public abstract class MixinChatComponent_LegacyRendering {
    @ModifyConstant(method = "lambda$extractRenderState$0", constant = @Constant(floatValue = 4.0F))
    private static float animatium$modifyChatXOffset(final float original) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().screen.legacyChatRendering) {
            return 2.0F;
        } else {
            return original;
        }
    }

    @ModifyConstant(method = "extractRenderState(Lnet/minecraft/client/gui/components/ChatComponent$ChatGraphicsAccess;IILnet/minecraft/client/gui/components/ChatComponent$DisplayMode;)V", constant = @Constant(intValue = 40))
    private int animatium$modifyChatBottomMargin(final int original) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().screen.legacyChatRendering) {
            return 28;
        } else {
            return original;
        }
    }

    @ModifyConstant(method = {"lambda$extractRenderState$1", "lambda$processMessageDeletionQueue$0"}, constant = @Constant(intValue = -4))
    private static int animatium$removeBackgroundXOffset(final int original) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().screen.legacyChatRendering) {
            return 0;
        } else {
            return original;
        }
    }

    @Expression("? + 4 + @(?)")
    @ModifyExpressionValue(method = "lambda$extractRenderState$1", at = @At("MIXINEXTRAS:EXPRESSION"))
    private static int animatium$removeBackgroundWidthPadding(final int original) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().screen.legacyChatRendering) {
            return 0;
        } else {
            return original;
        }
    }
}
