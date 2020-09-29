package com.nkh.androidvnfreakingmath;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Random;

public class GameActivity extends AppCompatActivity implements Runnable {
    private TextView tvName, tvScore, tvNum1, tvNum2, tvResult;
    private ProgressBar progressBar;
    private Button btnYes, btnNo;
    private int score;
    private int time = 5000;
    private int result, fakeResult;
    private Thread thread, thread1;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                setData();
            } else if (msg.what == 2) {
                gameOver();
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        tvName = findViewById(R.id.tv_name);
        tvNum1 = findViewById(R.id.tv_num1);
        tvNum2 = findViewById(R.id.tv_num2);
        tvResult = findViewById(R.id.tv_result);
        tvScore = findViewById(R.id.tv_score);
        progressBar = findViewById(R.id.pb_time);
        btnYes = findViewById(R.id.btn_yes);
        btnNo = findViewById(R.id.btn_no);
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        tvName.setText(name);
        progressBar.setProgress(100);
        thread = new Thread(GameActivity.this);
        thread.start();
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fakeResult != result) {

                    continueGame();
                    time = 5000;

                } else {
                    gameOver();

                }
            }

        });
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fakeResult == result) {
                    continueGame();
                    time = 5000;

                } else {
                    gameOver();

                }
            }
        });

    }

    private void continueGame() {
        score++;
        tvScore.setText(score + "");

    }

    private int getRandomNumber() {
        int result = 0;
        Random r = new Random();
        result = r.nextInt(100) + 1;
        return result;
    }

    private int trueAndFalse() {
        int result = 0;
        Random r = new Random();
        result = r.nextInt(2);
        return result;
    }

    private void setData() {

        int a = getRandomNumber();
        int b = getRandomNumber();
        tvNum1.setText(a + "");
        tvNum2.setText(b + "");
        result = a + b;
        if (trueAndFalse() == 0) {
            fakeResult = result;
        } else {
            fakeResult = getRandomNumber() + result;
        }
        tvResult.setText(fakeResult + "");


    }

    private void gameOver() {
        final Dialog dialog = new Dialog(GameActivity.this);
        dialog.setContentView(R.layout.gameover_dialog);
        dialog.getWindow().setLayout(1000, 800);
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        Button btnGameOver = dialog.findViewById(R.id.btn_start_over);
        TextView tvScoreOver = dialog.findViewById(R.id.tv_score_over);
        tvScoreOver.setText(String.valueOf(score));
        btnGameOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                setData();
                score = 0;
                time = 10000;
                tvScore.setText(String.valueOf(score));
                thread1 = new Thread(GameActivity.this);
                thread1.start();
            }
        });
        Button btnHome = dialog.findViewById(R.id.btn_home);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GameActivity.this, MainActivity.class));
            }
        });
    }

    @Override
    public void run() {
        while (time > 0) {
            if (time == 5000) {
                handler.sendEmptyMessage(1);
            }
            time -= 10;
            try {
                Thread.sleep(100);
                progressBar.post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setProgress((int) (time * 100 / 5000));
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();

            }
        }


        handler.sendEmptyMessage(2);

    }


}