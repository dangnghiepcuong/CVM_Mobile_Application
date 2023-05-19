package com.example.cvm_mobile_application.data.objects;

import android.content.Context;

import androidx.annotation.Nullable;

import com.example.cvm_mobile_application.data.SpinnerOption;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DVHCHelper {
    private final JSONObject jsonProvince;
    private final JSONObject jsonDistrict;
    private final JSONObject jsonWard;
    public static final int PROVINCE_LEVEL = 1;
    public static final int DISTRICT_LEVEL = 2;
    public static final int WARD_LEVEL = 3;

    public DVHCHelper(Context context) {
        String stringProvince, stringDistrict, stringWard;

        //READ JSON FILE
        try {
            InputStream inputStream = context.getAssets().open("tinh_tp.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            stringProvince = new String(buffer, StandardCharsets.UTF_8);

            inputStream = context.getAssets().open("quan_huyen.json");
            size = inputStream.available();
            buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            stringDistrict = new String(buffer, StandardCharsets.UTF_8);

            inputStream = context.getAssets().open("xa_phuong.json");
            size = inputStream.available();
            buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            stringWard = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //NEW JSON FROM CONTENT
        try {
            jsonProvince = new JSONObject(stringProvince);
            jsonDistrict = new JSONObject(stringDistrict);
            jsonWard = new JSONObject(stringWard);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    //GET THE SPECIFIED LIST OF LOCAL
    public List<SpinnerOption> getLocalList(int localLevel, @Nullable String parentCode) throws JSONException {
        List<SpinnerOption> localList = new ArrayList<>();

        //SPECIFY THE RETRIEVING LIST OF LOCAL
        JSONObject jsonLocal;
        switch (localLevel) {
            case 1:
                jsonLocal = jsonProvince;
                break;
            case 2:
                jsonLocal = jsonDistrict;
                break;
            case 3:
                jsonLocal = jsonWard;
                break;
            default:
                return null;
        }

        Iterator<String> keys = jsonLocal.keys();

        //CHECK WHETHER THE ITEMS IN THE RETRIEVING LIST DEPEND ON A PARENT CODE
        if (parentCode == null) {
            while (keys.hasNext()) {
                String key = keys.next();
                JSONObject object = jsonLocal.getJSONObject(key);
                SpinnerOption spOption = new SpinnerOption(
                        object.getString("name"), object.getString("code"));
                localList.add(spOption);
            }
        } else {
            //THEN RETRIEVE ONLY THE SATISFIED ITEMS BY CHECKING ITS PARENT CODE
            while (keys.hasNext()) {
                String key = keys.next();
                JSONObject object = jsonLocal.getJSONObject(key);
                if (object.getString("parent_code").equals(parentCode)) {
                    SpinnerOption spOption = new SpinnerOption(
                            object.getString("name"), object.getString("code"));
                    localList.add(spOption);
                }
            }
        }

        return localList;
    }

//    GET THE SPECIFIED LIST OF LOCAL WITH A GIVEN CUSTOM 1ST ELEMENT
    public List<SpinnerOption> getLocalListWithCustom1stElement(
            int localLevel, String localName, @Nullable String parentCode)
            throws JSONException {
        List<SpinnerOption> localList;
        int localPosition = -1;

        //SPECIFY THE RETRIEVING LIST OF LOCAL AND THE POSITION OF THE GIVEN LOCAL NAME IN THE LIST
        switch (localLevel) {
            case 1:
                localList = getLocalList(PROVINCE_LEVEL, null);
                break;
            case 2:
                localList = getLocalList(DISTRICT_LEVEL, parentCode);
                localPosition = getLocalPositionFromList(localLevel, localName, parentCode);
                break;
            case 3:
                localList = getLocalList(WARD_LEVEL, parentCode);
                localPosition = getLocalPositionFromList(localLevel, localName, parentCode);
                break;
            default:
                return null;
        }

        //REMOVE THE THE SAME LOCAL ALREADY HAD IN THE LIST
        if (localPosition != -1) {
            localList.remove(localPosition);
        }

        //RECREATE THE LOCAL AND ADD IT AT THE BEGIN OF THE LIST
        String localCode = getLocalCode(localLevel, localName);
        SpinnerOption sp = new SpinnerOption(localName, localCode);
        localList.add(0, sp);

        return localList;
    }

    //GET THE CODE OF THE GIVEN LOCAL NAME
    public String getLocalCode(int localLevel, String localName) throws JSONException {
        String code = "";
        JSONObject jsonLocal;

        //SPECIFY THE RETRIEVING LIST OF THE LOCAL
        switch (localLevel) {
            case 1:
                jsonLocal = jsonProvince;
                break;
            case 2:
                jsonLocal = jsonDistrict;
                break;
            case 3:
                jsonLocal = jsonWard;
                break;
            default:
                return "";
        }

        //LOOP TO FIND THE LOCAL IN THE LIST AND ITS CODE
        Iterator<String> keys = jsonLocal.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            JSONObject object = jsonLocal.getJSONObject(key);
            if (object.getString("name").equals(localName)) {
                code = object.getString("code");
                return code;
            }
        }

        return code;
    }

    //GET THE NAME OF THE GIVEN LOCAL CODE
    public String getLocalName(int localLevel, String localCode) throws JSONException {
        String code = "";
        JSONObject jsonLocal;

        //SPECIFY THE RETRIEVING LIST OF THE LOCAL
        switch (localLevel) {
            case 1:
                jsonLocal = jsonProvince;
                break;
            case 2:
            case 3:
                jsonLocal = jsonDistrict;
                break;
            default:
                return "";
        }

        //LOOP TO FIND THE LOCAL IN THE LIST AND ITS NAME
        Iterator<String> keys = jsonLocal.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            JSONObject object = jsonLocal.getJSONObject(key);
            if (object.getString("code").equals(localCode)) {
                code = object.getString("name");
                return code;
            }
        }

        return code;
    }

    //GET THE POSITION OF THE GIVEN LOCAL NAME IN THE SPECIFIED LIST OF LOCAL
    public int getLocalPositionFromList(int localLevel, String localName, @Nullable String parentCode)
            throws JSONException {
        int position = -1;

        //GET THE SPECIFIED LIST OF LOCAL
        List<SpinnerOption> localList;
        switch (localLevel) {
            case 1:
                localList = getLocalList(localLevel, null);
                break;
            case 2:
            case 3:
                localList = getLocalList(localLevel, parentCode);
                break;
            default:
                return -1;
        }

        //GET THE POSITION OF THE GIVEN LOCAL NAME IN THE SPECIFIED LIST
        for (SpinnerOption sp: localList) {
            position++;
            if (sp.getOption().equals(localName)) {
                return position;
            }
        }

        return position;
    }
}
