package com.test.mls;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.mls.model.AnswerSubmissionDto;
import com.test.mls.model.SurveyDTO;
import com.test.mls.service.SurveyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MlsApplication.class)
@AutoConfigureMockMvc
class SurveyControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SurveyService surveyService;

    @Autowired
    private ObjectMapper objectMapper;

    private SurveyDTO surveyDTO;
    private AnswerSubmissionDto answerSubmissionDto;

    @BeforeEach
    void setUp() {
        surveyDTO = new SurveyDTO();
        surveyDTO.setId(1L);
        surveyDTO.setName("Customer Satisfaction Survey");

        answerSubmissionDto = new AnswerSubmissionDto();
        answerSubmissionDto.setSurveyId(1L);
        AnswerSubmissionDto.QuestionAnswerDto questionAnswerDto = new AnswerSubmissionDto.QuestionAnswerDto();
        questionAnswerDto.setQuestionId(1L);
        questionAnswerDto.setSelectedOption(1);
        answerSubmissionDto.setQuestionAnswers(Collections.singletonList(questionAnswerDto));
    }

    @Test
    void createSurvey() throws Exception {
        Mockito.when(surveyService.createSurvey(any(SurveyDTO.class))).thenReturn(surveyDTO);

        mockMvc.perform(post("/api/surveys")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(surveyDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(surveyDTO.getId().intValue())))
                .andExpect(jsonPath("$.name", is(surveyDTO.getName())));
    }

    @Test
    void getSurvey() throws Exception {
        Mockito.when(surveyService.getSurvey(anyLong())).thenReturn(surveyDTO);

        mockMvc.perform(get("/api/surveys/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(surveyDTO.getId().intValue())))
                .andExpect(jsonPath("$.name", is(surveyDTO.getName())));
    }

    @Test
    void getAllSurveys() throws Exception {
        List<SurveyDTO> surveys = Arrays.asList(surveyDTO);

        Mockito.when(surveyService.getAllSurveys()).thenReturn(surveys);

        mockMvc.perform(get("/api/surveys")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(surveyDTO.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(surveyDTO.getName())));
    }

    @Test
    void submitSurveyAnswers() throws Exception {
        mockMvc.perform(post("/api/surveys/submit-answers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(answerSubmissionDto)))
                .andExpect(status().isOk());
    }
}
