package org.example.Records;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.*;

public class Scoreboard {
    private static final String SCORE_FILE = "scores.dat";
    public static void addRecord(Record newRec) {
        List<Record> records = loadRecords();
        Optional<Record> existingOpt = records.stream()
                .filter(r -> Objects.equals(r.getPlayerName(), newRec.getPlayerName()) &&
                                Objects.equals(r.getMapName(), newRec.getMapName()))
                .findFirst();

        if (existingOpt.isPresent()) {
            Record oldRec = existingOpt.get();
            int totalScore = oldRec.getScore() + newRec.getScore();
            Record updatedRec = new Record(oldRec.getPlayerName(), oldRec.getMapName(), totalScore);
            records.remove(oldRec);
            records.add(updatedRec);
        } else {
            records.add(newRec);
        }

        records.sort(Comparator.comparingInt(Record::getScore).reversed());

        while (records.size() > 5) {
            records.remove(records.size() - 1);
        }

        saveRecords(records);
    }

    public static List<Record> loadRecords() {
        File f = new File(SCORE_FILE);
        if (!f.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            List<Record> records = (List<Record>) ois.readObject();
            return (records != null) ? records : new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void saveRecords(List<Record> records) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SCORE_FILE))) {
            oos.writeObject(records);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printTopRecords() {
        List<Record> records = loadRecords();
        if (records.isEmpty()) {
            System.out.println("Рекордов пока нет!");
            return;
        }
        System.out.println("\n=== Топ рекордов (до 5) ===");
        for (int i = 0; i < records.size(); i++) {
            Record r = records.get(i);
            System.out.printf("%d) Игрок: %s | Карта: %s | Очки: %d | Время: %d%n",
                    (i + 1),
                    r.getPlayerName(),
                    r.getMapName(),
                    r.getScore(),
                    r.getTimestamp()
            );
        }
    }
}
