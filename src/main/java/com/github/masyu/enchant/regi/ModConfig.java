package com.github.masyu.enchant.regi;

import net.minecraftforge.common.ForgeConfigSpec;

public class ModConfig {

    public static final ForgeConfigSpec COMMON;

    public static final ForgeConfigSpec.IntValue ENCHANT_MAX_LEVEL;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push("Enchantment"); // TOML内の [combat] セクション

        ENCHANT_MAX_LEVEL = builder
                .comment("Enchantment MaxLevel (Maximum levels for \"vanilla\" enchantments (excluding those that do not change when raised above level 1))")
                .translation("config.enchant.enchantment_max_level")
                .defineInRange("enchant_max_level", 10, 1, 255);


        builder.pop();

        COMMON = builder.build();
    }

}
