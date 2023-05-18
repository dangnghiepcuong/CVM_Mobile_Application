package com.example.cvm_mobile_application.data.objects;

import android.os.AsyncTask;

import com.example.cvm_mobile_application.data.SpinnerOption;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class APILocalList extends AsyncTask<String, Void, JSONArray> {
    public static final char PROVINCE = 'p';
    public static final char DISTRICT = 'd';
    public static final char WARD = 'w';

    public List<SpinnerOption> getPovinceList() {
        List<SpinnerOption> list = new ArrayList<>();

        try {
            URL url = new URL("https://provinces.open-api.vn/api/p/");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            //Getting the response code
            int responsecode = conn.getResponseCode();

            if (responsecode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responsecode);
            } else {

                String inline = "";
                Scanner scanner = new Scanner(url.openStream());

                //Write all the JSON data into a string using a scanner
                while (scanner.hasNext()) {
                    inline += scanner.nextLine();
                }

                //Close the scanner
                scanner.close();

                JSONObject jsonProvince = new JSONObject(inline);
                try {
                    Iterator<String> keys = jsonProvince.keys();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        if (jsonProvince.get(key) instanceof JSONObject) {
                            Local local = (Local) jsonProvince.get(key);
                            SpinnerOption spOption = new SpinnerOption(local.getName(), local.getCode());
                            list.add(spOption);
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    protected JSONArray doInBackground(String... urls) {
        List<SpinnerOption> listLocal = new ArrayList<>();
        JSONArray jsonLocal = null;
        try {

            URL url = new URL(urls[0]);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            //Getting the response code
            int responsecode = conn.getResponseCode();

            if (responsecode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responsecode);
            } else {

                String inline = "";
                Scanner scanner = new Scanner(url.openStream());

                //Write all the JSON data into a string using a scanner
                while (scanner.hasNext()) {
                    inline += scanner.nextLine();
                }

                //Close the scanner
                scanner.close();

                jsonLocal = new JSONArray(inline);

//                for (int i = 0; i < jsonLocal.length(); i++) {
//                    try {
//                        JSONObject object = jsonLocal.getJSONObject(i);
//                        SpinnerOption spOption = new SpinnerOption(
//                                object.getString("name"), object.getString("code"));
//                        listLocal.add(spOption);
//                    } catch (JSONException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonLocal;
    }
}
