package com.github.masyu.enchant.event;

import com.github.masyu.enchant.regi.ModConfig;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.trading.MerchantOffer;

import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber
public class VillagerTradeHandlerVannila {

    @SubscribeEvent
    public static void addTrades(VillagerTradesEvent event) {

        if (event.getType() == VillagerProfession.LIBRARIAN) {

            List<VillagerTrades.ItemListing> trades =
                    event.getTrades().get(ModConfig.VILLAGER_TRADE_LEVEL.get());

            trades.add((trader, random) -> {

                Enchantment enchantment =
                        BuiltInRegistries.ENCHANTMENT
                                .getRandom(random)
                                .get()
                                .value();

                int level = 6 + random.nextInt(5);

                // レベル依存価格
                int emeralds = level * 6;

                return new MerchantOffer(

                        new ItemStack(Items.EMERALD, emeralds),

                        new ItemStack(Items.BOOK),

                        EnchantedBookItem.createForEnchantment(
                                new EnchantmentInstance(enchantment, level)
                        ),

                        4,
                        30,
                        0.0F
                );
            });
        }
    }
}