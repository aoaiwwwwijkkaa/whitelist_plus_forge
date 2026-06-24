package net.aoai.whitelistplus.whitelist;

import net.aoai.whitelistplus.nbt_saving.WhitelistData;
import net.aoai.whitelistplus.world.WhitelistDataHelper;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.OutgoingChatMessage;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Objects;

@Mod.EventBusSubscriber
public class OnPlayerJoined {
    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            WhitelistData data = WhitelistDataHelper.get(player.serverLevel());

            List<String> list = data.getWhitelist();

            if (!list.isEmpty()) {
                if (!list.contains(player.getUUID().toString()) && !player.hasPermissions(Commands.LEVEL_MODERATORS)) {
                    player.connection.disconnect(Component.literal("Whitelist+\nYou are not whitelisted!"));
                } else {
                    player.sendSystemMessage(Component.literal("[Whitelist+]: You are whitelisted!"));
                }
            }
        }
    }
}
