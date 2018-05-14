package com.progmasters.mordor.dto;

import java.util.Map;

public class OrcFormData {
    private Map<String, String> weapons;
    private Map<String, String> raceTypes;

    public OrcFormData(Map<String, String> weapons, Map<String, String> raceTypes) {
        this.weapons = weapons;
        this.raceTypes = raceTypes;
    }

    public Map<String, String> getWeapons() {
        return weapons;
    }

    public Map<String, String> getRaceTypes() {
        return raceTypes;
    }

}
