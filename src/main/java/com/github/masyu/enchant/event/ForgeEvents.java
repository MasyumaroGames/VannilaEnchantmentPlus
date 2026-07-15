package com.github.masyu.enchant.event;

import com.github.masyu.enchant.regi.ModEnchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ForgeEvents {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {

        ItemStack stack = event.player.getMainHandItem();

        if (EnchantmentHelper.getItemEnchantmentLevel(
                ModEnchantment.UNBREAKABLE.get(),
                stack
        ) > 0) {

            if (stack.isDamaged()) {
                stack.setDamageValue(0);
            }
        }
    }

}
