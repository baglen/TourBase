package com.example.tourbase;

import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class Main extends AppCompatActivity {
    private static long back_pressed;
    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Toast.makeText(getBaseContext(), "Нажмите ещё раз Назад для выхода", Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
    }
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDBHelper = new DatabaseHelper(this);
        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }
        try {
            mDB = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }
        TextView textViewLogin = findViewById(R.id.userLogin);
        Button buttonLogout = findViewById(R.id.button_logout);
        Button buttonTours = findViewById(R.id.button_tours);
        Button buttonHotels = findViewById(R.id.button_hotels);
        Button buttonUsers = findViewById(R.id.button_users);
        Button buttonCountries = findViewById(R.id.button_countries);
        textViewLogin.setText("@"+getIntent().getStringExtra("login"));


        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main.this, Auth.class);
                startActivity(intent);
                finish();
            }
        });
        buttonTours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main.this, Tours.class);
                startActivity(intent);
            }
        });
        buttonHotels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main.this, Hotels.class);
                startActivity(intent);
            }
        });
        buttonUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main.this, Users.class);
                startActivity(intent);
            }
        });
        buttonCountries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main.this, Countries.class);
                startActivity(intent);
            }
        });
    }
}

