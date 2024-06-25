package com.test.mls.controller;

import com.test.mls.model.AnswerSubmissionDto;
import com.test.mls.model.SurveyDTO;
import com.test.mls.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/surveys")
public class SurveyController {
    @Autowired
    private SurveyService surveyService;

    @PostMapping
    public ResponseEntity<SurveyDTO> createSurvey(@RequestBody SurveyDTO survey) {
        SurveyDTO createdSurvey = surveyService.createSurvey(survey);
        return new ResponseEntity<>(createdSurvey, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SurveyDTO> getSurvey(@PathVariable Long id) {
        SurveyDTO survey = surveyService.getSurvey(id);
        return new ResponseEntity<>(survey, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<SurveyDTO>> getAllSurveys() {
        List<SurveyDTO> surveys = surveyService.getAllSurveys();
        return new ResponseEntity<>(surveys, HttpStatus.OK);
    }

    @PostMapping("/submit-answers")
    public ResponseEntity<Void> submitSurveyAnswers(@RequestBody AnswerSubmissionDto answerSubmissionDto) {
        surveyService.submitSurveyAnswers(answerSubmissionDto);
        return ResponseEntity.ok().build();
    }
}
