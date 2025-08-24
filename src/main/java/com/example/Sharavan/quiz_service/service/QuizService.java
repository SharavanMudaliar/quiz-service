package com.example.Sharavan.quiz_service.service;


import com.example.Sharavan.quiz_service.dao.QuizDao;
import com.example.Sharavan.quiz_service.feign.QuizInteface;
import com.example.Sharavan.quiz_service.model.QuestionWrapper;

import com.example.Sharavan.quiz_service.model.Quiz;
import com.example.Sharavan.quiz_service.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

//    @Autowired
//    QuestionDao questionDao;

    @Autowired
    QuizDao quizDao;

    @Autowired
    QuizInteface quizInteface;

    public ResponseEntity<String> createQuiz(String category,int numQ, String title) {
        List<Integer> questions = quizInteface.getQuestionForQuiz(category, numQ).getBody();
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestionsIds(questions);
        quizDao.save(quiz);


        return new ResponseEntity<>("Success", HttpStatus.CREATED);

    }


    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {
        Quiz quiz = quizDao.findById(id).get();
        List<Integer> questionIds = quiz.getQuestionsIds();
        ResponseEntity<List<QuestionWrapper>> questions = quizInteface.getQuestionsFromId(questionIds);

        return questions;
    }

    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses) {
        ResponseEntity<Integer> score = quizInteface.getScore(responses);

        return score;

    }
}
