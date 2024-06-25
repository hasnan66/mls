package com.test.mls.service;

import com.test.mls.model.QuestionDTO;

import java.util.List;
import java.util.Map;

public interface QuestionService {

    QuestionDTO addQuestion(QuestionDTO question);

    List<QuestionDTO> getQuestionsBySurvey(Long surveyId);

    Map<String, Double> getAnswerDistribution(Long questionId);
}
