package com.example.trivia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.example.trivia.controller.AppController;
import com.example.trivia.data.AnswerListAsyncResponse;
import com.example.trivia.data.QuestionBank;
import com.example.trivia.model.Question;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView questiontextview;
    private TextView questioncountertextview;
    private TextView scoretextview;
    private TextView LastGameScore;
    private Button TrueButton;
    private Button FalseButton;
    private ImageButton NextButton;
    private ImageButton PrevButton;


    private int currentQuestionIndex = 0;
    private int score = 0 ;
    private List<Question> questionList ;


    private static final String Messasge_ID = "myscore";






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NextButton = findViewById(R.id.next_button);
        PrevButton = findViewById(R.id.prev_button);
        TrueButton = findViewById(R.id.true_button);
        FalseButton = findViewById(R.id.false_button);

        questioncountertextview = findViewById(R.id.counter_text);
        questiontextview        = findViewById(R.id.question_textview);
        scoretextview           = findViewById(R.id.scoretext);
        LastGameScore           = findViewById(R.id.LastGameScore);


        NextButton.setOnClickListener(this);
        PrevButton.setOnClickListener(this);
        TrueButton.setOnClickListener(this);
        FalseButton.setOnClickListener(this);

        SharedPreferences getshare = getSharedPreferences(Messasge_ID,MODE_PRIVATE);


        int LastScore = getshare.getInt("score",1000);



        LastGameScore.setText("Last Game Score : " + LastScore);









         new QuestionBank().getQuestions(new AnswerListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {



             questionList = questionArrayList;


               questiontextview.setText(questionArrayList.get(currentQuestionIndex).getAnswer());
               questioncountertextview.setText(currentQuestionIndex + " / " + questionArrayList.size());

              //  Log.d("JSON", "processFinished: "+ questionArrayList);

            }
        });

     //  Log.d("Main", "onCreate: " + questionList);


    }

    @Override
    public void onClick(View view) {



        switch (view.getId()){

            case R.id.prev_button:
                if(currentQuestionIndex > 0) {
                    currentQuestionIndex = (currentQuestionIndex-1)% questionList.size();
                    updatequestion();
                }else {currentQuestionIndex = 0; };

                break;

            case R.id.next_button:
                currentQuestionIndex = (currentQuestionIndex+1)% questionList.size();
                updatequestion();

                break;

            case R.id.false_button:
                checkAnswer(false);
                updatequestion();

                break;

            case R.id.true_button:
                checkAnswer(true);
                updatequestion();

                break;

        }

    }

    private void checkAnswer(boolean userChooseCorrect) {

        SharedPreferences sharedPreferences = getSharedPreferences(Messasge_ID,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();


        boolean answerIsTrue = questionList.get(currentQuestionIndex).isAnswerTrue();

        int toastMessageID  ;

        if(userChooseCorrect == answerIsTrue){

            shakeAnimation();
            updatequestion();
            toastMessageID = R.string.correct_answer;

                score++;
                scoretextview.setText("score : "+ (score));
                editor.putInt("score", score);
                editor.apply();




        }else {
            if(score > 0){
                score--;
                scoretextview.setText("score : "+ (score));
                editor.putInt("score", score);
                editor.apply();

            }
            toastMessageID = R.string.wrong_answer;
        }

        Toast.makeText(this, toastMessageID, Toast.LENGTH_SHORT).show();


    }

    private void updatequestion() {

        String question = questionList.get(currentQuestionIndex).getAnswer();

        CardView cardView = findViewById(R.id.cardView);

        cardView.setCardBackgroundColor(Color.WHITE);
        questiontextview.setText(question);
        questioncountertextview.setText(currentQuestionIndex + " / " + questionList.size());



    }

    private void fadeView(){
        CardView cardView = findViewById(R.id.cardView);




    }

    private void shakeAnimation(){

        Animation shake = AnimationUtils.loadAnimation(MainActivity.this,R.anim.shake_animation );

        CardView cardView = findViewById(R.id.cardView);
        cardView.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
               // cardView.setCardBackgroundColor(Color.WHITE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}