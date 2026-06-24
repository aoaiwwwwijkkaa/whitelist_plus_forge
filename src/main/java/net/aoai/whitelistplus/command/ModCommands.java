package net.aoai.whitelistplus.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.aoai.whitelistplus.Config;
import net.aoai.whitelistplus.nbt_saving.WhitelistData;
import net.aoai.whitelistplus.world.WhitelistDataHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.UuidArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Mod.EventBusSubscriber
public class ModCommands {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> commandDispatcher = event.getDispatcher();

        commandDispatcher.register(
                Commands.literal("whitelistplus")
                        .requires(commandSourceStack -> commandSourceStack.hasPermission(Config.levelRequired))
                        .then(Commands.literal("add")
                            .then(Commands.argument("uuid", UuidArgument.uuid())
                                .executes(context -> {
                                    UUID uuid = UuidArgument.getUuid(context, "uuid");

                                    ServerLevel level = context.getSource().getLevel();

                                    WhitelistData whitelistData = WhitelistDataHelper.get(level);

                                    List<String> list = whitelistData.getWhitelist();

                                    if (list.contains(uuid.toString())) {
                                        context.getSource().sendFailure(Component.nullToEmpty("UUID already whitelisted"));

                                        return 0;
                                    }

                                    whitelistData.addStringData(uuid.toString());

                                    context.getSource().sendSuccess(() -> Component.literal("Whitelisted UUID: " + uuid.toString()), true);

                                    return 1;
                                })
                            )
                        )

                        .then(Commands.literal("remove")
                                .then(Commands.argument("uuid", UuidArgument.uuid())
                                        .executes(context -> {
                                            UUID uuid = UuidArgument.getUuid(context, "uuid");

                                            ServerLevel level = context.getSource().getLevel();

                                            WhitelistData whitelistData = WhitelistDataHelper.get(level);

                                            List<String> list = whitelistData.getWhitelist();

                                            if (list.contains(uuid.toString())) {
                                                context.getSource().sendSuccess(() -> Component.literal("Un-whitelisted UUID: " + uuid.toString()), true);

                                                return 1;
                                            }

                                            whitelistData.addStringData(uuid.toString());

                                            context.getSource().sendFailure(Component.nullToEmpty("UUID not whitelisted"));

                                            return 0;
                                        })
                                )
                        )

                        .then(Commands.literal("getlist")
                                .executes(context -> {
                                    ServerLevel level = context.getSource().getLevel();

                                    WhitelistData whitelistData = WhitelistDataHelper.get(level);

                                    List<String> list = whitelistData.getWhitelist();

                                    context.getSource().sendSuccess(() -> Component.literal("Whitelist: "), false);

                                    for (int i = 0; i < list.toArray().length; i++) {
                                        int finalI = i;
                                        context.getSource().sendSuccess(() -> Component.literal("   " + list.get(finalI)), false);
                                    }

                                    return 1;
                                })
                        )

                        .then(Commands.literal("clear")
                                .requires(commandSourceStack -> commandSourceStack.hasPermission(Commands.LEVEL_OWNERS))
                            .executes(context -> {
                                ServerLevel level = context.getSource().getLevel();

                                WhitelistData whitelistData = WhitelistDataHelper.get(level);

                                List<String> list = whitelistData.getWhitelist();

                                for (int i = 0; i < list.toArray().length; i++) {
                                    whitelistData.removeStringData(list.get(i));
                                }

                                context.getSource().sendSuccess(() -> Component.literal("Cleared whitelist"), true);

                                return 1;
                            })
                        )
        );

        commandDispatcher.register(
                Commands.literal("getuuid")
                        .requires(commandSourceStack -> commandSourceStack.hasPermission(Commands.LEVEL_ALL))
                        .executes(context -> {
                            UUID uuid = Objects.requireNonNull(context.getSource().getPlayer()).getUUID();

                            context.getSource().sendSuccess(() -> Component.literal("Your UUID: " + uuid.toString()), false);

                            return 1;
                        })
        );
    }
}
