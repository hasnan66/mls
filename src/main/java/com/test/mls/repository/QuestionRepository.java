package com.test.mls.repository;

import com.test.mls.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("SELECT q FROM Question q WHERE q.survey.id = :surveyId")
    List<Question> findBySurveyId(@Param("surveyId") Long surveyId);
}
