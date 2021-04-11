package com.example.tourbase;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Countries extends AppCompatActivity implements DataLoad{
    private ArrayList<String> countryList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CountriesAdapter countriesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countries);
        EditText searchCountry = findViewById(R.id.search_country);
        recyclerView = findViewById(R.id.countriesList);
        GetCountries getCountries = new GetCountries();
        getCountries.execute(this);
        /*searchCountry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/
    }
    @Override
    public void setAnswer(List<String> list)
    {
        countriesAdapter = new CountriesAdapter(Countries.this, list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Countries.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(countriesAdapter);
    }
}

