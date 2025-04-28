package org.example.UI;

import org.example.GameState;
import java.io.*;

public class FileManager {

    public static GameMap loadMap(String filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (GameMap) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void saveMap(GameMap map, String filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(map);
            System.out.println("Карта сохранена в " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveGame(GameState state, String filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(state);
            System.out.println("Игра успешно сохранена в: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static GameState loadGameState(String filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            GameState state = (GameState) ois.readObject();
            System.out.println("Игра загружена из " + filePath);
            return state;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
