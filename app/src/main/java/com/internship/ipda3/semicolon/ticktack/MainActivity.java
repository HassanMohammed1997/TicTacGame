package com.internship.ipda3.semicolon.ticktack;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.rest_button)
    Button restButton;

    //create 2D array of buttons.
    private Button[][] buttons = new Button[3][3];

    private int count;
    private boolean isPlayer1Turn = true;

    private int player1Point;
    private int player2Point;

    @BindView(R.id.player_1)
    TextView player1Tv;
    @BindView(R.id.player_2)
    TextView player2Tv;

    TextView textView;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        dialog = new Dialog(this);
        View customView = LayoutInflater.from(this).inflate(R.layout.winner_dialog, null);
        dialog.setContentView(customView);
        dialog.setCancelable(true);

         textView = customView.findViewById(R.id.winner_text);

        //nested loop to assign the buttons with findViewById dynamically and set onClick listener on them.
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons.length; j++) {
                String buttonId = "button_" + i + j;
                int id = getResources().getIdentifier(buttonId, "id", getPackageName());
                buttons[i][j] = findViewById(id);
                buttons[i][j].setOnClickListener(this);

            }
        }
    }

    @Override
    public void onClick(View v) {
        //check if clicked button's text is empty or not.
        //if true, return nothing.
        if (!((Button) v).getText().toString().equals("")) {
            return;
        }

        //if player 1 click on any button, then set text to button ("X").
        //otherwise, set text to clicked button ("O").
        if (isPlayer1Turn) {
            ((Button) v).setText("X");
            ((Button)v).setBackgroundColor(getResources().getColor(R.color.colorAccent));
        } else {
            ((Button) v).setText("O");
            ((Button)v).setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        //increase count by 1.
        count++;


        if (checkForWin()) {
            if (isPlayer1Turn) {
                player1Win();
            }else{
                player2Win();
            }
        }else if (count == 9){
            draw();
        }else{
            isPlayer1Turn = !isPlayer1Turn;
        }

    }

    private void draw() {
        Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show();
        reset();
    }

    private void reset() {
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                buttons[i][j].setText("");
                buttons[i][j].setBackgroundColor(Color.GRAY);

            }
        }

        count = 0;
        isPlayer1Turn = true;
    }

    private void player2Win() {
        player2Point++;
        textView.setText("Player 2 wins!");
        dialog.show();
        updatePointsText();
        reset();
    }

    @SuppressLint("DefaultLocale")
    private void updatePointsText() {
        player1Tv.setText(String.format("%d", player1Point));
        player2Tv.setText(String.format("%d", player2Point));
    }

    private void player1Win() {
        player1Point++;
        textView.setText("Player 1 wins!");
        dialog.show();
        updatePointsText();
        reset();
    }

    @OnClick(R.id.rest_button)
    public void onViewClicked() {
        player1Point = 0;
        player2Point = 0;
        reset();
        updatePointsText();
    }

    public boolean checkForWin() {
        //create 2D String array.
        String[][] field = new String[3][3];

        //nested loop to get the button's text from button's array.
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field.length; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }

        for (int i = 0; i < field.length; i++) {
            /**
             * {[0,0], [0,1], [0,2]
             * [1,0], [1,1], [1,2]
             * [2,0], [2,1], [2,2]}
             */
            if (field[i][0].equals(field[i][1]) && field[i][0].equals(field[i][2]) && !field[i][0].equals("")) {
                return true;
            }

        }

        for (int i = 0; i < field.length; i++) {
            if (field[0][i].equals(field[1][i]) && field[0][i].equals(field[2][i]) && !field[0][i].equals("")) {
                return true;
            }

        }

        if (field[0][0].equals(field[1][1]) && field[0][0].equals(field[2][2]) && !field[0][0].equals("")) {
            return true;
        }

        if (field[0][2].equals(field[1][1]) && field[0][2].equals(field[2][2]) && !field[0][2].equals("")) {
            return true;
        }

        return false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //save data when rotation.
        outState.putInt("count", count);
        outState.putInt("player1Point", player1Point);
        outState.putInt("player2Point", player2Point);
        outState.putBoolean("player1Turn", isPlayer1Turn);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        //restore data when user rotate the screen.
        count = savedInstanceState.getInt("count");
        player1Point = savedInstanceState.getInt("player1Point");
        player2Point = savedInstanceState.getInt("player2Point");
        isPlayer1Turn = savedInstanceState.getBoolean("player1Turn");
    }
}
