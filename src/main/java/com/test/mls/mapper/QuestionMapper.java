package com.test.mls.mapper;

import com.test.mls.entity.Question;
import com.test.mls.model.QuestionDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {AnswerMapper.class})
public interface QuestionMapper {
    QuestionDTO toDTO(Question question);

    Question toEntity(QuestionDTO questionDTO);
}
