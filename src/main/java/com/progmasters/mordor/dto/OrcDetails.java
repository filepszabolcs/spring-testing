package com.progmasters.mordor.dto;

import com.progmasters.mordor.domain.Orc;
import com.progmasters.mordor.domain.WeaponType;

import java.util.List;
import java.util.stream.Collectors;

public class OrcDetails {

    private Long id;

    private String name;

    private String raceType;

    private Long killCount;

    private List<String> weapons;

    public OrcDetails() {}

    public OrcDetails(Orc orc) {
        this.id = orc.getId();
        this.name = orc.getName();
        this.raceType = orc.getOrcRaceType().name();
        this.killCount = orc.getKillCount();
        this.weapons = orc.getWeapons().stream().map(WeaponType::name).collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRaceType() {
        return raceType;
    }

    public Long getKillCount() {
        return killCount;
    }

    public List<String> getWeapons() {
        return weapons;
    }
}
