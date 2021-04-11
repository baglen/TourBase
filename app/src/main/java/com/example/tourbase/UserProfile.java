package com.example.tourbase;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.google.android.material.textfield.TextInputLayout;


import java.io.IOException;

public class UserProfile extends AppCompatActivity{
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this ,R.color.colorUser));
        setContentView(R.layout.activity_user_profile);
        TextView profileLogin = findViewById(R.id.login_txt);
        profileLogin.setText("@"+getIntent().getStringExtra("login"));
        final EditText txtFirstName = findViewById(R.id.editTextFirstName);
        final EditText txtLastName = findViewById(R.id.editTextLastName);
        final EditText txtEmail = findViewById(R.id.editTextEmail);
        final EditText txtLogin = findViewById(R.id.editTextLogin);
        ImageButton deleteButton = findViewById(R.id.button_delete);
        final Button updateButton = findViewById(R.id.buttonUpdate);
        ImageButton backButton = findViewById(R.id.button_back);
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
        String userQuery = "SELECT * FROM user WHERE Login = ?";
        String[] selectionArgs = {getIntent().getStringExtra("login")};
        final Cursor cursor = mDB.rawQuery(userQuery, selectionArgs);
        cursor.moveToNext();
        txtFirstName.setText(cursor.getString(cursor.getColumnIndex("FirstName")));
        txtLastName.setText(cursor.getString(cursor.getColumnIndex("LastName")));
        txtEmail.setText(cursor.getString(cursor.getColumnIndex("Email")));
        txtLogin.setText(cursor.getString(cursor.getColumnIndex("Login")));
        cursor.close();
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Utils.empty(txtEmail.getText().toString()) && !Utils.empty(txtFirstName.getText().toString()) &&
                        !Utils.empty(txtLastName.getText().toString()) &&
                        !Utils.empty(txtLogin.getText().toString())) {
                    if (Utils.isValidEmailAddress(txtEmail.getText().toString()) == true) {
                        OnClickListener myClickListener = new OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case Dialog.BUTTON_POSITIVE:
                                        try {
                                            ContentValues cv = new ContentValues();
                                            cv.put("Login", txtLogin.getText().toString());
                                            cv.put("FirstName", txtFirstName.getText().toString());
                                            cv.put("LastName", txtLastName.getText().toString());
                                            cv.put("Email", txtEmail.getText().toString());
                                            mDB.update("user", cv, "Login = ?", new String[]{getIntent().getStringExtra("login")});
                                            Toast.makeText(UserProfile.this, "Данные успешно обновлены", Toast.LENGTH_SHORT).show();
                                            Utils.updateUsersList(mDB);
                                        } catch (Exception ex) {
                                            Log.e("userUpdate", ex.getMessage());
                                        }
                                        finish();
                                        break;
                                    case Dialog.BUTTON_NEGATIVE:
                                        finish();
                                        break;
                                }
                            }
                        };

                        AlertDialog.Builder submitUpdate = new AlertDialog.Builder(UserProfile.this);
                        submitUpdate.setTitle("Сохранение");
                        submitUpdate.setMessage("Сохранить данные?");
                        submitUpdate.setPositiveButton("Да", myClickListener);
                        submitUpdate.setNegativeButton("Нет", myClickListener);
                        submitUpdate.create();
                        submitUpdate.show();
                    }
                    else
                    {
                        Toast.makeText(UserProfile.this, "Введите Email в верном формате", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(UserProfile.this, "Не все данные заполенены", Toast.LENGTH_SHORT).show();
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserProfile.this.finish();
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickListener myClickListener = new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case Dialog.BUTTON_POSITIVE:
                                try {
                                    String userDeleteQuery = "DELETE FROM user WHERE Login = '" + getIntent().getStringExtra("login")+"'";
                                    mDB.execSQL(userDeleteQuery);
                                    Utils.updateUsersList(mDB);
                                    Toast.makeText(UserProfile.this, "Пользователь удален", Toast.LENGTH_SHORT).show();
                                    finish();
                                } catch (Exception ex) {
                                    Log.e("userUpdate", ex.getMessage());
                                }
                                finish();
                                break;
                            case Dialog.BUTTON_NEGATIVE:
                                finish();
                                break;
                        }
                    }
                };
                AlertDialog.Builder submitUpdate = new AlertDialog.Builder(UserProfile.this);
                submitUpdate.setTitle("Удаление");
                submitUpdate.setMessage("Удалить пользователя?");
                submitUpdate.setPositiveButton("Да", myClickListener);
                submitUpdate.setNegativeButton("Нет", myClickListener);
                submitUpdate.create();
                submitUpdate.show();
            }
        });
    }
}
