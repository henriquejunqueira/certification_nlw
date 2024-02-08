package com.rocketseat.certification_nlw.modules.students.entities;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // o lombok coloca os getters e setters automaticamente
@AllArgsConstructor // cria um construtor com todos os atributos
@NoArgsConstructor // cria um construtor vazio
@Entity(name = "students") // indica que isso é uma tabela e que o nome é students
public class StudentEntity {
    
    @Id // indica o id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // pra definir o nome da coluna é só colocar a propriedade nome="nome_coluna"
    @Column(unique = true, nullable = false) // unique se o campo precisa ser único, nullable se ele pode ser nulo, length define no máximo 100 caracteres
    private String email;
    
    @OneToMany(mappedBy = "studentEntity") // Define um relacionamento de um para muitos entre duas entidades. Nesse caso um estudante para muitas certificações
    private List<CertificationStudentEntity> certificationStudentEntity;

    @CreationTimestamp
    private LocalDateTime createdAt;

}
