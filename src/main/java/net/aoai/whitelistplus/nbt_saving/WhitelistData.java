package net.aoai.whitelistplus.nbt_saving;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class WhitelistData extends SavedData {
    private final List<String> whitelistList = new ArrayList<>();

    private static final List<Consumer<WhitelistData>> listeners = new ArrayList<>();

    public WhitelistData() {

    }

    public static void registerListener(Consumer<WhitelistData> listener) {
        listeners.add(listener);
    }

    private void notifyListeners() {
        for (Consumer<WhitelistData> listener:listeners) {
            listener.accept(this);
        }
    }

    public static WhitelistData load(CompoundTag nbt) {
        WhitelistData data = new WhitelistData();

        if (nbt.contains("Whitelist", StringTag.TAG_LIST)) {
            ListTag listTag = nbt.getList("Whitelist", StringTag.TAG_STRING);
            for (int i = 0; i < listTag.size(); i++) {
                data.whitelistList.add(listTag.getString(i));
            }
        }

        return data;
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        ListTag listTag = new ListTag();

        for (String str: whitelistList) {
            listTag.add(StringTag.valueOf(str));
        }

        nbt.put("Whitelist", listTag);
        return nbt;
    }

    public List<String> getWhitelist() {
        return this.whitelistList;
    }

    public void addStringData(String value) {
        this.whitelistList.add(value);
        this.setDirty();
    }

    public void removeStringData(String value) {
        if (this.whitelistList.contains(value)) {
            this.whitelistList.remove(value);
            this.setDirty();
        }
    }
}
