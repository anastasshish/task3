import org.example.*;
import org.example.Enum.CellType;
import org.example.Enum.BuildingType;
import org.example.Heroes.Computer;
import org.example.Heroes.Player;
import org.example.Records.Scoreboard;
import org.example.UI.FileManager;
import org.example.UI.GameMap;
import org.example.Records.Record;
import org.junit.jupiter.api.*;
import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    private static final String TestMapPath = "maps/test_map.dat";
    private static final String TestSavePath = "saves/test_save.dat";
    private static final String TestAutosavePath = "saves/TestMap_autosave.dat";

    @AfterEach
    public void cleanUp() {
        new File(TestMapPath).delete();
        new File(TestSavePath).delete();
        new File(TestAutosavePath).delete();
    }

    @Test
    void testMapEditCorrectCellCoordinates() {
        GameMap map = new GameMap();
        map.setCellType(5, 5, CellType.PLAYER_CASTLE);
        assertEquals(CellType.PLAYER_CASTLE, map.getCellType(5, 5));
    }

    @Test
    void testMapSaveCorrectSerialization() {
        GameMap map = new GameMap();
        map.setCellType(1, 1, CellType.COMPUTER_CASTLE);
        FileManager.saveMap(map, TestMapPath);
        assertTrue(new File(TestMapPath).exists());
    }

    @Test
    void testPlayerBuildArenaBuilding() {
        Player player = new Player(8, 8);
        boolean built = player.buildStructure(BuildingType.ARENA);
        assertTrue(built);
        assertTrue(player.getBuildings().contains(BuildingType.ARENA));
    }


    @Test
    void testMapLoadByFilenameCorrectMapLoaded() {
        GameMap map = new GameMap();
        map.setCellType(3, 3, CellType.PLAYER_ZONE);
        FileManager.saveMap(map, TestMapPath);

        GameMap loaded = FileManager.loadMap(TestMapPath);
        assertEquals(CellType.PLAYER_ZONE, loaded.getCellType(3, 3));
    }


    @Test
    void testGameStateLoadCorrectRestoration() {
        GameMap map = new GameMap();
        Player p = new Player(8, 8);
        p.setPlayerName("Tester");
        p.buildStructure(BuildingType.ARENA);

        Computer c = new Computer(1, 1);
        GameState state = new GameState("Tester", p, c, map);
        FileManager.saveGame(state, TestSavePath);

        GameState loaded = FileManager.loadGameState(TestSavePath);
        assertNotNull(loaded);
        assertEquals("Tester", loaded.getPlayerName());
        assertEquals(p.getBuildings().size(), loaded.getPlayer().getBuildings().size());
    }

    @Test
    void testAutoSaveTriggeredAfterBuild() {
        GameMap map = new GameMap();
        Player p = new Player(8, 8);
        p.setPlayerName("AutoSaveTester");
        Computer c = new Computer(1, 1);
        p.buildStructure(org.example.Enum.BuildingType.WATCHTOWER);

        GameState state = new GameState(p.getPlayerName(), p, c, map);
        FileManager.saveGame(state, TestAutosavePath);

        assertTrue(new File(TestAutosavePath).exists());
    }

    @Test
    void testScoreboardRecordUpdateAddsOrUpdatesProperly() {
        File scoreFile = new File("scores.dat");
        if (scoreFile.exists()) {
            scoreFile.delete();
        }

        String testPlayer = "ScoreTester";
        String mapName = "TestMap";

        Record old = new Record(testPlayer, mapName, 100);
        Record more = new Record(testPlayer, mapName, 150);

        Scoreboard.addRecord(old);
        Scoreboard.addRecord(more);

        List<Record> records = Scoreboard.loadRecords();
        boolean found = records.stream()
                .anyMatch(r -> r.getPlayerName().equals(testPlayer)
                        && r.getMapName().equals(mapName)
                        && r.getScore() == 250);

        assertTrue(found, "Рекорд должен обновиться как сумма очков");
    }
}
