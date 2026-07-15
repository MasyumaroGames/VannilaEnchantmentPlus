package com.github.masyu.enchant.mixin;

import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    // ★【変更】耐久値を設定する基本メソッド「setDamageValue」の「手前(HEAD)」に割り込みます！
    @Inject(method = "setDamageValue(I)V", at = @At("HEAD"), cancellable = true)
    private void onSetDamageValue(int damage, CallbackInfo ci) {
        ItemStack stack = (ItemStack)(Object)this;

        if (stack.isEmpty()) return;

        // 不可壊（unbreakable）が付いているかチェック
        boolean hasUnbreakable = stack.getEnchantments().keySet().stream()
                .anyMatch(holder -> holder.unwrapKey().isPresent() &&
                        holder.unwrapKey().get().identifier().toString().equals("enchant:unbreakable"));

        if (hasUnbreakable) {
            // ★【核心】もし耐久値を増やそう（傷つけよう）とした場合、その処理を完全にキャンセルして無視する！
            if (damage > 0) {
                ci.cancel();
            }
        }
    }
}