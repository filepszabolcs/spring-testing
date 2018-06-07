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

package com.progmasters.mordor.validator;

import com.progmasters.mordor.dto.OrcDetails;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class OrcDetailsValidator implements Validator {

    private static final long MAX_KILL_COUNT = 500000;

    public boolean supports(Class clazz) {
        return OrcDetails.class.equals(clazz);
    }

    public void validate(Object obj, Errors e) {
        if (obj != null) {
            OrcDetails orc = (OrcDetails) obj;
            if (orc.getWeapons() != null && orc.getWeapons().size() > 3) {
                e.rejectValue("weapons", "weapons.tooMany");
            }
            if (orc.getKillCount() != null && orc.getKillCount() > MAX_KILL_COUNT) {
                e.rejectValue("killCount", "killCount.tooMany");
            }
            if (orc.getRaceType() == null || orc.getRaceType().trim().equals("")) {
                e.rejectValue("raceType", "raceType.mustChoose");
            }
            if (orc.getName().equals("")) {
                e.rejectValue("name", "name.mustGive");
            }
        }
    }
}
