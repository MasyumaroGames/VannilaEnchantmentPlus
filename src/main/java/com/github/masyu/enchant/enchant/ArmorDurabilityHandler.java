package com.github.masyu.enchant.enchant; // あなたのパッケージ名

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

@EventBusSubscriber(modid = "enchant") // あなたのModID
public class ArmorDurabilityHandler {

    @SubscribeEvent
    public static void onLivingDamagePost(LivingDamageEvent.Post event) {
        // ダメージを受けたエンティティ（プレイヤー等）がいない場合は無視
        if (event.getEntity() == null) return;

        // 全ての防具スロット（頭、胸、脚、足）をループでチェック
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.isArmor()) {
                ItemStack armorStack = event.getEntity().getItemBySlot(slot);

                if (!armorStack.isEmpty()) {
                    // その防具に「不可壊（unbreakable）」が付いているか判定
                    boolean hasUnbreakable = armorStack.getEnchantments().keySet().stream()
                            .anyMatch(holder -> holder.unwrapKey().isPresent() &&
                                    holder.unwrapKey().get().location().toString().equals("enchant:unbreakable"));

                    if (hasUnbreakable) {
                        // ★【核心】バニラに削られたダメージを、その場で即座に0にリセット（完全修復）する！
                        // 1.21.1のItemStackはsetDamageValueで現在の摩耗度を設定できます。
                        // 常に0（無傷）に固定することで、実質的に絶対に壊れない防具になります。
                        armorStack.setDamageValue(0);
                    }
                }
            }
        }
    }
}