package org.example.UI;

import org.example.*;
import org.example.Enum.BuildingType;
import org.example.Enum.CellType;
import org.example.Enum.UnitType;
import org.example.Heroes.ArmyGroup;
import org.example.Heroes.Computer;
import org.example.Heroes.Player;
import org.example.Heroes.Unit;
import org.example.MiniGameJson.BattleStorage;
import org.example.MiniGameJson.MiniGame;
import org.example.MiniGameJson.StoredBattleData;
import org.example.Move.Move;
import org.example.Records.Record;
import org.example.Records.Scoreboard;
import java.io.File;
import java.util.*;

public class GameUI {

    private final GameDisplay display;
    private final GameInputHandler inputHandler;
    private final String mapName;

    public GameUI(String mapName) {
        this.mapName = Objects.requireNonNull(mapName);
        this.display = new GameDisplay();
        this.inputHandler = new GameInputHandler();
    }

    public GameUI() {
        this("DefaultMap");
    }

    public void runMainMenu() {
        Scanner sc = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("\n=== Главное меню ===");
            System.out.println("1) Новая игра");
            System.out.println("2) Загрузить игру");
            System.out.println("3) Редактор карт");
            System.out.println("4) Посмотреть рекорды");
            System.out.println("5) Выход");
            System.out.print("Выберите пункт меню: ");

            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1":
                    startNewGameUI();
                    break;
                case "2":
                    loadExistingGameUI();
                    break;
                case "3":
                    runMapEditorUI();
                    break;
                case "4":
                    Scoreboard.printTopRecords();
                    break;
                case "5":
                    System.out.println("Выходим из программы...");
                    running = false;
                    break;
                default:
                    System.out.println("Неизвестная команда. Попробуйте снова.");
            }
        }
    }

    private void startNewGameUI() {
        Scanner sc = new Scanner(System.in);
        File mapsDir = new File("maps");
        if (!mapsDir.exists() || !mapsDir.isDirectory()) {
            System.out.println("Папка 'maps/' не найдена. Создаём дефолтную карту...");
            GameMap defaultMap = new GameMap();
            prepareBattleCells(defaultMap);
            runGameOnMap(defaultMap, "DefaultMap");
            return;
        }

        File[] mapFiles = mapsDir.listFiles((dir, name) -> name.endsWith(".dat"));
        if (mapFiles == null || mapFiles.length == 0) {
            System.out.println("Нет сохранённых карт (.dat) в папке maps/.");
            System.out.println("Будет создана дефолтная карта.");
            GameMap defaultMap = new GameMap();
            prepareBattleCells(defaultMap);
            runGameOnMap(defaultMap, "DefaultMap");
            return;
        }

        System.out.println("\n=== Выберите карту для новой игры ===");
        for (int i = 0; i < mapFiles.length; i++) {
            System.out.printf("%d) %s%n", i + 1, mapFiles[i].getName());
        }
        System.out.print("Введите номер карты: ");
        String choice = sc.nextLine().trim();
        int index;
        try {
            index = Integer.parseInt(choice) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Некорректный ввод! Используем дефолтную карту...");
            GameMap defaultMap = new GameMap();
            prepareBattleCells(defaultMap);
            runGameOnMap(defaultMap, "DefaultMap");
            return;
        }
        if (index < 0 || index >= mapFiles.length) {
            System.out.println("Номер вне диапазона. Дефолтная карта...");
            GameMap defaultMap = new GameMap();
            System.out.print("Введите ваше имя: ");
            String playerName = sc.nextLine();
            Player p = new Player(8, 8);
            p.setPlayerName(playerName);
            Computer c = new Computer(1, 1);
            prepareBattleCells(defaultMap);
            runGameLoop(defaultMap, p, c, "DefaultMap");
            return;
        }

        File chosenFile = mapFiles[index];
        String mapName = chosenFile.getName().replace(".dat", "");
        GameMap loadedMap = FileManager.loadMap(chosenFile.getAbsolutePath());
        if (loadedMap == null) {
            System.out.println("Ошибка при загрузке карты. Создаём дефолтную.");
            loadedMap = new GameMap();
        }
        System.out.print("Введите ваше имя: ");
        String playerName = sc.nextLine();
        Player player = new Player(8, 8);
        player.setPlayerName(playerName);
        Computer comp = new Computer(1, 1);
        prepareBattleCells(loadedMap);
        runGameLoop(loadedMap, player, comp, mapName);
    }

    private void runGameOnMap(GameMap map, String mapName) {
        Player defaultP = new Player(8, 8);
        defaultP.setPlayerName("Default");
        Computer defaultC = new Computer(1, 1);
        prepareBattleCells(map);
        runGameLoop(map, defaultP, defaultC, mapName);
    }

    private void runGameLoop(GameMap map, Player player, Computer comp, String mapName) {
        GameUI gameUI = new GameUI(mapName);
        gameUI.runGameLoop(map, player, comp);
    }

    private void loadExistingGameUI() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Укажите путь к сохранённой игре (например, saves/ivan_save.dat): ");
        String path = sc.nextLine().trim();
        GameState loadedState = FileManager.loadGameState(path);
        if (loadedState == null) {
            System.out.println("Ошибка при загрузке!");
            return;
        }
        Player player = loadedState.getPlayer();
        Computer comp = loadedState.getComputer();
        GameMap map = loadedState.getMap();
        if (player == null || comp == null || map == null) {
            System.out.println("Некорректное сохранение, нет нужных данных!");
            return;
        }
        prepareBattleCells(map);
        runGameLoop(map, player, comp, "DefaultMap");
    }

    private void runMapEditorUI() {
        GameMap map = new GameMap(true);
        MapEditorUI editor = new MapEditorUI(map);
        editor.runEditor();

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print("Сохранить карту? (да/нет): ");
            String ans = sc.nextLine().trim().toLowerCase();

            if (ans.equals("да")) {
                System.out.print("Имя файла для сохранения (например, maps/map1.dat): ");
                String mapFile = sc.nextLine().trim();
                FileManager.saveMap(map, mapFile);
                break;
            } else if (ans.equals("нет")) {
                System.out.println("Карта не сохранена.");
                break;
            } else {
                System.out.println("Неизвестная команда, введите \"да\" или \"нет\".");
            }
        }
    }


    public void moveGroup(Player player, GameMap map, UnitType t, String dir) {
        ArmyGroup found = null;
        for (ArmyGroup ag : player.getGroups()) {
            if (ag.getType() == t) {
                found = ag;
                break;
            }
        }
        if (found == null) {
            System.out.println("Нет армии типа " + t);
            return;
        }
        int dx = 0, dy = 0;
        switch (dir) {
            case "W":
                dy = -1;
                break;
            case "S":
                dy = 1;
                break;
            case "A":
                dx = -1;
                break;
            case "D":
                dx = 1;
                break;
            case "Q":
                dx = -1;
                dy = -1;
                break;
            case "E":
                dx = 1;
                dy = -1;
                break;
            default:
                System.out.println("Неверное направление: " + dir);
                return;
        }
        Move.moveGroup(found, map, dx, dy);
    }

    public void runGameLoop(GameMap map, Player player, Computer comp) {
        boolean gameWon = false;
        while (true) {
            display.printMap(map, player, comp);
            display.displayStatus(player, comp);
            processPlayerTurn(map, player, comp);
            comp.computerTurn(player, map);
            display.printMap(map, player, comp);
            if ((Math.abs(player.getHeroX() - comp.getHeroX()) == 1 && player.getHeroY() == comp.getHeroY())
                    || (Math.abs(player.getHeroY() - comp.getHeroY()) == 1 && player.getHeroX() == comp.getHeroX())) {
                display.showMessage("Герои встретились! Начинается МЕГАБОЙ!");
                boolean pWinMega = BattleHandler.megaBattle(player, comp, mapName);
                if (pWinMega) {
                    display.showMessage("Игрок победил в мегабое!");
                    if (!gameWon) {
                        Scoreboard.addRecord(new Record(
                                player.getPlayerName(),
                                this.mapName,
                                100
                        ));
                        gameWon = true;
                    }
                    comp.getGroups().clear();
                } else {
                    display.showMessage("Компьютер победил в мегабое!");
                    player.getGroups().clear();
                }
            }
            if (map.getCellType(player.getHeroX(), player.getHeroY()) == CellType.COMPUTER_CASTLE) {
                display.showMessage("Игрок захватил замок компьютера! Игрок победил!");
                if (!gameWon) {
                    Scoreboard.addRecord(new Record(
                            player.getPlayerName(),
                            this.mapName,
                            100
                    ));
                }
                break;
            }

            // МИНИИГРА
            if (map.getCellType(player.getHeroX(), player.getHeroY()) == CellType.BATTLE) {
                display.showMessage("Вы нашли останки после сражения...");

                StoredBattleData bd = BattleStorage.loadBattleData();
                if (bd == null) {
                    display.showMessage("Здесь нет никаких трофеев!");
                } else {
                    player.gold += bd.getGold();

                    for (Unit u : bd.getSurvivingUnits()) {
                        BuildingType needed = getBuildingForUnit(u.getType());
                        if (!player.getBuildings().contains(needed)) {
                            player.getBuildings().add(needed);
                            display.showMessage("Постройка " + needed + " получена бесплатно (не было)!");
                        }
                        player.recruitUnit(u);
                    }

                    display.showMessage("Вы получили " + bd.getGold()
                            + " золота и " + bd.getSurvivingUnits().size() + " юнитов!");
                }

                map.clearCell(player.getHeroX(), player.getHeroY());
            }

            if (map.getCellType(comp.getHeroX(), comp.getHeroY()) == CellType.PLAYER_CASTLE) {
                display.showMessage("Компьютер захватил замок игрока! Компьютер победил!");
                break;
            }
            if (player.getArmy().isEmpty()) {
                display.showMessage("У игрока не осталось войск. Компьютер победил!");
                break;
            }
            if (comp.getArmy().isEmpty()) {
                display.showMessage("У компьютера не осталось войск. Игрок победил!");
                if (!gameWon) {
                    Scoreboard.addRecord(new Record(
                            player.getPlayerName(),
                            this.mapName,
                            100
                    ));
                    gameWon = true;
                }
                break;
            }
            if (BattleHandler.checkBattle(player, comp)) {
                display.showMessage("Происходит бой!");
                int totalP = player.getArmy().size();
                int totalC = comp.getArmy().size();
                boolean playerHasBiggerOrEqualArmy = (totalP >= totalC);
                boolean compHasBiggerOrEqualArmy = (totalC >= totalP);

                for (ArmyGroup pg : player.getGroups()) {
                    pg.calculateLuck(map, player.getBuildings(), playerHasBiggerOrEqualArmy);
                }
                for (ArmyGroup cg : comp.getGroups()) {
                    cg.calculateLuck(map, comp.getBuildings(), compHasBiggerOrEqualArmy);
                }
                boolean pWin = BattleHandler.doBattleInRange(player, comp, mapName);
                BattleHandler.removeDeadGroups(player);
                BattleHandler.removeDeadGroups(comp);
                if (pWin) {
                    display.showMessage("Игрок победил в бою!");
                    if (comp.getArmy().isEmpty()) {
                        display.showMessage("Компьютер лишился всех войск — Игрок выиграл игру!");
                        if (!gameWon) {
                            Scoreboard.addRecord(new Record(
                                    player.getPlayerName(),
                                    this.mapName,
                                    100
                            ));
                            gameWon = true;
                        }
                        break;
                    }
                } else {
                    display.showMessage("Компьютер победил в бою!");
                    if (player.getArmy().isEmpty()) {
                        display.showMessage("У игрока не осталось войск — Компьютер выиграл игру!");
                        break;
                    }
                }
            }
        }
    }

    private BuildingType getBuildingForUnit(UnitType t) {
        switch(t) {
            case SPEARMAN:
                return BuildingType.WATCHTOWER;
            case ARCHER:
                return BuildingType.CROSSBOW_TOWER;
            case SWORDSMAN:
                return BuildingType.ARMORY;
            case CAVALRY:
                return BuildingType.ARENA;
            case PALADIN:
                return BuildingType.CATHEDRAL;
            default:
                return BuildingType.WATCHTOWER;
        }
    }

    private void processPlayerTurn(GameMap map, Player player, Computer comp) {
        while (true) {
            String cmd = inputHandler.prompt("\nКоманда? (W/A/S/D/Q/E, RECRUIT, BUILD, MOVE, SAVE, END): ").toUpperCase();
            if (cmd.equals("END")) {
                break;
            } else if (cmd.equals("MOVE")) {
                String typeInput = inputHandler.prompt("Какой тип двигаем? (1-SPEARMAN,2-ARCHER,3-SWORDSMAN,4-CAVALRY,5-PALADIN): ");
                UnitType t = ParsingUtils.parseUnitType(typeInput);
                if (t == null) {
                    display.showMessage("Нет такого типа.");
                    continue;
                }
                String dir = inputHandler.prompt("Направление (W,A,S,D,Q,E): ").toUpperCase();
                moveGroup(player, map, t, dir);
                break;
            } else if (cmd.equals("W")) {
                Move.moveHero(player, map, 0, -1);
                break;
            } else if (cmd.equals("S")) {
                Move.moveHero(player, map, 0, 1);
                break;
            } else if (cmd.equals("A")) {
                Move.moveHero(player, map, -1, 0);
                break;
            } else if (cmd.equals("D")) {
                Move.moveHero(player, map, 1, 0);
                break;
            } else if (cmd.equals("Q")) {
                Move.moveHero(player, map, -1, -1);
                break;
            } else if (cmd.equals("E")) {
                Move.moveHero(player, map, 1, -1);
                break;
            } else if (cmd.equals("RECRUIT")) {
                String typeInput = inputHandler.prompt("Какой тип? (1-SPEARMAN,2-ARCHER,3-SWORDSMAN,4-CAVALRY,5-PALADIN): ");
                UnitType t = ParsingUtils.parseUnitType(typeInput);
                if (t == null) {
                    display.showMessage("Нет такого типа.");
                    continue;
                }
                player.recruitUnit(new Unit(t));
            } else if (cmd.equals("BUILD")) {
                String bInput = inputHandler.prompt("(1-WATCHTOWER(10),2-CROSSBOW_TOWER(15),3-ARMORY(20),4-ARENA(25),5-CATHEDRAL(30),6-LUCKY_TEMPLE(40)): ");
                BuildingType b = ParsingUtils.parseBuildingType(bInput);
                if (b == null) {
                    display.showMessage("Нет такого здания.");
                    continue;
                }
                boolean built = player.buildStructure(b);
                if (built) {
                    GameState state = new GameState(
                            player.getPlayerName(),
                            player,
                            comp,
                            map
                    );
                    String autosaveFileName = "saves/" + mapName + "_autosave.dat";
                    FileManager.saveGame(state, autosaveFileName);
                    display.showMessage("Автосохранение в " + autosaveFileName + " после постройки: " + b);
                }
            } else if (cmd.equals("SAVE")) {
                GameState state = new GameState(
                        player.getPlayerName(),
                        player,
                        comp,
                        map
                );
                String saveFile = "saves/" + player.getPlayerName() + "_save.dat";
                FileManager.saveGame(state, saveFile);
                display.showMessage("Игра сохранена в " + saveFile);
            } else {
                display.showMessage("Неизвестная команда: " + cmd);
            }
        }
    }

    private List<int[]> findAllBattleCells(GameMap map) {
        List<int[]> coords = new ArrayList<>();
        for (int y = 0; y < GameMap.HEIGHT; y++) {
            for (int x = 0; x < GameMap.WIDTH; x++) {
                if (map.getCellType(x, y) == CellType.BATTLE) {
                    coords.add(new int[]{ x, y });
                }
            }
        }
        return coords;
    }

    private void prepareBattleCells(GameMap map) {
        List<int[]> cells = findAllBattleCells(map);
        if (cells.isEmpty()) {
            return;
        }
        System.out.println("Найдены " + cells.size() + " клетки BATTLE. Запускаем мини-игры...");
        for (int[] cell : cells) {
            int x = cell[0];
            int y = cell[1];
            MiniGame mg = new MiniGame();
            mg.startGame();
            int gold = mg.getGoldEarned();
            List<Unit> surv = mg.getSurvivingUnits();
            StoredBattleData sbd = new StoredBattleData(gold, surv);
            BattleStorage.saveBattleData(sbd);
            System.out.println("Бой завершён. " +
                    "Победа игрока = " + mg.isPlayerWon() +
                    ", золото=" + gold + ", кол-во юнитов=" + surv.size());
        }
    }
}
