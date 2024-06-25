package com.test.mls.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurveyDTO {

    private Long id;
    private String name;
    private List<QuestionDTO> questions;
}
