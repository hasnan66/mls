package com.test.mls.mapper;

import com.test.mls.entity.Answer;
import com.test.mls.model.AnswerDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AnswerMapper {
    AnswerDTO toDTO(Answer answer);

    Answer toEntity(AnswerDTO answerDTO);
}
