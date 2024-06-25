package com.test.mls.service;

import com.test.mls.model.AnswerSubmissionDto;
import com.test.mls.model.SurveyDTO;

import java.util.List;

public interface SurveyService {

    SurveyDTO createSurvey(SurveyDTO surveyDTO);

    SurveyDTO getSurvey(Long id);

    List<SurveyDTO> getAllSurveys();

    void submitSurveyAnswers(AnswerSubmissionDto answerSubmissionDto);
}
