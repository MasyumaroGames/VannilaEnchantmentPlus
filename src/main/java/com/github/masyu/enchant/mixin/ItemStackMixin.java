package com.github.masyu.enchant.mixin; // あなたのパッケージ名

import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    // ★耐久値を変更する基本メソッド「setDamageValue」の「手前(HEAD)」に割り込みます
    @Inject(method = "setDamageValue(I)V", at = @At("HEAD"), cancellable = true)
    private void onSetDamageValue(int damage, CallbackInfo ci) {
        ItemStack stack = (ItemStack)(Object)this;

        if (stack.isEmpty()) return;

        // 不可壊（unbreakable）が付いているかチェック
        boolean hasUnbreakable = stack.getEnchantments().keySet().stream()
                .anyMatch(holder -> holder.unwrapKey().isPresent() &&
                        holder.unwrapKey().get().location().toString().equals("enchant:unbreakable"));

        if (hasUnbreakable) {
            // ★もし現在の耐久値（摩耗度）より大きい値（＝傷つく方向）に設定されそうになったら、処理を強制キャンセル！
            // これにより、バニラや他のMODによる耐久減少の命令がすべて無効化されます。
            if (damage > stack.getDamageValue()) {
                ci.cancel();
            }
        }
    }
}