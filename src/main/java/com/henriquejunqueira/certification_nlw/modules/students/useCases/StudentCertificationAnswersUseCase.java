package com.henriquejunqueira.certification_nlw.modules.students.useCases;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.henriquejunqueira.certification_nlw.modules.questions.entities.QuestionEntity;
import com.henriquejunqueira.certification_nlw.modules.questions.repositories.QuestionRepository;
import com.henriquejunqueira.certification_nlw.modules.students.dto.StudentCertificationAnswerDTO;
import com.henriquejunqueira.certification_nlw.modules.students.dto.VerifyHasCertificationDTO;
import com.henriquejunqueira.certification_nlw.modules.students.entities.AnswersCertificationsEntity;
import com.henriquejunqueira.certification_nlw.modules.students.entities.CertificationStudentEntity;
import com.henriquejunqueira.certification_nlw.modules.students.entities.StudentEntity;
import com.henriquejunqueira.certification_nlw.modules.students.repositories.CertificationStudentRepository;
import com.henriquejunqueira.certification_nlw.modules.students.repositories.StudentRepository;

@Service
public class StudentCertificationAnswersUseCase {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private CertificationStudentRepository certificationStudentRepository;

    @Autowired
    private VerifyIfHasCertificationUseCase verifyIfHasCertificationUseCase;

    public CertificationStudentEntity execute(StudentCertificationAnswerDTO dto) throws Exception{

        var hasCertification = this.verifyIfHasCertificationUseCase.execute(new VerifyHasCertificationDTO(dto.getEmail(), dto.getTechnology()));
        
        if(hasCertification){
            throw new Exception("Você já tirou essa certificação!");
        }

        // Buscar as alternativas das perguntas (correta ou incorreta)
        List<QuestionEntity> questionsEntity = questionRepository.findByTechnology(dto.getTechnology());
        List<AnswersCertificationsEntity> answersCertifications = new ArrayList<>();

        AtomicInteger correctAnswers = new AtomicInteger(0);

        dto.getQuestionsAnswers()
        .stream().forEach(questionAnswer -> {
            var question = questionsEntity.stream()
            .filter(quest -> quest.getId().equals(questionAnswer.getQuestionID()));

            var findCorrectAlternative = question.findFirst().get()
            .getAlternatives().stream()
            .filter(alternative -> alternative.isCorrect()).findFirst().get();

            if(findCorrectAlternative.getId().equals(questionAnswer.getAlternativeID())){
                questionAnswer.setCorrect(true);
                correctAnswers.incrementAndGet();
            }else{
                questionAnswer.setCorrect(false);
            }

            var answersCertificationsEntity = AnswersCertificationsEntity.builder()
            .answerID(questionAnswer.getAlternativeID())
            .questionID(questionAnswer.getQuestionID())
            .isCorrect(questionAnswer.isCorrect()).build();

            answersCertifications.add(answersCertificationsEntity);
        });

        // Verificar se existe student pelo email
        var student = studentRepository.findByEmail(dto.getEmail());
        UUID studentID;

        if(student.isEmpty()){
            var studentCreated = StudentEntity.builder().email(dto.getEmail()).build();

            studentCreated = studentRepository.save(studentCreated);
            studentID = studentCreated.getId();
        }else{
            studentID = student.get().getId();
        }

        CertificationStudentEntity certificationStudentEntity = CertificationStudentEntity.builder()
        .technology(dto.getTechnology())
        .studentID(studentID)
        .grade(correctAnswers.get())
        .build();

        var certificationStudentCreated = certificationStudentRepository.save(certificationStudentEntity);

        answersCertifications.stream().forEach(answersCertification -> {
            answersCertification.setCertificationID(certificationStudentEntity.getId());
            answersCertification.setCertificationStudentEntity(certificationStudentEntity);
        });

        certificationStudentEntity.setAnswersCertificationsEntities(answersCertifications);

        certificationStudentRepository.save(certificationStudentEntity);

        return certificationStudentCreated;

        // Salvar as informações da certificação
    }

}
