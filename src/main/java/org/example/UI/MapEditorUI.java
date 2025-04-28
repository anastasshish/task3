package org.example.UI;

import org.example.Enum.CellType;
import java.util.Scanner;

public class MapEditorUI {

    private final GameMap map;
    private final Scanner scanner;

    public MapEditorUI(GameMap map) {
        this.map = map;
        this.scanner = new Scanner(System.in);
    }

    public void runEditor() {
        while (true) {
            map.printMap(null, null);

            System.out.println("\n=== Редактор карт ===");
            System.out.println("1) Установить клетку Дорога (ROAD)");
            System.out.println("2) Установить клетку Нейтральная (NEUTRAL)");
            System.out.println("3) Установить клетку Зона игрока (PLAYER_ZONE)");
            System.out.println("4) Установить клетку Зона компьютера (COMPUTER_ZONE)");
            System.out.println("5) Установить клетку Замок игрока (PLAYER_CASTLE)");
            System.out.println("6) Установить клетку Замок компьютера (COMPUTER_CASTLE)");
            System.out.println("7) Установить клетку Последствия сражения (BATTLE)");
            System.out.println("8) Выход из редактора");

            System.out.print("Выберите пункт (1-8): ");
            String choice = scanner.nextLine().trim();

            if (choice.equals("8")) {
                System.out.println("Выходим из редактора карт");
                break;
            }

            int menuOption;
            try {
                menuOption = Integer.parseInt(choice);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите целое число от 1 до 8.");
                continue;
            }
            if (menuOption < 1 || menuOption > 8) {
                System.out.println("Нет такого пункта меню: " + menuOption);
                continue;
            }

            System.out.print("Введите X: ");
            String sx = scanner.nextLine().trim();
            System.out.print("Введите Y: ");
            String sy = scanner.nextLine().trim();

            int x, y;
            try {
                x = Integer.parseInt(sx);
                y = Integer.parseInt(sy);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: координаты должны быть числами!");
                continue;
            }

            if (x < 0 || x >= GameMap.WIDTH || y < 0 || y >= GameMap.HEIGHT) {
                System.out.println("Координаты (" + x + "," + y + ") вне карты!");
                continue;
            }

            switch (choice) {
                case "1":
                    map.setCellType(x, y, CellType.ROAD);
                    System.out.println("Клетка (" + x + "," + y + ") установлена в ROAD.");
                    break;
                case "2":
                    map.setCellType(x, y, CellType.NEUTRAL);
                    System.out.println("Клетка (" + x + "," + y + ") установлена в NEUTRAL.");
                    break;
                case "3":
                    map.setCellType(x, y, CellType.PLAYER_ZONE);
                    System.out.println("Клетка (" + x + "," + y + ") установлена в PLAYER_ZONE.");
                    break;
                case "4":
                    map.setCellType(x, y, CellType.COMPUTER_ZONE);
                    System.out.println("Клетка (" + x + "," + y + ") установлена в COMPUTER_ZONE.");
                    break;
                case "5":
                    map.setCellType(x, y, CellType.PLAYER_CASTLE);
                    System.out.println("Клетка (" + x + "," + y + ") установлена в PLAYER_CASTLE.");
                    break;
                case "6":
                    map.setCellType(x, y, CellType.COMPUTER_CASTLE);
                    System.out.println("Клетка (" + x + "," + y + ") установлена в COMPUTER_CASTLE.");
                    break;
                case "7":
                    map.setCellType(x, y, CellType.BATTLE);
                    System.out.println("Клетка (" + x + "," + y + ") установлена в BATTLE.");
                    break;
                default:
                    System.out.println("Неизвестная команда: " + choice);
                    break;
            }
        }
    }
}
