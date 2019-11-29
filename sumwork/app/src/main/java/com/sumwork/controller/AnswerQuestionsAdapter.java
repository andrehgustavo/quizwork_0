package com.sumwork.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.sumwork.R;
import com.sumwork.model.NumericAnswer;
import com.quizwork.QuizAnswer;
import com.quizwork.QuestionAnswer;
import com.quizwork.Question;
import com.quizwork.Quiz;
import com.quizwork.User;

public class AnswerQuestionsAdapter extends BaseAdapter {
    private QuizAnswer quizAnswer;
    private LayoutInflater inflater;
    private int itemBackgroundResource;
    private EditText userAnswer;
    private QuestionAnswer questionAnswer;

    AnswerQuestionsAdapter(Context context, Quiz quiz, User user) {
        this.inflater = LayoutInflater.from(context);
        this.quizAnswer = new QuizAnswer(quiz, user);

        for (Question question : quiz.getQuestions()) {
            quizAnswer.getQuestionAnswers().add(new QuestionAnswer(quizAnswer, question));
        }
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        itemBackgroundResource = outValue.resourceId;

        ((EditText) userAnswer.findViewById(R.id.answer_question_input)).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Object[] item = (Object[]) v.getTag();
                if (!hasFocus) {
                    {
                        ((QuestionAnswer) item[0]).setAnswer(new NumericAnswer(Double.valueOf(userAnswer.getText().toString()), questionAnswer.getQuestion()));
                    }
                }
            }
        });
    }

    @Override
    @SuppressLint("ViewHolder")
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.item_answer_question, viewGroup, false);
        userAnswer = v.findViewById(R.id.answer_question_input);
        questionAnswer = quizAnswer.getQuestionAnswers().get(i);
        ((TextView) v.findViewById(R.id.answer_question_text)).setText(questionAnswer.getQuestion().getText());


        return v;
    }

    @Override
    public int getCount() {
        return quizAnswer.getQuestionAnswers().size();
    }

    @Override
    public Object getItem(int i) {
        return quizAnswer.getQuestionAnswers().get(i);
    }

    @Override
    public long getItemId(int i) {
        return quizAnswer.getQuestionAnswers().get(i).getId();
    }

    QuizAnswer getQuizAnswer() {

        return quizAnswer;
    }

//    @Override
//    public void onClick(View view) {
//        Object[] item = (Object[]) view.getTag();
//        ((QuestionAnswer) item[0]).setAnswer(new NumericAnswer(Double.valueOf(userAnswer.getText().toString()), questionAnswer.getQuestion()));
//        ViewGroup v = (ViewGroup) item[2];
//        for (int i = 1; i < v.getChildCount(); ++i) {
//            ((CheckedTextView) v.getChildAt(i)).setChecked(false);
//        }
//        ((CheckedTextView) view).setChecked(true);
//    }

}
