package com.example.tourbase;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;

public class Users extends AppCompatActivity {
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDB;
    public static ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        final ListView usersList = findViewById(R.id.usersList);
        EditText searchUser = findViewById(R.id.search_user);
        ImageButton buttonAddUser = findViewById(R.id.buttonAddUser);
        final ArrayList<String> userLogins = new ArrayList<>();
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

        Cursor cursor = mDB.rawQuery("SELECT Login FROM user", null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            userLogins.add(cursor.getString(cursor.getColumnIndex("Login")));
            cursor.moveToNext();
        }
        cursor.close();
        adapter = new ArrayAdapter<>(this, R.layout.list_item_user, R.id.text_view_user_name, userLogins);
        usersList.setAdapter(adapter);

        searchUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Users.this, UserProfile.class);
                intent.putExtra("login", (String)parent.getItemAtPosition(position));
                Users.this.startActivity(intent);
            }
        });
        buttonAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Users.this, UserAdd.class);
                Users.this.startActivity(intent);
            }
        });
    }
}
