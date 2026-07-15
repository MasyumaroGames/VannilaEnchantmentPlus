package com.github.masyu.enchant;

import com.github.masyu.enchant.regi.ModConfig;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@Mod(Enchant.MODID)
public class Enchant {
    // MODID
    public static final String MODID = "enchant";

    // Config
    public Enchant(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(net.neoforged.fml.config.ModConfig.Type.COMMON, ModConfig.COMMON);
    }
    // クリエイティブタブへの追加イベントを登録
    private void addCreativeContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath("enchant", "unbreakable");
            ResourceKey<Enchantment> key = ResourceKey.create(Registries.ENCHANTMENT, id);

            event.getParameters().holders().lookup(Registries.ENCHANTMENT).ifPresent(registry -> {
                registry.get(key).ifPresent(enchantmentHolder -> {
                    ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);
                    ItemEnchantments.Mutable mutable = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
                    mutable.set(enchantmentHolder, 1);
                    book.set(DataComponents.STORED_ENCHANTMENTS, mutable.toImmutable());

                    // try-catch で重複エラーを回避
                    try {
                        event.accept(book);
                    } catch (IllegalArgumentException e) {
                        // すでに追加されている場合は何もしない
                        System.out.println("Unbreakable book was already added to this tab.");
                    }
                });
            });
        }
    }
}
