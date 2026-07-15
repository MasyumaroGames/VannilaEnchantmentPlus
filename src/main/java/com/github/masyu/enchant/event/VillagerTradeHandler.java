package com.github.masyu.enchant.event;

import com.github.masyu.enchant.regi.ModConfig;
import com.github.masyu.enchant.regi.ModEnchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MerchantOffer;
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
            List<VillagerTrades.ITrade> trades =
                    event.getTrades().get(ModConfig.UNBREAKABLE_VILLAGER_TRADE_LEVEL.get());

            trades.add((trader, random) -> new MerchantOffer(

                    // 必要エメラルド
                    new ItemStack(Items.EMERALD, 32),

                    // 本
                    new ItemStack(Items.BOOK),

                    // 売るアイテム
                    EnchantedBookItem.createForEnchantment(
                            new EnchantmentData(
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