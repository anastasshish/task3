package org.example.MiniGameJson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

public class BattleStorage {
    private static final String FILE_PATH = "miniGame.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void saveBattleData(StoredBattleData data) {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static StoredBattleData loadBattleData() {
        File f = new File(FILE_PATH);
        if (!f.exists()) {
            return null;
        }
        try (Reader reader = new FileReader(f)) {
            return gson.fromJson(reader, StoredBattleData.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
