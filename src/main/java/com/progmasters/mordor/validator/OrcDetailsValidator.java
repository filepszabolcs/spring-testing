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
import com.progmasters.mordor.service.OrcValidatorService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class OrcDetailsValidator implements Validator {

    private final OrcValidatorService validatorService;

    public OrcDetailsValidator(OrcValidatorService validatorService) {
        this.validatorService = validatorService;
    }

    @Override
    public boolean supports(Class clazz) {
        return OrcDetails.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors e) {
        if (obj != null) {
            OrcDetails orc = (OrcDetails) obj;
            validatorService.validateCommonAttributes(orc, e);
            if (orc.getName() != null) {
                if (orc.getId() != null) {
                    validatorService.checkNameUniquenessOnUpdate(orc, e);
                } else {
                    if (validatorService.nameIsTaken(orc.getName())) {
                        e.rejectValue("name", "name.mustBeUnique");
                    }
                }
            }
        }
    }
}
