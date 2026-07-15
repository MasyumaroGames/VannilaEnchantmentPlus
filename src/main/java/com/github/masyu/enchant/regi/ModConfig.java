package com.github.masyu.enchant.regi;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ModConfig {

    public static final ModConfigSpec COMMON;

    public static final ModConfigSpec.IntValue ENCHANT_MAX_LEVEL;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.push("Enchantment"); // TOML内の [combat] セクション

        ENCHANT_MAX_LEVEL = builder
                .comment("Enchantment MaxLevel (Maximum levels for \"vanilla\" enchantments (excluding those that do not change when raised above level 1))")
                .defineInRange("enchant_max_level", 10, 1, 255);

        builder.pop();

        COMMON = builder.build();
    }

}
