package com.test.mls.model;

import lombok.Data;

import java.util.List;

@Data
public class AnswerSubmissionDto {

    private Long surveyId;
    private List<QuestionAnswerDto> questionAnswers;

    @Data
    public static class QuestionAnswerDto {
        private Long questionId;
        private int selectedOption;
    }
}
