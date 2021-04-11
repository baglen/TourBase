package com.example.tourbase;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

class GetCountries extends AsyncTask<DataLoad,Void, Void> {
    private List<String> countryList = new ArrayList<>();
    private DataLoad dataLoad;
    @Override
    protected Void doInBackground(DataLoad... params){
        dataLoad=params[0];
        try {
            String url = "https://restcountries.eu/rest/v2/all\n";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine = in.readLine();
            in.close();
            JSONArray array = new JSONArray(inputLine);
            for(int i = 0; i < array.length(); i++)
            {
                JSONObject jsonObject = array.getJSONObject(i);
                countryList.add(jsonObject.getString("name"));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void aVoid){
        dataLoad.setAnswer(countryList);
    }
}
