package com.github.masyu.enchant.mixin;

import com.github.masyu.enchant.regi.ModConfig;
import com.github.masyu.enchant.regi.ModEnchantment;
import net.minecraft.inventory.container.RepairContainer;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.item.ItemStack;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(RepairContainer.class)
public class RepairContainerMixin {

    @Shadow @Final
    private IntReferenceHolder cost;

    // createResult -> 本番環境の名前(m_38895_)に自動変換されるよう指定
    @ModifyConstant(method = "createResult", constant = @Constant(intValue = 40))
    private int removeTooExpensiveLimit(int original) {
        return 1000000;
    }

    @Redirect(
            method = "createResult",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/Enchantment;getMaxLevel()I")
    )
    private int redirectMaxLevel(Enchantment instance) {
        System.out.println("Redirect: " + instance);
        if (instance == Enchantments.MENDING || instance == Enchantments.SILK_TOUCH || instance == Enchantments.AQUA_AFFINITY || instance == Enchantments.BINDING_CURSE || instance == Enchantments.INFINITY_ARROWS || instance == Enchantments.VANISHING_CURSE ||
                (ModEnchantment.UNBREAKABLE.isPresent() && instance == ModEnchantment.UNBREAKABLE.get())) {
            return 1;
        }
        if (instance == Enchantments.KNOCKBACK || instance == Enchantments.PUNCH_ARROWS) {
            //return 5;
            return ModConfig.SPECIAL_ENCHANT_MAX_LEVEL.get();
        }
        //return 10;
        return ModConfig.ENCHANT_MAX_LEVEL.get();
    }

    @Redirect(
            method = "createResult",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/Enchantment;isCompatibleWith(Lnet/minecraft/enchantment/Enchantment;)Z")
    )
    private boolean allowAllConflicts(Enchantment instance, Enchantment other) {
        return true;
    }

//    @Inject(method = "createResult", at = @At("HEAD"))
//    private void test(CallbackInfo ci) {
//        System.out.println("RepairContainerMixin Applied");
//    }

    @Inject(method = "createResult", at = @At("TAIL"))
    private void finalizeAnvilResult(CallbackInfo ci) {
        RepairContainer menu = (RepairContainer)(Object)this;
        // getSlot(0)や(2)も本番では名前が変わることがあるため注意
        ItemStack left = menu.getSlot(0).getItem();
        ItemStack result = menu.getSlot(2).getItem();

        if (result.isEmpty()) return;

        Map<Enchantment, Integer> leftEnchants = EnchantmentHelper.getEnchantments(left);
        Map<Enchantment, Integer> resultEnchants = EnchantmentHelper.getEnchantments(result);

        boolean isEnchantUpdated = !leftEnchants.equals(resultEnchants);
        boolean isRepaired = result.getDamageValue() < left.getDamageValue();
        boolean isRenamed = result.hasCustomHoverName() && !result.getHoverName().equals(left.getHoverName());

        if (!isEnchantUpdated && !isRepaired && !isRenamed) {
            menu.getSlot(2).set(ItemStack.EMPTY);
            this.cost.set(0);
            return;
        }

        if (this.cost.get() > 10) {
            this.cost.set(10);
        }
        result.setRepairCost(0);
    }
}