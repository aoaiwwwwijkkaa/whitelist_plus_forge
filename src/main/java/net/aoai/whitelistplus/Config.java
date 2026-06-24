package net.aoai.whitelistplus;

import net.minecraft.commands.Commands;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = WhitelistPlus.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.IntValue LEVEL_REQUIRED = BUILDER
            .comment("Permission Level required for Whitelist+ access")
            .defineInRange(WhitelistPlus.MODID + "_" + "level_required", Commands.LEVEL_ADMINS, Commands.LEVEL_MODERATORS, Commands.LEVEL_OWNERS);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static int levelRequired;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        levelRequired = LEVEL_REQUIRED.get();
    }
}
