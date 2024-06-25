package com.test.mls;

import com.test.mls.entity.Answer;
import com.test.mls.entity.Question;
import com.test.mls.mapper.QuestionMapper;
import com.test.mls.model.QuestionDTO;
import com.test.mls.repository.QuestionRepository;
import com.test.mls.service.impl.QuestionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionServiceImplTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private QuestionMapper questionMapper;

    @InjectMocks
    private QuestionServiceImpl questionService;

    private Question question;
    private QuestionDTO questionDTO;

    @BeforeEach
    void setUp() {
        question = new Question();
        question.setId(1L);
        question.setText("Sample Question");
        question.setOptions(Arrays.asList("Option 1", "Option 2", "Option 3"));
        question.setAnswers(Arrays.asList(
                new Answer(1L, 0, 1, question),
                new Answer(2L, 1, 1, question),
                new Answer(3L, 0, 1, question)
        ));

        questionDTO = new QuestionDTO();
        questionDTO.setId(1L);
        questionDTO.setText("Sample Question");
        questionDTO.setOptions(Arrays.asList("Option 1", "Option 2", "Option 3"));
    }

    @Test
    void addQuestion() {
        when(questionMapper.toEntity(any(QuestionDTO.class))).thenReturn(question);
        when(questionRepository.save(any(Question.class))).thenReturn(question);
        when(questionMapper.toDTO(any(Question.class))).thenReturn(questionDTO);

        QuestionDTO result = questionService.addQuestion(questionDTO);

        assertNotNull(result);
        assertEquals(questionDTO.getId(), result.getId());
        verify(questionRepository, times(1)).save(question);
    }

    @Test
    void getQuestionsBySurvey() {
        when(questionRepository.findBySurveyId(1L)).thenReturn(Arrays.asList(question));
        when(questionMapper.toDTO(any(Question.class))).thenReturn(questionDTO);

        List<QuestionDTO> result = questionService.getQuestionsBySurvey(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(questionRepository, times(1)).findBySurveyId(1L);
    }

    @Test
    void getAnswerDistribution() {
        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));

        Map<String, Double> distribution = questionService.getAnswerDistribution(1L);

        assertNotNull(distribution);
        assertEquals(3, distribution.size());
        assertEquals(66.66666666666666, distribution.get("Option 1"));
        assertEquals(33.33333333333333, distribution.get("Option 2"));
        assertEquals(0.0, distribution.get("Option 3"));
    }

    @Test
    void getAnswerDistributionThrowsExceptionWhenQuestionNotFound() {
        when(questionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> questionService.getAnswerDistribution(1L));
    }
}
