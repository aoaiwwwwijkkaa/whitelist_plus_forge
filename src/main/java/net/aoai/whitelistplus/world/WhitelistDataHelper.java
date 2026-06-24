package net.aoai.whitelistplus.world;

import net.aoai.whitelistplus.nbt_saving.WhitelistData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.storage.DimensionDataStorage;

public class WhitelistDataHelper {
    private static final String DATA_NAME = "whitelist_plus_whitelist_data";

    public static WhitelistData get(LevelAccessor level) {
        if (level instanceof ServerLevel serverLevel) {
            DimensionDataStorage storage = serverLevel.getDataStorage();
            return storage.computeIfAbsent(
                    WhitelistData::load,
                    WhitelistData::new,
                    DATA_NAME
            );
        }
        throw new IllegalStateException("Cannot access SavedData from logical client");
    }
}
