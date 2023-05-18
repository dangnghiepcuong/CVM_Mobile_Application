package com.example.cvm_mobile_application.data.objects;

import android.content.Context;

import com.example.cvm_mobile_application.data.SpinnerOption;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class LocalHelper {
    JSONArray jsonLocal;

    public LocalHelper(Context context) {
        String json;
        try {
            InputStream inputStream = context.getAssets().open("local.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            jsonLocal = new JSONArray(json);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public List<SpinnerOption> getProvinceList() throws JSONException {
        List<SpinnerOption> provinceList = new ArrayList<>();
        int len = jsonLocal.length();
        for (int i = 0; i < len; i++) {
                JSONObject object = jsonLocal.getJSONObject(i);
                SpinnerOption spOption = new SpinnerOption(
                        object.getString("name"), object.getString("id"));
                provinceList.add(spOption);
        }

        return provinceList;
    }

    public List<SpinnerOption> getDistrictList(int provincePos) throws JSONException {
        List<SpinnerOption> districtList = new ArrayList<>();

        JSONArray jsonDistrict = jsonLocal.getJSONObject(provincePos).getJSONArray("districts");

        int len = jsonDistrict.length();
        for (int i = 0; i < len; i++) {
            JSONObject object = jsonDistrict.getJSONObject(i);
            SpinnerOption spOption = new SpinnerOption(
                    object.getString("name"), object.getString("id"));
            districtList.add(spOption);
        }
        return districtList;
    }

    public List<SpinnerOption> getWardList(int provincePos, int districtPos) throws JSONException {
        List<SpinnerOption> wardList = new ArrayList<>();

        JSONArray jsonWard = jsonLocal.getJSONObject(provincePos).getJSONArray("districts")
                .getJSONObject(districtPos).getJSONArray("wards");

        int len = jsonWard.length();
        for (int i = 0; i < len; i++) {
            JSONObject object = jsonWard.getJSONObject(i);
            SpinnerOption spOption = new SpinnerOption(
                    object.getString("name"), object.getString("id"));
            wardList.add(spOption);
        }
        return wardList;
    }

    public List<SpinnerOption> getCitizenProvinceList(String provinceName) throws JSONException {
        List<SpinnerOption> citizenProvinceList = getProvinceList();

        //FIND THE PROVINCE CODE OF CITIZEN
        String provinceId = getPropertyValueFromList(citizenProvinceList, provinceName);
        int provincePos = getPropertyPositionFromList(citizenProvinceList, provinceName);
        //CREATE NEW OPTION AS CITIZEN'S PROVINCE
        SpinnerOption sp = new SpinnerOption(provinceName, provinceId);
        //ADD THE OPTION ON TOP OF THE LIST
        citizenProvinceList.add(0, sp);
        //REMOVE THE DUPLICATE OPTION WHICH IS NOT ON TOP OF THE LIST
        citizenProvinceList.remove(provincePos+1);

        return citizenProvinceList;
    }

    public List<SpinnerOption> getCitizenDistrictList(String provinceName, String districtName) throws JSONException {
        int provincePos = getPropertyPositionFromList(getProvinceList(), provinceName);
        List<SpinnerOption> citizenDistrictList = getDistrictList(provincePos);

        //FIND THE PROVINCE CODE OF CITIZEN
        String districtId = getPropertyValueFromList(citizenDistrictList, districtName);
        int districtPos = getPropertyPositionFromList(citizenDistrictList, districtName);
        //CREATE NEW OPTION AS CITIZEN'S PROVINCE
        SpinnerOption sp = new SpinnerOption(districtName, districtId);
        //ADD THE OPTION ON TOP OF THE LIST
        citizenDistrictList.add(0, sp);
        //REMOVE THE DUPLICATE OPTION WHICH IS NOT ON TOP OF THE LIST
        citizenDistrictList.remove(districtPos+1);

        return citizenDistrictList;
    }

    public List<SpinnerOption> getCitizenWardList(String provinceName,
                                                  String districtName,
                                                  String wardName) throws JSONException {
        int provincePos = getPropertyPositionFromList(getProvinceList(), provinceName);
        List<SpinnerOption> citizenDistrictList = getDistrictList(provincePos);

        int districtPos = getPropertyPositionFromList(citizenDistrictList, districtName);
        List<SpinnerOption> citizenWardList = getWardList(provincePos, districtPos);

        //FIND THE DISTRICT CODE OF CITIZEN
        String wardId = getPropertyValueFromList(citizenWardList, wardName);
        int wardPos = getPropertyPositionFromList(citizenWardList, wardName);
        //CREATE NEW OPTION AS CITIZEN'S PROVINCE
        SpinnerOption sp = new SpinnerOption(wardName, wardId);
        //ADD THE OPTION ON TOP OF THE LIST
        citizenWardList.add(0, sp);
        //REMOVE THE DUPLICATE OPTION WHICH IS NOT ON TOP OF THE LIST
        citizenWardList.remove(wardPos+1);

        return citizenWardList;
    }

    public String getPropertyValueFromList(List<SpinnerOption> list, String property) {
        String value = "";

        for (SpinnerOption object : list) {
            if (object.getOption().equals(property)) {
                value = object.getValue();
                return value;
            }
        }

        return value;
    }

    public int getPropertyPositionFromList(List<SpinnerOption> list, String property) {
        int position = -1;

        for (SpinnerOption object : list) {
            position++;
            if (object.getOption().equals(property)) {
                return position;
            }
        }

        return position;
    }
}
