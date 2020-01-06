package com.progmasters.mordor.dto;

import com.progmasters.mordor.domain.WeaponType;

public class WeaponOption {

    private String name;
    private String displayName;

    public WeaponOption(WeaponType weaponType) {
        this.name = weaponType.toString();
        this.displayName = weaponType.getDisplayName();
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
