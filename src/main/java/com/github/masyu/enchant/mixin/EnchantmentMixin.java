package com.github.masyu.enchant.mixin;

import com.github.masyu.enchant.regi.ModConfig;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public class EnchantmentMixin {

    @Inject(method = "getMaxLevel", at = @At("RETURN"), cancellable = true)
    private void adjustMaxLevel(CallbackInfoReturnable<Integer> cir) {
        int original = cir.getReturnValue();
        if (original <= 1) {
            return; // 元々レベル1のエンチャントはそのまま維持
        }
        cir.setReturnValue(ModConfig.ENCHANT_MAX_LEVEL.get());
    }
}