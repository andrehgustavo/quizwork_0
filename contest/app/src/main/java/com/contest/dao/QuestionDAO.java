package com.contest.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.contest.model.ObjectiveQuestion;
import com.contest.model.Option;
import com.quizwork.Question;

import static com.contest.dao.DAO.*;

public class QuestionDAO extends WithDAO {
	private static QuestionDAO questionDAO;

	private QuestionDAO(Context context) {
		super(context);
	}

	public Question create(Question question) {
		SQLiteDatabase db = dao.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(QUESTION_TEXT, question.getText());
		values.put(QUESTION_QUIZ, question.getQuiz().getId());
		question.setId(db.insert(QUESTION_TABLE, null, values));

		for (Option op : ((ObjectiveQuestion) question).getOptions()) {
			values.clear();
			values.put(OPTION_TEXT, op.getText());
			values.put(OPTION_QUESTION, question.getId());
			op.setId(db.insert(OPTION_TABLE, null, values));
		}

		values.clear();
		values.put(QUESTION_OPTION, question.getCorrect().getId());
		db.update(QUESTION_TABLE, values, QUESTION_ID + "=" + question.getId(), null);
		return question;
	}

	public static QuestionDAO getInstance(Context context) {
		if (questionDAO == null)
			questionDAO = new QuestionDAO(context);
		return questionDAO;
	}
}
