package org.example;

import org.example.Enum.BuildingType;

import java.util.Arrays;
import java.util.List;

public final class CompBuildOrder {
    public static final List<BuildingType> ORDER = Arrays.asList(
            BuildingType.WATCHTOWER,
            BuildingType.CROSSBOW_TOWER,
            BuildingType.ARMORY,
            BuildingType.ARENA,
            BuildingType.CATHEDRAL
    );
}
