package com.rocketseat.certification_nlw.modules.students.entities;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // o lombok coloca os getters e setters automaticamente
@AllArgsConstructor // cria um construtor com todos os atributos
@NoArgsConstructor // cria um construtor vazio
public class StudentEntity {
    
    private UUID id;
    private String email;
    private List<CertificationStudentEntity> certificationStudentEntities;

}
