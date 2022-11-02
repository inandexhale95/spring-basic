package com.example.quiz.service;

import com.example.quiz.entity.Quiz;
import com.example.quiz.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class QuizServiceImpl implements QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Override
    public Iterable<Quiz> selectAll() {
        return quizRepository.findAll();
    }

    @Override
    public Optional<Quiz> selectOneById(Integer id) {
        return quizRepository.findById(id);
    }

    @Override
    public Optional<Quiz> selectOneRandomQuiz() {
        Integer randId = quizRepository.getRandomId();

        if (randId == null) {
            return Optional.empty();
        }

        return quizRepository.findById(randId);
    }

    @Override
    public Boolean checkQuiz(Integer id, Boolean myAnswer) {
        Boolean check = false;

        Optional<Quiz> optQuiz = quizRepository.findById(id);

        if (optQuiz.isPresent()) {
            Quiz quiz = optQuiz.get();

            if (quiz.getAnswer().equals(myAnswer)) {
                check = true;
            }
        }

        return check;
    }

    @Override
    public void insertQuiz(Quiz quiz) {
        quizRepository.save(quiz);
    }

    @Override
    public void updateQuiz(Quiz quiz) {
        quizRepository.save(quiz);
    }

    @Override
    public void deleteQuizById(Integer id) {
        quizRepository.deleteById(id);
    }
}
