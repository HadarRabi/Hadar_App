package com.example.hw1_hadar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static int[][] stoneMat = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
    private static int[] policeCarArr = {0, 1, 0};

    //car and arrows:
    private AppCompatImageView[] policeCar;
    private AppCompatImageView[] stones;
    private AppCompatImageView[] lives;
    private int currentLives = 3;

    private AppCompatImageView car1;
    private AppCompatImageView car2;
    private AppCompatImageView car3;
    private MaterialButton BTN_left;
    private MaterialButton BTN_right;
    private AppCompatImageView heart1;
    private AppCompatImageView heart2;
    private AppCompatImageView heart3;

    //stone

    private AppCompatImageView stone11;
    private AppCompatImageView stone12;
    private AppCompatImageView stone13;
    private AppCompatImageView stone21;
    private AppCompatImageView stone22;
    private AppCompatImageView stone23;
    private AppCompatImageView stone31;
    private AppCompatImageView stone32;
    private AppCompatImageView stone33;
    private AppCompatImageView stone14;
    private AppCompatImageView stone24;
    private AppCompatImageView stone34;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        views();
        initViews();
        updateStones();

    }

    private void views() {
        car1 = findViewById(R.id.car1);
        car2 = findViewById(R.id.car2);
        car3 = findViewById(R.id.car3);

        heart1 = findViewById(R.id.main_IMG_heart1);
        heart2 = findViewById(R.id.main_IMG_heart2);
        heart3 = findViewById(R.id.main_IMG_heart3);

        BTN_left = findViewById(R.id.main_IMG_left);
        BTN_right = findViewById(R.id.main_IMG_right);

        stone12 = findViewById(R.id.stone12);
        stone22 = findViewById(R.id.stone22);
        stone32 = findViewById(R.id.stone32);
        stone11 = findViewById(R.id.stone11);
        stone21 = findViewById(R.id.stone21);
        stone31 = findViewById(R.id.stone31);
        stone13 = findViewById(R.id.stone13);
        stone23 = findViewById(R.id.stone23);
        stone33 = findViewById(R.id.stone33);
        stone14 = findViewById(R.id.stone14);
        stone24 = findViewById(R.id.stone24);
        stone34 = findViewById(R.id.stone34);

        lives = new AppCompatImageView[]{
                heart1, heart2, heart3,
        };
        policeCar = new AppCompatImageView[]{
                car1, car2, car3,
        };
        stones = new AppCompatImageView[]{
                stone12, stone22, stone32, stone11, stone21, stone31, stone13, stone23, stone33, stone14, stone24, stone34
        };

    }

    private void initViews() {
        BTN_left.setOnClickListener(v -> clicked(0));
        BTN_right.setOnClickListener(v -> clicked(1));
    }


    private int getPoliceCarIndex() {
        for (int i = 0; i < policeCar.length; i++) {
            if (policeCarArr[i] == 1) {
                return i;
            }
        }
        return -1;
    }

    private void clicked(int arrow) {
        int index = getPoliceCarIndex();
        boolean canMoveLeft = index > 0;
        boolean canMoveRight = index < 2;

        if (arrow == 0 && canMoveLeft) {
            updatePoliceCar(arrow, index);
        } else if (arrow == 1 && canMoveRight) {
            updatePoliceCar(arrow, index);
        }
         updateLives();
        // checkIfGameOver();
    }

    private void updatePoliceCar(int arrow, int index) {
        int newIndex = arrow == 0 ? index - 1 : index + 1;
        policeCarArr[index] = 0;
        policeCar[index].setVisibility(View.INVISIBLE);
        policeCarArr[newIndex] = 1;
        policeCar[newIndex].setVisibility(View.VISIBLE);
    }


    private void getRandomNumber() {
        for (int i = 0; i < 3; i++) {
            stoneMat[0][i] = 0;
        }
        Random r = new Random();
        int randomNum = r.nextInt(3);
        stoneMat[0][randomNum] = 1;

    }


    private void updateStonesPositions() {
        for (int i = 0; i < stones.length; i++) {
            stones[i].setVisibility(View.INVISIBLE);
        }

        for (int i = 0; i < stoneMat.length; i++) {
            for (int j = 0; j < stoneMat[i].length; j++) {
                if (stoneMat[i][j] == 1) {
                    int stoneIndex = i * stoneMat[i].length + j;
                    stones[stoneIndex].setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void saveStonesPositions() {
        for (int i = stoneMat.length - 1; i > 0; i--) {
            for (int j = 0; j < stoneMat[i].length; j++) {
                stoneMat[i][j] = stoneMat[i - 1][j];
            }
        }
    }

    private void updateStonesMatrix() {
        saveStonesPositions();
        getRandomNumber();
        updateStonesPositions();
        updateLives();
        // checkIfGameOver();

    }

    private void updateStones() {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                updateStonesMatrix();
                handler.postDelayed(this, 850);
            }
        };
        handler.post(runnable);
    }



    private boolean carCrashedStone() {
        for (int i = 0; i < stoneMat[3].length; i++) {
            if (stoneMat[3][i] == 1 && policeCarArr[i] == 1) {
                if (currentLives >= 1) {
                    lives[currentLives-1].setVisibility(View.INVISIBLE);

                    return true;
                }

            }
        }
        return false;
    }

        private void updateLives() {
            if(currentLives == 0 ){
                currentLives = 3;
                for (int j = 0; j < currentLives; j++){
                    lives[j].setVisibility(View.VISIBLE);
                }
            }

                if (carCrashedStone() == true) {
                    vibCrash();
                lives[currentLives-1].setVisibility(View.INVISIBLE);
                currentLives--;
            }
        }

    private void vibCrash() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(500);
        }
        runOnUiThread(() -> {
            Toast.makeText(MainActivity.this, "Crash", Toast.LENGTH_SHORT).show();
        });
        Log.e("MainActivity", "App crashed");
    }


}
