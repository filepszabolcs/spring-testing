/*
 * Copyright © Progmasters (QTC Kft.), 2016-2018.
 * All rights reserved. No part or the whole of this Teaching Material (TM) may be reproduced, copied, distributed, 
 * publicly performed, disseminated to the public, adapted or transmitted in any form or by any means, including 
 * photocopying, recording, or other electronic or mechanical methods, without the prior written permission of QTC Kft. 
 * This TM may only be used for the purposes of teaching exclusively by QTC Kft. and studying exclusively by QTC Kft.’s 
 * students and for no other purposes by any parties other than QTC Kft.
 * This TM shall be kept confidential and shall not be made public or made available or disclosed to any unauthorized person.
 * Any dispute or claim arising out of the breach of these provisions shall be governed by and construed in accordance with the laws of Hungary.
 */

package com.progmasters.mordor.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orc")
public class Orc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "orc_name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "orc_race_type")
    private OrcRaceType orcRaceType;

    @Column(name = "orc_kill_count")
    private Long killCount;

    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = WeaponType.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "orc_weapon")
    @Column(name = "orc_weapon")
    private List<WeaponType> weapons = new ArrayList<>();

    public Orc() {}

    public Orc(String name, OrcRaceType orcRaceType, Long killCount, List<WeaponType> weapons) {
        this.name = name;
        this.orcRaceType = orcRaceType;
        this.killCount = killCount;
        this.weapons = weapons;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OrcRaceType getOrcRaceType() {
        return orcRaceType;
    }

    public void setOrcRaceType(OrcRaceType orcRaceType) {
        this.orcRaceType = orcRaceType;
    }

    public Long getKillCount() {
        return killCount;
    }

    public void setKillCount(Long killCount) {
        this.killCount = killCount;
    }

    public List<WeaponType> getWeapons() {
        return weapons;
    }

    public void setWeapons(List<WeaponType> weapons) {
        this.weapons = weapons;
    }

    @Override
    public String toString() {
        return "Orc{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", orcRaceType=" + orcRaceType +
                ", killCount=" + killCount +
                ", weapons=" + weapons +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Orc orc = (Orc) o;

        return id != null ? id.equals(orc.id) : orc.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
