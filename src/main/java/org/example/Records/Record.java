package org.example.Records;

import java.io.Serializable;
import java.util.Objects;

public class Record implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String playerName;
    private final String mapName;
    private final int score;
    private final long timestamp;

    public Record(String playerName, String mapName, int score) {
        this.playerName = playerName;
        this.mapName = mapName;
        this.score = score;
        this.timestamp = System.currentTimeMillis();
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getMapName() {
        return mapName;
    }

    public int getScore() {
        return score;
    }

    public long getTimestamp() {
        return timestamp;
    }


    @Override
    public String toString() {
        return "Record{" +
                "playerName='" + playerName + '\'' +
                ", mapName='" + mapName + '\'' +
                ", score=" + score +
                ", timestamp=" + timestamp +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Record))
            return false;
        Record record = (Record) o;
        return score == record.score
                && timestamp == record.timestamp
                && Objects.equals(playerName, record.playerName)
                && Objects.equals(mapName, record.mapName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerName, mapName, score, timestamp);
    }
}
