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

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;

@Mixin(targets = "net.minecraft.client.gui.components.ChatComponent$1")
public abstract class MixinChatComponent$1_LegacyRendering {
    @ModifyConstant(method = "accept", constant = @Constant(intValue = -4))
    private static int animatium$removeBackgroundXOffset(final int original) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().screen.legacyChatRendering) {
            return 0;
        } else {
            return original;
        }
    }
}