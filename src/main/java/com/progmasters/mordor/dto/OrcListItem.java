package com.progmasters.mordor.dto;

import com.progmasters.mordor.domain.Orc;
import com.progmasters.mordor.domain.WeaponType;

import java.util.List;
import java.util.stream.Collectors;

public class OrcListItem {

    private Long id;

    private String name;

    private String orcRaceType;

    private Long killCount;

    private List<String> weapons;

    public OrcListItem(Orc orc) {
        this.id = orc.getId();
        this.name = orc.getName();
        this.orcRaceType = orc.getOrcRaceType().getDisplayName();
        this.killCount = orc.getKillCount();
        this.weapons = orc.getWeapons().stream().map(WeaponType::getDisplayName).collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOrcRaceType() {
        return orcRaceType;
    }

    public Long getKillCount() {
        return killCount;
    }

    public List<String> getWeapons() {
        return weapons;
    }
}
