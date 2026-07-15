package com.github.masyu.enchant.mixin;

import net.minecraft.core.Holder;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.core.component.DataComponents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilMenu.class)
public class AnvilMenuMixin {

    @Shadow @Final private DataSlot cost;

    @Inject(method = "createResult", at = @At("RETURN"))
    private void finalizeAnvilResult(CallbackInfo ci) {
        AnvilMenu menu = (AnvilMenu)(Object)this;
        ItemStack left = menu.getSlot(0).getItem();
        ItemStack right = menu.getSlot(1).getItem();

        if (left.isEmpty() || right.isEmpty()) return;

        // ↓ 本・道具どちらでも正しく読めるヘルパーに変更
        ItemEnchantments leftEnchants = EnchantmentHelper.getEnchantmentsForCrafting(left);
        ItemEnchantments rightEnchants = EnchantmentHelper.getEnchantmentsForCrafting(right);

        ItemEnchantments.Mutable mutableEnchants = new ItemEnchantments.Mutable(leftEnchants);
        boolean isUpdated = false;
        boolean isImpossible = false;

        for (Holder<Enchantment> enchantHolder : rightEnchants.keySet()) {
            if (enchantHolder == null) continue;

            int leftLevel = leftEnchants.getLevel(enchantHolder);
            int rightLevel = rightEnchants.getLevel(enchantHolder);
            int maxLevel = enchantHolder.value().getMaxLevel();

            System.out.println("[DEBUG] enchant=" + enchantHolder.unwrapKey().orElse(null)
                    + " left=" + leftLevel + " right=" + rightLevel + " max=" + maxLevel);

            if (leftLevel > 0 && leftLevel == rightLevel) {
                if (leftLevel >= maxLevel) {
                    isImpossible = true;
                } else {
                    int nextLevel = leftLevel + 1;
                    mutableEnchants.set(enchantHolder, Math.min(nextLevel, maxLevel));
                    isUpdated = true;
                }
            } else if (rightLevel > leftLevel) {
                mutableEnchants.set(enchantHolder, Math.min(rightLevel, maxLevel));
                isUpdated = true;
            } else if (leftLevel == 0 && rightLevel > 0) {
                mutableEnchants.set(enchantHolder, Math.min(rightLevel, maxLevel));
                isUpdated = true;
            }
        }

        if (isImpossible) {
            menu.getSlot(2).set(ItemStack.EMPTY);
            this.cost.set(0);
            return;
        }

        if (!isUpdated) return;

        ItemStack current = menu.getSlot(2).getItem();
        boolean resultIsBook = left.is(Items.ENCHANTED_BOOK) || left.is(Items.BOOK);

        ItemStack newResult = current.isEmpty()
                ? (resultIsBook ? new ItemStack(Items.ENCHANTED_BOOK) : left.copy())
                : current;

        // ↓ 本の場合は STORED_ENCHANTMENTS、道具の場合は ENCHANTMENTS に書き込む
        EnchantmentHelper.setEnchantments(newResult, mutableEnchants.toImmutable());

        newResult.set(DataComponents.REPAIR_COST, 0);

        menu.getSlot(2).set(newResult);

        if (this.cost.get() <= 0 || this.cost.get() >= 40) {
            this.cost.set(30);
        }
    }
}