package com.github.masyu.enchant;


import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("enchant")
public class Enchant {

    public static final String MODID = "enchant";

    public Enchant(){
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
    }

}