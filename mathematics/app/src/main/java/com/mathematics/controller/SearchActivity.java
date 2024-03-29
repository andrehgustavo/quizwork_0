package com.mathematics.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mathematics.R;
import com.quizwork.Quiz;
import com.quizwork.User;
import com.quizwork.ValidationException;
import com.mathematics.service.QuizService;

public class SearchActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
	private User user;
	private ArrayAdapter<Quiz> arrayAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		user = (User) getIntent().getSerializableExtra("user");
		arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
		ListView listView = findViewById(R.id.search_list);
		listView.setAdapter(arrayAdapter);
		listView.setOnItemClickListener(this);
	}

	public void searchCode(View view) {
		String code = ((EditText) findViewById(R.id.search_code)).getText().toString();
		try {
			Quiz quiz = QuizService.getInstance(this).findByCode(code);
			if (quiz == null)
				Toast.makeText(this, "No quiz found", Toast.LENGTH_LONG).show();
			else
				startActivity(new Intent(view.getContext(), AnswerActivity.class)
						.putExtra("user", user)
						.putExtra("quiz", quiz));
		} catch (ValidationException msg) {
			msg.show(this);
		}
	}

	public void searchText(View view) {
		String text = ((EditText) findViewById(R.id.search_text)).getText().toString();
		try {
			arrayAdapter.clear();
			arrayAdapter.addAll(QuizService.getInstance(this).findAllByName(text));
			if (arrayAdapter.isEmpty())
				Toast.makeText(this, "No quiz found", Toast.LENGTH_LONG).show();
		} catch (ValidationException msg) {
			msg.show(this);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
		startActivity(new Intent(view.getContext(), AnswerActivity.class)
				.putExtra("user", user)
				.putExtra("quiz", arrayAdapter.getItem(i)));
	}
}
