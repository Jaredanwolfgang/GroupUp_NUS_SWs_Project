package com.sustech.groupup.entity.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sustech.groupup.entity.api.SurveyDTO;
import com.sustech.groupup.entity.db.SurveyEntity;
import com.sustech.groupup.entity.db.SurveyStatus;
import com.sustech.groupup.services.SurveyService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SurveyConverter {

    private final SurveyService surveyService;
    private final ObjectMapper objectMapper;

    public SurveyDTO toDTO(SurveyEntity surveyEntity) throws JsonProcessingException {

        SurveyDTO surveyDTO = new SurveyDTO();
        surveyDTO.setName(surveyEntity.getName());
        surveyDTO.setDescription(surveyEntity.getDescription());
        surveyDTO.setCreateAt(surveyEntity.getCreateAt());
        surveyDTO.setUpdateAt(surveyEntity.getUpdateAt());
        surveyDTO.setPersonalInfo(objectMapper.readTree(surveyEntity.getPersonalInfo()));
        surveyDTO.setQuestions(objectMapper.readTree(surveyEntity.getQuestions()));

        surveyDTO.setRestriction(objectMapper.readTree(surveyEntity.getGroupRestriction()));
        surveyDTO.setOwners(surveyService.getOwnerIdBySurveyId(surveyEntity.getId()));
        surveyDTO.setMembers(surveyService.getMemberIdBySurveyId(surveyEntity.getId()));
        return surveyDTO;
    }
    public SurveyEntity toEntity(SurveyDTO surveyDTO) {
        SurveyEntity surveyEntity = new SurveyEntity();
        surveyEntity.setName(surveyDTO.getName());
        surveyEntity.setDescription(surveyDTO.getDescription());
        surveyEntity.setCreateAt(surveyDTO.getCreateAt());
        surveyEntity.setUpdateAt(surveyDTO.getUpdateAt());
        surveyEntity.setStatus(SurveyStatus.CLOSED);
        surveyEntity.setPersonalInfo(surveyDTO.getPersonalInfo().asText());
        surveyEntity.setQuestions(surveyDTO.getQuestions().asText());
        return surveyEntity;
    }

}
