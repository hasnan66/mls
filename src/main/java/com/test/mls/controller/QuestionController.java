package com.test.mls.controller;

import com.test.mls.model.QuestionDTO;
import com.test.mls.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    @GetMapping("/{id}/distribution")
    public ResponseEntity<Map<String, Double>> getAnswerDistribution(@PathVariable Long id) {
        Map<String, Double> distribution = questionService.getAnswerDistribution(id);
        return new ResponseEntity<>(distribution, HttpStatus.OK);
    }

    @GetMapping("/{surveyId}")
    public ResponseEntity<List<QuestionDTO>> getQuestionsBySurvey(@PathVariable Long surveyId) {
        List<QuestionDTO> questions = questionService.getQuestionsBySurvey(surveyId);
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }
}
