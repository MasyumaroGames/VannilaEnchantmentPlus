package com.github.masyu.enchant.regi;

import net.minecraftforge.common.ForgeConfigSpec;

public class ModConfig {

    public static final ForgeConfigSpec COMMON;

    public static final ForgeConfigSpec.IntValue ENCHANT_MAX_LEVEL;
    public static final ForgeConfigSpec.IntValue SPECIAL_ENCHANT_MAX_LEVEL;
    public static final ForgeConfigSpec.IntValue VILLAGER_TRADE_LEVEL;
    public static final ForgeConfigSpec.IntValue UNBREAKABLE_VILLAGER_TRADE_LEVEL;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push("Enchantment"); // TOML内の [combat] セクション

        ENCHANT_MAX_LEVEL = builder
                .comment("Enchantment MaxLevel (Maximum levels for \"vanilla\" enchantments (excluding those that do not change when raised above level 1))")
                .translation("config.enchant.enchantment_max_level")
                .defineInRange("enchant_max_level", 10, 1, 255);

        SPECIAL_ENCHANT_MAX_LEVEL = builder
                .comment("Special Enchantment MaxLevel (Knockback and Punch_Arrow enchantment levels)")
                .translation("config.enchant.special_enchantment_max_level")
                .defineInRange("special_enchant_max_level", 5, 1, 255);

        VILLAGER_TRADE_LEVEL = builder
                .comment("Villager Trade Level (At what level do Librarian villagers start selling high-level vanilla enchantments)")
                .translation("config.enchant.villager_trade_level")
                .defineInRange("villager_trade_level", 3, 1, 5);

        UNBREAKABLE_VILLAGER_TRADE_LEVEL = builder
                .comment("Unbreakable Villager Trade Level (At what level do Librarian villagers start selling the Unbreaking enchantment)")
                .translation("config.enchant.unbreakable_villager_trade_level")
                .defineInRange("unbreakable_villager_trade_level", 3, 1, 5);

        builder.pop();

        COMMON = builder.build();
    }

}
