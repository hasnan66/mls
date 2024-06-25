package com.test.mls.repository;

import com.test.mls.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
