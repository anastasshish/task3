package org.example.MiniGameJson;

import org.example.BattleHandler;
import org.example.Enum.CellType;
import org.example.Enum.Side;
import org.example.Enum.UnitType;
import org.example.Heroes.ArmyGroup;
import org.example.Heroes.Computer;
import org.example.Heroes.Player;
import org.example.Heroes.Unit;
import org.example.Move.Move;
import org.example.UI.GameMap;

import java.util.*;

public class MiniGame {

    private static final int SIZE = 6;
    private final CellType[][] board;
    private final Player tempPlayer;
    private final Computer tempComp;
    private final GameMap miniMap;
    private boolean playerWon;
    private int goldEarned;
    private static final int REWARD_GOLD = 100;


    public MiniGame() {
        board = new CellType[SIZE][SIZE];
        for (int r = 0; r < SIZE; r++) {
            Arrays.fill(board[r], CellType.ROAD);
        }

        miniMap = new GameMap(true);
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                miniMap.setCellType(x, y, CellType.ROAD);
            }
        }

        tempPlayer = new Player(0, 0);
        tempComp   = new Computer(0, 0);
        tempPlayer.getGroups().clear();
        tempComp.getGroups().clear();

        initUnits();
    }

    private void initUnits() {
        UnitType[] allTypes = UnitType.values();
        Random rand = new Random();

        for (int i = 0; i < 2; i++) {
            UnitType t = allTypes[rand.nextInt(allTypes.length)];
            Unit u = new Unit(t);
            int col = rand.nextInt(SIZE);
            int row = SIZE - 1;
            ArmyGroup g = new ArmyGroup(t, col, row, Side.PLAYER);
            g.addUnit(u);
            tempPlayer.getGroups().add(g);
        }

        for (int i = 0; i < 2; i++) {
            UnitType t = allTypes[rand.nextInt(allTypes.length)];
            Unit u = new Unit(t);
            int col = rand.nextInt(SIZE);
            int row = 0;
            ArmyGroup g = new ArmyGroup(t, col, row, Side.COMPUTER);
            g.addUnit(u);
            tempComp.getGroups().add(g);
        }
    }

    public void startGame() {
        playerWon = false;
        goldEarned = 0;

        Scanner sc = new Scanner(System.in);
        Side currentSide = Side.PLAYER;

        while (!tempPlayer.getGroups().isEmpty() && !tempComp.getGroups().isEmpty()) {
            System.out.println("\n=== Ход  " + currentSide + " ===");

            if (canAttack(currentSide)) {
                boolean sideWon = doBattle(currentSide);
                if (sideWon) {
                    if (tempComp.getGroups().isEmpty()) {
                        break;
                    }
                    if (tempPlayer.getGroups().isEmpty()) {
                        break;
                    }
                }
            }

            printBoard();

            if (currentSide == Side.PLAYER) {
                playerMove(sc);
                currentSide = Side.COMPUTER;
            } else {
                computerMove();
                currentSide = Side.PLAYER;
            }
        }

        if (tempPlayer.getGroups().isEmpty() && tempComp.getGroups().isEmpty()) {
            System.out.println("Ничья!");
        } else if (tempPlayer.getGroups().isEmpty()) {
            System.out.println("Игрок проиграл!");

        } else if (tempComp.getGroups().isEmpty()) {
            System.out.println("Игрок выиграл!");
            playerWon = true;
            goldEarned = REWARD_GOLD;
        }

        if (playerWon) {
            System.out.println("Вы заработали " + goldEarned + " золота!");
        }

        storeResult();
    }

    private boolean canAttack(Side currentSide) {
        if (currentSide == Side.PLAYER) {
            return BattleHandler.checkBattle(tempPlayer, tempComp);
        } else {
            return BattleHandler.checkBattle(tempComp, tempPlayer);
        }
    }

    private boolean doBattle(Side currentSide) {
        System.out.println("\nБой начинается автоматически, т.к. стороны в радиусе!");
        boolean sideWon;
        if (currentSide == Side.PLAYER) {
            sideWon = BattleHandler.doBattleInRange(tempPlayer, tempComp, "MiniGame6x6");
            BattleHandler.removeDeadGroups(tempPlayer);
            BattleHandler.removeDeadGroups(tempComp);
            if (sideWon) {
                System.out.println("Игрок выиграл этот бой!");
            } else {
                System.out.println("Комп выиграл этот бой!");
            }
        } else {
            sideWon = BattleHandler.doBattleInRange(tempPlayer, tempComp, "MiniGame6x6");
            BattleHandler.removeDeadGroups(tempPlayer);
            BattleHandler.removeDeadGroups(tempComp);
            if (!sideWon) {
                System.out.println("Компьютер выиграл бой!");
            } else {
                System.out.println("Игрок выиграл бой!");
            }
        }
        return sideWon;
    }

    private void playerMove(Scanner sc) {
        if (tempPlayer.getGroups().isEmpty())
            return;
        System.out.print("\nИгрок: (move/skip)? ");
        String cmd = sc.nextLine().trim().toLowerCase();
        if (cmd.equals("move")) {
            doPlayerMove(sc);
        } else {
            System.out.println("Пропуск хода...");
        }
    }

    private void doPlayerMove(Scanner sc) {
        List<ArmyGroup> groups = tempPlayer.getGroups();
        for (int i = 0; i < groups.size(); i++) {
            ArmyGroup g = groups.get(i);
            System.out.printf("%d) %s @(%d,%d)\n", i, g.getType(), g.getX(), g.getY());
        }
        System.out.print("Выберите группу или пропусти код: ");
        String line = sc.nextLine().trim();
        if (line.equalsIgnoreCase("skip")) return;
        int idx;
        try {
            idx = Integer.parseInt(line);
        } catch (NumberFormatException e) {
            return;
        }
        if (idx < 0 || idx >= groups.size()) return;

        ArmyGroup chosen = groups.get(idx);
        System.out.print("Направление (W/A/S/D/Q/E/)? ");
        String dir = sc.nextLine().trim().toUpperCase();
        int dx=0, dy=0;
        switch (dir) {
            case "W":
                dy=-1;
            break;
            case "S":
                dy= 1;
            break;
            case "A":
                dx=-1;
            break;
            case "D":
                dx= 1;
            break;
            case "Q":
                dx=-1;
                dy=-1;
                break;
            case "E":
                dx= 1;
                dy=-1; break;
            default:
                System.out.println("Неизвестное направление.");
                return;
        }
        Move.moveGroup(chosen, miniMap, dx, dy);
        if (chosen.isEmpty()) {
            System.out.println("Группа погибла от штрафов!");
            groups.remove(chosen);
        }
    }

    private void computerMove() {
        List<ArmyGroup> cg = tempComp.getGroups();
        if (cg.isEmpty()) return;
        ArmyGroup pick = cg.get(new Random().nextInt(cg.size()));
        Move.moveGroup(pick, miniMap, 0, 1);
        if (pick.isEmpty()) {
            cg.remove(pick);
        }
    }

    private void printBoard() {
        for (int row=0; row<SIZE; row++){
            for (int col=0; col<SIZE; col++){
                ArmyGroup pg = GameMap.findGroupAt(tempPlayer.getGroups(), col, row);
                if (pg != null) {
                    System.out.print(GameMap.getPlayerSymbol(pg.getType())+ "  ");
                    continue;
                }
                ArmyGroup cg = GameMap.findGroupAt(tempComp.getGroups(), col, row);
                if (cg != null) {
                    System.out.print(GameMap.getCompSymbol(cg.getType())+ "  ");
                    continue;
                }
                System.out.print(".  ");
            }
            System.out.println();
        }
    }

    public boolean isPlayerWon(){
        return playerWon;
    }

    public int getGoldEarned(){
        return goldEarned;
    }

    public List<Unit> getSurvivingUnits() {
        List<Unit> result = new ArrayList<>();
        if (playerWon) {
            for (ArmyGroup ag : tempPlayer.getGroups()) {
                result.addAll(ag.getUnits());
            }
        } else {
            for (ArmyGroup ag : tempComp.getGroups()) {
                result.addAll(ag.getUnits());
            }
        }
        return result;
    }


    private void storeResult() {
        int finalGold;
        List<Unit> finalUnits = new ArrayList<>();
        if (playerWon) {
            finalGold = goldEarned;
            finalUnits.addAll(getSurvivingUnits());
        } else {
            finalGold = 0;
            for (ArmyGroup ag : tempComp.getGroups()) {
                finalUnits.addAll(ag.getUnits());
            }
        }
        StoredBattleData data = new StoredBattleData(finalGold, finalUnits);
        BattleStorage.saveBattleData(data);
        System.out.println("Результат мини-игры сохранён в miniGame.json");
    }
}
