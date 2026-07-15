package com.github.masyu.enchant.enchantment;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;

public class Unbreakable extends Enchantment {

    public Unbreakable() {
        super(
                Rarity.VERY_RARE,
                EnchantmentType.BREAKABLE,
                EquipmentSlotType.values()
        );
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean isTradeable() {
        return true;
    }

    @Override
    public boolean isDiscoverable() {
        return true;
    }

    @Override
    public int getMinCost(int level) {
        return 30;
    }

    @Override
    public int getMaxCost(int level) {
        return 30;
    }
}