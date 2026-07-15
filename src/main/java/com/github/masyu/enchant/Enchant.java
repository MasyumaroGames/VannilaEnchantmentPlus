package com.github.masyu.enchant;

import com.github.masyu.enchant.regi.ModConfig;
import com.github.masyu.enchant.regi.ModEnchantment;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("enchant")
public class Enchant {

    public static final String MODID = "enchant";

    public Enchant()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModEnchantment.ENCHANTMENTS.register(bus);
        ModLoadingContext.get().registerConfig(
                net.minecraftforge.fml.config.ModConfig.Type.COMMON,
                ModConfig.COMMON);

    }

}
