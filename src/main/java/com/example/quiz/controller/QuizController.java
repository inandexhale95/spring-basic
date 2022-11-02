package com.example.quiz.controller;

import com.example.quiz.entity.Quiz;
import com.example.quiz.form.QuizForm;
import com.example.quiz.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/quiz")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @ModelAttribute
    public QuizForm setUpForm() {
        QuizForm quizForm = new QuizForm();
        quizForm.setAnswer(true);
        return quizForm;
    }

    @GetMapping("")
    public String showList(QuizForm quizForm, Model model) {
        quizForm.setNewQuiz(true);

        Iterable<Quiz> list = quizService.selectAll();

        model.addAttribute("list", list);
        model.addAttribute("title", "등록 폼");
        return "crud";
    }

    @PostMapping("/insert")
    public String insert(@Validated QuizForm quizForm, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        Quiz quiz = new Quiz();
        quiz.setQuestion(quizForm.getQuestion());
        quiz.setAnswer(quizForm.getAnswer());
        quiz.setAuthor(quizForm.getAuthor());

        if (!bindingResult.hasErrors()) {
            quizService.insertQuiz(quiz);
            redirectAttributes.addFlashAttribute("complete", "등록이 완료되었습니다.");
            return "redirect:/quiz";
        } else {
            return showList(quizForm, model);
        }
    }

    @GetMapping("/{id}")
    public String showUpdate(QuizForm quizForm, @PathVariable Integer id, Model model) {
        Optional<Quiz> optQuiz = quizService.selectOneById(id);

        Optional<QuizForm> optQuizForm = optQuiz.map(q -> makeQuizForm(q));

        if (optQuizForm.isPresent()) {
            quizForm = optQuizForm.get();
        }

        makeUpdateModel(quizForm, model);
        return "crud";
    }

    private void makeUpdateModel(QuizForm quizForm, Model model) {
        model.addAttribute("id", quizForm.getId());
        quizForm.setNewQuiz(false);
        model.addAttribute("quizForm", quizForm);
        model.addAttribute("title", "변경 폼");
    }

    private QuizForm makeQuizForm(Quiz quiz) {
        QuizForm form = new QuizForm();
        form.setId(quiz.getId());
        form.setQuestion(quiz.getQuestion());
        form.setAnswer(quiz.getAnswer());
        form.setAuthor(quiz.getAuthor());
        form.setNewQuiz(false);
        return form;
    }

    @PostMapping("/update")
    public String update(@Validated QuizForm quizForm, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        Quiz quiz = makeQuiz(quizForm);

        if (!bindingResult.hasErrors()) {
            quizService.updateQuiz(quiz);

            redirectAttributes.addFlashAttribute("complete", "변경이 완료되었습니다.");
            return "redirect:/quiz/" + quiz.getId();
        } else {
            makeUpdateModel(quizForm, model);
            return "crud";
        }
    }

    private Quiz makeQuiz(QuizForm quizForm) {
        Quiz quiz = new Quiz();
        quiz.setId(quizForm.getId());
        quiz.setQuestion(quizForm.getQuestion());
        quiz.setAnswer(quizForm.getAnswer());
        quiz.setAuthor(quizForm.getAuthor());
        return quiz;
    }

    @PostMapping("/delete")
    public String delete(@RequestParam Integer id, Model model, RedirectAttributes redirectAttributes) {
        quizService.deleteQuizById(id);

        redirectAttributes.addFlashAttribute("complete", "삭제가 완료되었습니다.");
        return "redirect:/quiz";
    }

    @GetMapping("/play")
    public String showQuiz(QuizForm quizForm, Model model) {
        Optional<Quiz> optQuiz = quizService.selectOneRandomQuiz();

        if (optQuiz.isPresent()) {
            Optional<QuizForm> optQuizForm = optQuiz.map(q -> makeQuizForm(q));
            quizForm = optQuizForm.get();
        } else {
            model.addAttribute("msg", "등록된 문제가 없습니다.");
            return "play";
        }

        model.addAttribute("quizForm", quizForm);
        return "play";
    }

    @PostMapping("/check")
    public String checkQuiz(QuizForm quizForm, @RequestParam Boolean answer, Model model) {
        if (quizService.checkQuiz(quizForm.getId(), answer)) {
            model.addAttribute("msg", "정답입니다.");
        } else {
            model.addAttribute("msg", "오답입니다.");
        }
        return "answer";
    }
}
