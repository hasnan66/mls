package com.test.mls.service.impl;

import com.test.mls.entity.Answer;
import com.test.mls.entity.Question;
import com.test.mls.entity.Survey;
import com.test.mls.mapper.SurveyMapper;
import com.test.mls.model.AnswerSubmissionDto;
import com.test.mls.model.SurveyDTO;
import com.test.mls.repository.AnswerRepository;
import com.test.mls.repository.QuestionRepository;
import com.test.mls.repository.SurveyRepository;
import com.test.mls.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SurveyServiceImpl implements SurveyService {

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private SurveyMapper surveyMapper;

    @Transactional
    @Override
    public synchronized SurveyDTO createSurvey(SurveyDTO surveyDTO) {
        Survey survey = surveyMapper.toEntity(surveyDTO);
        Survey savedSurvey = surveyRepository.save(survey);
        return surveyMapper.toDTO(savedSurvey);
    }

    @Transactional(readOnly = true)
    @Override
    public SurveyDTO getSurvey(Long id) {
        Survey survey = surveyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Survey not found"));
        return surveyMapper.toDTO(survey);
    }

    @Transactional(readOnly = true)
    @Override
    public List<SurveyDTO> getAllSurveys() {
        List<Survey> surveys = surveyRepository.findAll();
        return surveys.stream().map(surveyMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void submitSurveyAnswers(AnswerSubmissionDto answerSubmissionDto) {
        Survey survey = surveyRepository.findById(answerSubmissionDto.getSurveyId())
                .orElseThrow(() -> new RuntimeException("Survey not found"));

        for (AnswerSubmissionDto.QuestionAnswerDto questionAnswerDto : answerSubmissionDto.getQuestionAnswers()) {
            Question question = questionRepository.findById(questionAnswerDto.getQuestionId())
                    .orElseThrow(() -> new RuntimeException("Question not found"));

            Answer answer = new Answer();
            answer.setSelectedOption(questionAnswerDto.getSelectedOption());
            answer.setQuestion(question);

            question.getAnswers().add(answer);
            answerRepository.save(answer);
        }
    }
}
