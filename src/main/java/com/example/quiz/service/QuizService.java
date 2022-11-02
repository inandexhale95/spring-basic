package com.example.quiz.service;

import com.example.quiz.entity.Quiz;

import java.util.Optional;

public interface QuizService {
    Iterable<Quiz> selectAll();

    Optional<Quiz> selectOneById(Integer id);

    Optional<Quiz> selectOneRandomQuiz();

    Boolean checkQuiz(Integer id, Boolean myAnswer);

    void insertQuiz(Quiz quiz);

    void updateQuiz(Quiz quiz);

    void deleteQuizById(Integer id);
}
