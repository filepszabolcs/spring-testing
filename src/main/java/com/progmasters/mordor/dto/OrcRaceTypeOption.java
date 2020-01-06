package com.progmasters.mordor.dto;

import com.progmasters.mordor.domain.OrcRaceType;

public class OrcRaceTypeOption {

    private String name;
    private String displayName;

    public OrcRaceTypeOption(OrcRaceType orcRaceType) {
        this.name = orcRaceType.toString();
        this.displayName = orcRaceType.getDisplayName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
