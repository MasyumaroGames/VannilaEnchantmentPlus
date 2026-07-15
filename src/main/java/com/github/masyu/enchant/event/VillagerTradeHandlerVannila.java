package com.github.masyu.enchant.event;

import com.github.masyu.enchant.regi.ModConfig;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MerchantOffer;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber
public class VillagerTradeHandlerVannila {

    @SubscribeEvent
    public static void addTrades(VillagerTradesEvent event) {

        if (event.getType() == VillagerProfession.LIBRARIAN) {

            List<VillagerTrades.ITrade> trades =
                    event.getTrades().get(ModConfig.VILLAGER_TRADE_LEVEL.get());

            trades.add((trader, random) -> {

                List<Enchantment> enchantments =
                        new ArrayList<>(ForgeRegistries.ENCHANTMENTS.getValues());

                Enchantment enchantment =
                        enchantments.get(random.nextInt(enchantments.size()));

                int level = 6 + random.nextInt(5);

                // レベル依存価格
                int emeralds = level * 6;

                return new MerchantOffer(

                        new ItemStack(Items.EMERALD, emeralds),

                        new ItemStack(Items.BOOK),

                        EnchantedBookItem.createForEnchantment(
                                new EnchantmentData(enchantment, level)
                        ),

                        4,
                        30,
                        0.0F
                );
            });
        }
    }
}