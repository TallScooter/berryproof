package com.scooter1974.berryproof;

import com.mojang.logging.LogUtils;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.bus.api.IEventBus;
import org.slf4j.Logger;

@Mod(BerryProofMod.MODID)
public class BerryProofMod {
    public static final String MODID = "berryproof";
    public static final Logger LOGGER = LogUtils.getLogger();

    public BerryProofMod(IEventBus modEventBus) {

    modEventBus.addListener(this::onCommonSetup);

    eu.midnightdust.lib.config.MidnightConfig.init(MODID, BerryProofConfigMidnight.class);

    NeoForge.EVENT_BUS.register(new BerryProofEventHandler());

    LOGGER.info("Berry Proof mod initialized!");
    }

    private void onCommonSetup(final FMLCommonSetupEvent event) {
    }
}