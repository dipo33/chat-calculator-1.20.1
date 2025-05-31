package com.dipo33.chatcalc;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = ChatCalcMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.ConfigValue<String> GREETING = BUILDER
            .comment("How shall I greet?")
            .define("greeting", "Hello World");

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static String greeting;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        greeting = GREETING.get();
    }
}
