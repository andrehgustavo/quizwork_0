package com.sumwork.model;

import com.quizwork.Answer;
import com.quizwork.Question;
import com.quizwork.ValidationException;

import java.io.Serializable;

public class ObjectiveQuestion extends Question implements Serializable {
	private NumericAnswer numericAnswer;

	public ObjectiveQuestion() {}

	public ObjectiveQuestion(String text) {
		this.text = text;
		this.numericAnswer = null;
	}

	public ObjectiveQuestion(long id, String text, Answer correct) {
		this.id = id;
		this.text = text;
		this.correct = correct;
		this.numericAnswer = null;
	}

	@Override
	public void validate() throws ValidationException {
		if (numericAnswer == null)
			throw new ValidationException("At least one Options are required for each Question");
		if (numericAnswer.getNumber() == null)
				throw new ValidationException("A answer is required");

		if (correct == null)
			throw new ValidationException("You must select the Correct numericAnswer for each Question");
	}

	public NumericAnswer getOptions() {
		return numericAnswer;
	}

	public void setOptions(NumericAnswer numericAnswer) {
		this.numericAnswer = numericAnswer;
	}
}
