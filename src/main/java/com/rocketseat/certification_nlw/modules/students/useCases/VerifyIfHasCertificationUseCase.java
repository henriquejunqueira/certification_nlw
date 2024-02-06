package com.rocketseat.certification_nlw.modules.students.useCases;

import org.springframework.stereotype.Service;

import com.rocketseat.certification_nlw.modules.students.dto.VerifyIfHasCertificationDTO;

@Service
public class VerifyIfHasCertificationUseCase {
    
    public boolean execute(VerifyIfHasCertificationDTO dto){
        if(dto.getEmail().equals("henriquejunqueir94@gmail.com") && dto.getTechnology().equals("JAVA")){
            return true;
        }

        return false;
    }

}
