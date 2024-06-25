package com.test.mls.mapper;

import com.test.mls.entity.Survey;
import com.test.mls.model.SurveyDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {QuestionMapper.class})
public interface SurveyMapper {
    SurveyDTO toDTO(Survey survey);

    Survey toEntity(SurveyDTO surveyDTO);
}
