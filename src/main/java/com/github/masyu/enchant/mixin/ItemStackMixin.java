package com.github.masyu.enchant.mixin;

import com.github.masyu.enchant.regi.ModEnchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public class ItemStackMixin {

//    @Inject(method = "hurtAndBreak", at = @At("HEAD"))
//    private void test(int amount, LivingEntity entity, Consumer<?> onBroken, CallbackInfo ci) {
//        System.out.println("ItemStackMixin Applied");
//    }

    // IDEでのコンパイルエラーを防ぐため、開発環境での名前を使用します
    @Inject(
            method = "hurtAndBreak",
            at = @At("HEAD"),
            cancellable = true
    )
    private void preventBreaking(int amount, LivingEntity entity, Consumer<?> onBroken, CallbackInfo ci) {
        ItemStack stack = (ItemStack)(Object)this;

        if (ModEnchantment.UNBREAKABLE.isPresent() &&
                EnchantmentHelper.getItemEnchantmentLevel(ModEnchantment.UNBREAKABLE.get(), stack) > 0) {
            ci.cancel();
        }
    }
}