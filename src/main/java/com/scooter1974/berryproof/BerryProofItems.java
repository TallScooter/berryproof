package com.scooter1974.berryproof;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BerryProofItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(BerryProofMod.MODID);

    // Advancement Icon
    public static final DeferredHolder<Item, Item> BERRYPROOF_BUSH_ICON = ITEMS.register(
        "berryproof_bush_icon",
        () -> new Item(new Item.Properties())
    );

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}
