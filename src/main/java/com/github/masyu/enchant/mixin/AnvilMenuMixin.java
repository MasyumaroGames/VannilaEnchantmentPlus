package com.github.masyu.enchant.mixin; // あなたのパッケージ名

import net.minecraft.core.Holder;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilMenu.class)
public class AnvilMenuMixin {

    @Shadow @Final private DataSlot cost;

    // 「コストが高すぎます！」（40レベル制限）を解除
    @ModifyConstant(method = "createResult", constant = @Constant(intValue = 40))
    private int removeTooExpensiveLimit(int original) {
        return 1000000;
    }

    @Inject(method = "createResult", at = @At("TAIL"))
    private void finalizeAnvilResult(CallbackInfo ci) {
        AnvilMenu menu = (AnvilMenu)(Object)this;
        ItemStack left = menu.getSlot(0).getItem();
        ItemStack right = menu.getSlot(1).getItem();
        ItemStack result = menu.getSlot(2).getItem();

        if (left.isEmpty() || right.isEmpty()) return;

        ItemEnchantments leftEnchants = left.getEnchantments();
        ItemEnchantments rightEnchants = right.getEnchantments();

        if (rightEnchants.isEmpty()) return;

        // 新しいエンチャント計算用のコンテナ
        ItemEnchantments.Mutable mutableEnchants = new ItemEnchantments.Mutable(leftEnchants);
        boolean isUpdated = false;

        for (Holder<Enchantment> enchantHolder : rightEnchants.keySet()) {
            if (enchantHolder == null) continue;

            int leftLevel = leftEnchants.getLevel(enchantHolder);
            int rightLevel = rightEnchants.getLevel(enchantHolder);

            // 4+4=5 にする処理
            if (leftLevel > 0 && leftLevel == rightLevel) {
                int nextLevel = leftLevel + 1;
                if (nextLevel <= 10) { // 最大10まで
                    mutableEnchants.set(enchantHolder, nextLevel);
                    isUpdated = true;
                }
            }
            // 右側のレベルの方が高い場合は高い方を採用
            else if (rightLevel > leftLevel) {
                mutableEnchants.set(enchantHolder, rightLevel);
                isUpdated = true;
            }
            // 左側にないエンチャントを右側から引き継ぐ
            else if (leftLevel == 0) {
                mutableEnchants.set(enchantHolder, rightLevel);
                isUpdated = true;
            }
        }

        if (isUpdated) {
            // バニラが上限制限で結果を空（×）にしていた場合は、左のアイテムをコピー
            ItemStack newResult = result.isEmpty() ? left.copy() : result;

            // 本同士の合成なら完成品を本にする
            if (left.is(Items.BOOK) || left.is(Items.ENCHANTED_BOOK)) {
                newResult = new ItemStack(Items.ENCHANTED_BOOK);
            }

            // 限界突破したレベルを流し込む
            newResult.set(net.minecraft.core.component.DataComponents.ENCHANTMENTS, mutableEnchants.toImmutable());

            // 金床の完成スロットに強制セット
            menu.getSlot(2).set(newResult);

            // コストが0なら最低コスト1を設定
            if (this.cost.get() <= 0) {
                this.cost.set(1);
            }
        }

        // 最終安全処理（コスト上限を30に固定、ペナルティリセット）
        result = menu.getSlot(2).getItem();
        if (result.isEmpty()) return;

        result.set(net.minecraft.core.component.DataComponents.REPAIR_COST, 0);
        if (this.cost.get() > 30) {
            this.cost.set(30);
        }
    }
}