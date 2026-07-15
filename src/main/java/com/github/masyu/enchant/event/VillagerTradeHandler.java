package com.github.masyu.enchant.event;

import com.github.masyu.enchant.regi.ModConfig;
import com.github.masyu.enchant.regi.ModEnchantment;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.trading.MerchantOffer;

import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber
public class VillagerTradeHandler {

    @SubscribeEvent
    public static void addTrades(VillagerTradesEvent event) {

        // 司書
        if (event.getType() == VillagerProfession.LIBRARIAN) {

            // レベル5交易
            List<VillagerTrades.ItemListing> trades =
                    event.getTrades().get(ModConfig.UNBREAKABLE_VILLAGER_TRADE_LEVEL.get());

            trades.add((trader, random) -> new MerchantOffer(

                    // 必要エメラルド
                    new ItemStack(Items.EMERALD, 32),

                    // 本
                    new ItemStack(Items.BOOK),

                    // 売るアイテム
                    EnchantedBookItem.createForEnchantment(
                            new EnchantmentInstance(
                                    ModEnchantment.UNBREAKABLE.get(),
                                    1
                            )
                    ),

                    // 最大取引回数
                    4,

                    // 経験値
                    30,

                    // 価格倍率
                    0.2F
            ));
        }
    }
}