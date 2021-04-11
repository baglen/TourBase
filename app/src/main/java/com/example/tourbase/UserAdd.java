package com.example.tourbase;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
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

import java.io.IOException;

public class UserAdd extends AppCompatActivity {
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorUser));
        setContentView(R.layout.activity_user_add);
        final EditText txtFirstName = findViewById(R.id.editTextFirstName);
        final EditText txtLastName = findViewById(R.id.editTextLastName);
        final EditText txtEmail = findViewById(R.id.editTextEmail);
        final EditText txtLogin = findViewById(R.id.editTextLogin);
        Button saveButton = findViewById(R.id.buttonSave);
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
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Utils.empty(txtEmail.getText().toString()) && !Utils.empty(txtFirstName.getText().toString()) &&
                        !Utils.empty(txtLastName.getText().toString()) &&
                        !Utils.empty(txtLogin.getText().toString())) {
                    if (Utils.isValidEmailAddress(txtEmail.getText().toString()) == true) {
                        DialogInterface.OnClickListener myClickListener = new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case Dialog.BUTTON_POSITIVE:
                                        try {
                                            String addQuery = "INSERT INTO user VALUES " +
                                                    "('" + txtLogin.getText().toString() + "', 'password', '" + txtFirstName.getText().toString() + "', '"
                                                    + txtLastName.getText().toString() + "', '" + txtEmail.getText().toString() + "')";
                                            mDB.execSQL(addQuery);
                                            Toast.makeText(UserAdd.this, "Данные успешно сохранены. Пароль по умолчанию: password", Toast.LENGTH_SHORT).show();
                                            Users.adapter.clear();
                                            Cursor cursor = mDB.rawQuery("SELECT Login FROM user", null);
                                            cursor.moveToFirst();
                                            for (int i = 0; i < cursor.getCount(); i++) {
                                                Users.adapter.add(cursor.getString(cursor.getColumnIndex("Login")));
                                                cursor.moveToNext();
                                            }
                                            cursor.close();
                                            Users.adapter.notifyDataSetChanged();
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
                        AlertDialog.Builder submitUpdate = new AlertDialog.Builder(UserAdd.this);
                        submitUpdate.setTitle("Сохранение");
                        submitUpdate.setMessage("Сохранить данные?");
                        submitUpdate.setPositiveButton("Да", myClickListener);
                        submitUpdate.setNegativeButton("Нет", myClickListener);
                        submitUpdate.create();
                        submitUpdate.show();
                    }
                    else
                    {
                        Toast.makeText(UserAdd.this, "Введите Email в верном формате", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(UserAdd.this, "Не все данные заполены",Toast.LENGTH_SHORT).show();
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserAdd.this.finish();
            }
        });
    }
}
