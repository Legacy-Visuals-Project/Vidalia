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

package org.visuals.legacy.animatium.mixins.v1.rendering.items;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.player.FirstPersonHandsAndItems;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.visuals.legacy.animatium.Animatium;
import org.visuals.legacy.animatium.config.AnimatiumConfig;
import org.visuals.legacy.animatium.util.enums.EquipAnimationVersionSetting;

import java.util.function.Predicate;

@Mixin(FirstPersonHandsAndItems.class)
public abstract class MixinFirstPersonHandsAndItems_EquipAnimationChecks {
    @Shadow
    private float mainHandHeight;

    @Shadow
    private ItemStack mainHandItem;

    @Unique
    private int animatium$selectedSlot = -1;

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isHandsBusy()Z"))
    private boolean animatium$heldItemVisibilityInBoat(final LocalPlayer instance, final Operation<Boolean> original) {
        return (!Animatium.isEnabled() || !AnimatiumConfig.instance().items.heldItemVisibilityInBoat) && original.call(instance);
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getItemSwapScale(F)F"))
    private float animatium$legacySwingAnimation(final LocalPlayer instance, final float delta, final Operation<Float> original) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().extras.legacySwingAnimation) {
            return 1.0F;
        } else {
            return original.call(instance, delta);
        }
    }

    @Inject(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/client/player/FirstPersonHandsAndItems;mainHandItem:Lnet/minecraft/world/item/ItemStack;", ordinal = 1, opcode = Opcodes.PUTFIELD))
    private void animatium$cacheSlot(final LocalPlayer player, final CallbackInfo ci) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().items.equipAnimationVersion == EquipAnimationVersionSetting.V1_7 && this.mainHandHeight < 0.1F) {
            this.animatium$selectedSlot = player.getInventory().getSelectedSlot();
        }
    }

    @Definition(id = "mainHandItem", field = "Lnet/minecraft/client/player/FirstPersonHandsAndItems;mainHandItem:Lnet/minecraft/world/item/ItemStack;")
    @Definition(id = "nextMainHand", local = @Local(type = ItemStack.class, name = "nextMainHand"))
    @Expression("this.mainHandItem != nextMainHand")
    @ModifyExpressionValue(method = "tick", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean animatium$legacyEquipLogic(final boolean original, @Local(name = "player", argsOnly = true) final LocalPlayer player, @Local(name = "nextMainHand") final ItemStack nextMainHand) {
        final EquipAnimationVersionSetting equipAnimation = AnimatiumConfig.instance().items.equipAnimationVersion;
        if (Animatium.isEnabled() && equipAnimation != EquipAnimationVersionSetting.VANILLA) {
            final boolean hasItem = this.mainHandItem != ItemStack.EMPTY;
            final boolean hasNewItem = nextMainHand != ItemStack.EMPTY;
            if (hasItem && hasNewItem) {
                return !this.animatium$isEqualForHoldAnimation(equipAnimation, this.mainHandItem, nextMainHand, player);
            } else {
                return hasItem || hasNewItem;
            }
        } else {
            return original;
        }
    }

    @WrapOperation(method = "shouldInstantlyReplaceVisibleItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;matchesIgnoringComponents(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;Ljava/util/function/Predicate;)Z"))
    private boolean animatium$disableVanillaEquipCheck(final ItemStack left, final ItemStack right, final Predicate<DataComponentType<?>> ignoredPredicate, final Operation<Boolean> original) {
        if (Animatium.isEnabled() && AnimatiumConfig.instance().items.equipAnimationVersion != EquipAnimationVersionSetting.VANILLA) {
            return false;
        } else {
            return original.call(left, right, ignoredPredicate);
        }
    }

    @Unique
    private boolean animatium$isEqualForHoldAnimation(final EquipAnimationVersionSetting equipAnimation, final ItemStack held, final ItemStack selected, final LocalPlayer player) {
        if (equipAnimation == EquipAnimationVersionSetting.V1_7) {
            if (held == selected) {
                return this.animatium$selectedSlot == player.getInventory().getSelectedSlot();
            } else if (ItemStack.isSameItemSameComponents(held, selected)) {
                this.mainHandItem = selected;
                return true;
            } else {
                return false;
            }
        } else {
            return ItemStack.isSameItemSameComponents(held, selected) && held.count() == selected.count();
        }
    }
}
