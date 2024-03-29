package com.mathematics.service;

import android.annotation.SuppressLint;
import android.content.Context;

import com.mathematics.dao.QuizDAO;
import com.quizwork.Question;
import com.quizwork.Quiz;
import com.quizwork.User;
import com.quizwork.ValidationException;
import com.mathematics.model.WithContext;

import java.util.List;

public class QuizService extends WithContext {
	@SuppressLint("StaticFieldLeak")
	private static QuizService service;

	private QuizService(Context context) {
		super(context);
	}

	public Quiz create(Quiz quiz) throws ValidationException {
		if (quiz.getQuestions() == null) {
			if (quiz.getName() == null || quiz.getName().trim().isEmpty())
				throw new ValidationException("Quiz Name is required");
			if (quiz.getStart() == null)
				throw new ValidationException("Start Date is required");
			if (quiz.getEnd() == null)
				throw new ValidationException("End Date is required");
			if (quiz.getCategory().getId() == 0)
				quiz.setCategory(CategoryService.getInstance(context).create(quiz.getCategory()));

			quiz.setCode(quiz.isOpen() ? null : (int) (Math.random() * 99999 + 1));
			return null;
		} else {
			if (quiz.getQuestions().size() == 0)
				throw new ValidationException("At least one Question required");
			QuizDAO.getInstance(context).beginTransaction();
			try {
				QuizDAO.getInstance(context).create(quiz);
				for (Question q : quiz.getQuestions()) {
					q.setQuiz(quiz);
					QuestionService.getInstance(context).create(q);
				}
				QuizDAO.getInstance(context).setTransactionSuccessful();
			} finally {
				QuizDAO.getInstance(context).endTransaction();
			}
			return quiz;
		}
	}

	public List<Quiz> findAllByUser(User user) throws ValidationException {
		if (user == null)
			throw new ValidationException("Invalid user");

		return QuizDAO.getInstance(context).findAllByUser(user);
	}

	public Quiz findByCode(String code) throws ValidationException {
		if (code == null || code.trim().isEmpty())
			throw new ValidationException("Quiz code is required");
		try {
			Integer.parseInt(code);
		} catch (Exception e) {
			throw new ValidationException("Quiz code must be a text");
		}

		return QuizDAO.getInstance(context).findByCode(code);
	}

	public List<Quiz> findAllByName(String text) throws ValidationException {
		if (text == null || text.trim().isEmpty())
			throw new ValidationException("You need a Text to search");

		return QuizDAO.getInstance(context).findAllByName(text);
	}

	public static QuizService getInstance(Context context) {
		if (service == null)
			service = new QuizService(context);
		return service;
	}
}
