package com.test.mls.service.impl;

import com.test.mls.entity.Question;
import com.test.mls.mapper.QuestionMapper;
import com.test.mls.model.QuestionDTO;
import com.test.mls.repository.QuestionRepository;
import com.test.mls.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionMapper questionMapper;

    @Transactional
    @Override
    public synchronized QuestionDTO addQuestion(QuestionDTO questionDTO) {
        Question question = questionMapper.toEntity(questionDTO);
        Question savedQuestion = questionRepository.save(question);
        return questionMapper.toDTO(savedQuestion);
    }

    @Transactional(readOnly = true)
    @Override
    public List<QuestionDTO> getQuestionsBySurvey(Long surveyId) {
        List<Question> questions = questionRepository.findBySurveyId(surveyId);
        return questions.stream().map(questionMapper::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public Map<String, Double> getAnswerDistribution(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

        Map<String, Double> distribution = new HashMap<>();
        int totalAnswers = question.getAnswers().size();

        for (String option : question.getOptions()) {
            int count = (int) question.getAnswers().stream()
                    .filter(a -> a.getSelectedOption() == question.getOptions().indexOf(option)).count();
            distribution.put(option, (double) count / totalAnswers * 100);
        }

        return distribution;
    }
}