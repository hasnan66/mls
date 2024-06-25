package com.test.mls;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.mls.model.QuestionDTO;
import com.test.mls.service.QuestionService;
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

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MlsApplication.class)
@AutoConfigureMockMvc
class QuestionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuestionService questionService;

    @Autowired
    private ObjectMapper objectMapper;

    private QuestionDTO questionDTO;
    private Map<String, Double> distribution;

    @BeforeEach
    void setUp() {
        questionDTO = new QuestionDTO();
        questionDTO.setId(1L);
        questionDTO.setText("Sample Question");
        questionDTO.setOptions(Arrays.asList("Option 1", "Option 2", "Option 3"));

        distribution = new HashMap<>();
        distribution.put("Option 1", 50.0);
        distribution.put("Option 2", 30.0);
        distribution.put("Option 3", 20.0);
    }

    @Test
    void getAnswerDistribution() throws Exception {
        Mockito.when(questionService.getAnswerDistribution(anyLong())).thenReturn(distribution);

        mockMvc.perform(get("/api/questions/{id}/distribution", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Option 1']", is(50.0)))
                .andExpect(jsonPath("$.['Option 2']", is(30.0)))
                .andExpect(jsonPath("$.['Option 3']", is(20.0)));
    }

    @Test
    void getQuestionsBySurvey() throws Exception {
        List<QuestionDTO> questionDTOList = Collections.singletonList(questionDTO);

        Mockito.when(questionService.getQuestionsBySurvey(anyLong())).thenReturn(questionDTOList);

        mockMvc.perform(get("/api/questions/{surveyId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(questionDTO.getId().intValue())))
                .andExpect(jsonPath("$[0].text", is(questionDTO.getText())))
                .andExpect(jsonPath("$[0].options", hasSize(3)))
                .andExpect(jsonPath("$[0].options", contains("Option 1", "Option 2", "Option 3")));
    }
}

