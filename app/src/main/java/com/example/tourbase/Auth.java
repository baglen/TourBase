package com.example.tourbase;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;

public class Auth extends AppCompatActivity {
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDB;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
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
        Button loginButton = findViewById(R.id.login_button);
        final EditText login = findViewById(R.id.inputLogin);
        final EditText password = findViewById(R.id.inputPassword);
        login.setText("admin");
        password.setText("admin");


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginQuery = "SELECT * FROM user WHERE Login = ? AND Password = ?";
                String[] selectionArgs = {login.getText().toString(), password.getText().toString()};
                Cursor cursor = mDB.rawQuery(loginQuery, selectionArgs);

                if(cursor.getCount() == 1)
                {
                    //(cursor.getString(0), cursor.getString(2), cursor.getString(3));
                    Toast.makeText(Auth.this, "Успешный вход!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Auth.this, Main.class);
                    cursor.moveToNext();
                    intent.putExtra("login", cursor.getString(cursor.getColumnIndex("Login")));
                    intent.putExtra("firstName", cursor.getString(cursor.getColumnIndex("FirstName")));
                    intent.putExtra("lastName", cursor.getString(cursor.getColumnIndex("LastName")));
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(Auth.this, "Неверный логин или пароль", Toast.LENGTH_SHORT).show();
                }
                cursor.close();
            }
        });

    }
}




