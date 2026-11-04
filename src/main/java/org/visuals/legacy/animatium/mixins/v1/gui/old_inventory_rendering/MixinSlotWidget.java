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

package org.visuals.legacy.animatium.mixins.v1.gui.old_inventory_rendering;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.GuiPipeline;
import net.minecraft.client.gui.components.inventory.SlotWidget;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;

@Mixin(SlotWidget.class)
public abstract class MixinSlotWidget {
    @Shadow
    @Final
    protected static Identifier SLOT_HIGHLIGHT_FRONT_SPRITE;

    @WrapOperation(method = "extractSlotHighlight", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lnet/minecraft/client/gui/GuiPipeline;Lnet/minecraft/resources/Identifier;IIII)V"))
    private void animatium$slotHoverStyleRendering(final GuiGraphicsExtractor instance, final GuiPipeline pipeline, final Identifier location, final int x, final int y, final int width, final int height, final Operation<Void> original) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().screen.slotHoverStyleRendering) {
            if (location.equals(SLOT_HIGHLIGHT_FRONT_SPRITE)) {
                instance.fillGradient(x + 4, y + 4, x + 20, y + 20, -2130706433, -2130706433);
            }
        } else {
            original.call(instance, pipeline, location, x, y, width, height);
        }
    }
}
