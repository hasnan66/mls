package com.test.mls;

import com.test.mls.entity.Answer;
import com.test.mls.entity.Question;
import com.test.mls.entity.Survey;
import com.test.mls.mapper.SurveyMapper;
import com.test.mls.model.AnswerSubmissionDto;
import com.test.mls.model.SurveyDTO;
import com.test.mls.repository.AnswerRepository;
import com.test.mls.repository.QuestionRepository;
import com.test.mls.repository.SurveyRepository;
import com.test.mls.service.impl.SurveyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SurveyServiceImplTest {

    @Mock
    private SurveyRepository surveyRepository;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private SurveyMapper surveyMapper;

    @InjectMocks
    private SurveyServiceImpl surveyService;

    private Survey survey;
    private SurveyDTO surveyDTO;

    @BeforeEach
    void setUp() {
        survey = new Survey();
        survey.setId(1L);
        survey.setName("Sample Survey");

        Question question = new Question();
        question.setId(1L);
        question.setText("Sample Question");
        question.setOptions(Arrays.asList("Option 1", "Option 2", "Option 3"));

        survey.setQuestions(Arrays.asList(question));

        surveyDTO = new SurveyDTO();
        surveyDTO.setId(1L);
        surveyDTO.setName("Sample Survey");
    }

    @Test
    void createSurvey() {
        when(surveyMapper.toEntity(any(SurveyDTO.class))).thenReturn(survey);
        when(surveyRepository.save(any(Survey.class))).thenReturn(survey);
        when(surveyMapper.toDTO(any(Survey.class))).thenReturn(surveyDTO);

        SurveyDTO result = surveyService.createSurvey(surveyDTO);

        assertNotNull(result);
        assertEquals(surveyDTO.getId(), result.getId());
        verify(surveyRepository, times(1)).save(survey);
    }

    @Test
    void getSurvey() {
        when(surveyRepository.findById(1L)).thenReturn(Optional.of(survey));
        when(surveyMapper.toDTO(any(Survey.class))).thenReturn(surveyDTO);

        SurveyDTO result = surveyService.getSurvey(1L);

        assertNotNull(result);
        assertEquals(surveyDTO.getId(), result.getId());
        verify(surveyRepository, times(1)).findById(1L);
    }

    @Test
    void getSurveyThrowsExceptionWhenNotFound() {
        when(surveyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> surveyService.getSurvey(1L));
    }

    @Test
    void getAllSurveys() {
        when(surveyRepository.findAll()).thenReturn(Arrays.asList(survey));
        when(surveyMapper.toDTO(any(Survey.class))).thenReturn(surveyDTO);

        List<SurveyDTO> result = surveyService.getAllSurveys();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(surveyRepository, times(1)).findAll();
    }

    @Test
    void submitSurveyAnswers() {
        AnswerSubmissionDto answerSubmissionDto = new AnswerSubmissionDto();
        answerSubmissionDto.setSurveyId(1L);

        AnswerSubmissionDto.QuestionAnswerDto questionAnswerDto = new AnswerSubmissionDto.QuestionAnswerDto();
        questionAnswerDto.setQuestionId(1L);
        questionAnswerDto.setSelectedOption(0);

        answerSubmissionDto.setQuestionAnswers(Arrays.asList(questionAnswerDto));

        when(surveyRepository.findById(1L)).thenReturn(Optional.of(survey));
        when(questionRepository.findById(1L)).thenReturn(Optional.of(survey.getQuestions().get(0)));

        surveyService.submitSurveyAnswers(answerSubmissionDto);

        verify(answerRepository, times(1)).save(any(Answer.class));
    }

    @Test
    void submitSurveyAnswersThrowsExceptionWhenSurveyNotFound() {
        AnswerSubmissionDto answerSubmissionDto = new AnswerSubmissionDto();
        answerSubmissionDto.setSurveyId(1L);

        when(surveyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> surveyService.submitSurveyAnswers(answerSubmissionDto));
    }

    @Test
    void submitSurveyAnswersThrowsExceptionWhenQuestionNotFound() {
        AnswerSubmissionDto answerSubmissionDto = new AnswerSubmissionDto();
        answerSubmissionDto.setSurveyId(1L);

        AnswerSubmissionDto.QuestionAnswerDto questionAnswerDto = new AnswerSubmissionDto.QuestionAnswerDto();
        questionAnswerDto.setQuestionId(1L);
        questionAnswerDto.setSelectedOption(0);

        answerSubmissionDto.setQuestionAnswers(Arrays.asList(questionAnswerDto));

        when(surveyRepository.findById(1L)).thenReturn(Optional.of(survey));
        when(questionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> surveyService.submitSurveyAnswers(answerSubmissionDto));
    }
}
