package com.example.cvm_mobile_application.data.helpers;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.annotation.Nullable;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.SpinnerOption;
import com.example.cvm_mobile_application.ui.SpinnerAdapter;

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
    private Spinner spProvince;
    private List<SpinnerOption> provinceList = new ArrayList<>();
    private SpinnerAdapter spProvinceListAdapter;
    private Spinner spDistrict;
    private List<SpinnerOption> districtList = new ArrayList<>();
    private SpinnerAdapter spDistrictListAdapter;
    private Spinner spWard;
    private List<SpinnerOption> wardList = new ArrayList<>();
    private SpinnerAdapter spWardListAdapter;

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

    public Spinner getSpProvince() {
        return spProvince;
    }

    public void setSpProvince(Spinner spProvince) {
        this.spProvince = spProvince;
    }

    public Spinner getSpDistrict() {
        return spDistrict;
    }

    public void setSpDistrict(Spinner spDistrict) {
        this.spDistrict = spDistrict;
    }

    public Spinner getSpWard() {
        return spWard;
    }

    public void setSpWard(Spinner spWard) {
        this.spWard = spWard;
    }

    //GET THE SPECIFIED LIST OF LOCAL
    public List<SpinnerOption> getLocalList(int localLevel, @Nullable String parentCode) throws JSONException {
        List<SpinnerOption> localList = getAnAllOption();

        //SPECIFY THE RETRIEVING LIST OF LOCAL
        JSONObject jsonLocal;
        switch (localLevel) {
            case PROVINCE_LEVEL:
                jsonLocal = jsonProvince;
                break;
            case DISTRICT_LEVEL:
                jsonLocal = jsonDistrict;
                break;
            case WARD_LEVEL:
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
            // IF PARENT CODE IS UNDEFINED, RETURN A LIST WITH AN ALL OPTION
            if (parentCode.equals("-1")) {
                localList = getAnAllOption();

            } else {
                // ELSE RETRIEVE ONLY THE SATISFIED ITEMS BY CHECKING ITS PARENT CODE
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
            case PROVINCE_LEVEL:
                localList = getLocalList(PROVINCE_LEVEL, null);
                break;
            case DISTRICT_LEVEL:
                localList = getLocalList(DISTRICT_LEVEL, parentCode);
                localPosition = getLocalPositionFromList(localLevel, localName, parentCode);
                break;
            case WARD_LEVEL:
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

    public List<SpinnerOption> getAnAllOption() {
        List<SpinnerOption> localList = new ArrayList<>();
        SpinnerOption spOption = new SpinnerOption("Tất cả", "-1");
        localList.add(spOption);
        return localList;
    }

    //GET THE CODE OF THE GIVEN LOCAL NAME
    public String getLocalCode(int localLevel, String localName) throws JSONException {
        String code = "";
        JSONObject jsonLocal;

        //SPECIFY THE RETRIEVING LIST OF THE LOCAL
        switch (localLevel) {
            case PROVINCE_LEVEL:
                jsonLocal = jsonProvince;
                break;
            case DISTRICT_LEVEL:
                jsonLocal = jsonDistrict;
                break;
            case WARD_LEVEL:
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
            case PROVINCE_LEVEL:
                jsonLocal = jsonProvince;
                break;
            case DISTRICT_LEVEL:
            case WARD_LEVEL:
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
            case PROVINCE_LEVEL:
                localList = getLocalList(localLevel, null);
                break;
            case DISTRICT_LEVEL:
            case WARD_LEVEL:
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

    public void bindLocalListSpinnerData(Context context,
                                         String provinceName,
                                         String districtName,
                                         String wardName) throws JSONException {
        //GET PROVINCE LIST
        provinceList = getLocalList(PROVINCE_LEVEL, null);

        //SET PROVINCE INFO VALUE
        spProvinceListAdapter = new SpinnerAdapter(context,
                R.layout.item_string, provinceList);
        spProvince.setAdapter(spProvinceListAdapter);
        int provincePosition = getLocalPositionFromList(
                PROVINCE_LEVEL, provinceName, null);
        spProvince.setSelection(provincePosition, false);

        //GET DISTRICT LIST
        String provinceCode = ((SpinnerOption) spProvince.getItemAtPosition(spProvince.getSelectedItemPosition())).getValue();
        districtList = getLocalList(DISTRICT_LEVEL, provinceCode);

        //SET DISTRICT INFO VALUE
        spDistrictListAdapter = new SpinnerAdapter(context,
                R.layout.item_string, districtList);
        spDistrict.setAdapter(spDistrictListAdapter);
        int districtPosition = getLocalPositionFromList(
                DISTRICT_LEVEL, districtName, provinceCode);
        spDistrict.setSelection(districtPosition, false);

        //GET WARD LIST
        String districtCode = ((SpinnerOption) spDistrict.getItemAtPosition(spDistrict.getSelectedItemPosition())).getValue();
        wardList = getLocalList(WARD_LEVEL, districtCode);

        //SET WARD INFO VALUE
        spWardListAdapter = new SpinnerAdapter(context,
                R.layout.item_string, wardList);
        spWard.setAdapter(spWardListAdapter);
        int wardPosition = getLocalPositionFromList(
                WARD_LEVEL, wardName, districtCode);
        spWard.setSelection(wardPosition);
    }

    public void setLocalListSpinnerListener() {
        //SET PROVINCE SPINNER LISTENER
        spProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //GET DISTRICT LIST, WARD LIST
                DVHCHelper.this
                        .spProvinceTriggeredActivities();
                Log.i("myTAG", "province spinner");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //SET DISTRICT SPINNER LISTENER
        spDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //GET WARD LIST
                DVHCHelper.this
                        .spDistrictTriggeredActivities();
                Log.i("myTAG", "district spinner");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spWard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DVHCHelper.this
                        .spWardTriggeredActivities();
                Log.i("myTAG", "ward spinner");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void spProvinceTriggeredActivities() {
        try {
            SpinnerOption provinceOption = provinceList.get(spProvince.getSelectedItemPosition());
            districtList = getLocalList(
                    DVHCHelper.DISTRICT_LEVEL, provinceOption.getValue());
            spDistrictListAdapter.setOptionList(districtList);
            spDistrictListAdapter.notifyDataSetChanged();

            // Changing selected province triggers district listener to change district list.
            // And when changing district list, we also need to change ward list,

            // IN CASE, THE DISTRICT SPINNER HAS NOT BEEN SELECTED
            // (SELECTED POSITION STAYS = 0)
            // THEN WHEN .setSelection(0) IS CALLED
            // IT DOES NOT TRIGGER THE SELECTION OF THE DISTRICT SPINNER
            // (THE ACTIVITY WHEN DISTRICT SPINNER IS TRIGGERED IS CHANGING THE WARD LIST)
            // SO WE NEED TO DO THE ACTIVITY OF THE DISTRICT SPINNER TRIGGER BY HAND HERE
            if (spDistrict.getSelectedItemPosition() == 0) {
                spDistrictTriggeredActivities();
            }
            // ELSE SET SELECTION TO 0 AND TRIGGER THE DISTRICT SPINNER AUTOMATICALLY
            else {
                spDistrict.setSelection(0, true);
            }

            // MORE EXPLANATION
            // THE REASON WE NEED TO TRIGGER THESE LISTENERS IN CHAIN THAT IS
            // TO MAKE SURE ALL THESE ACTIVITIES ARE ACTIVATED COMPLETELY AND IN ORDERLY

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void spDistrictTriggeredActivities() {
        try {
            SpinnerOption districtOption = districtList.get(spDistrict.getSelectedItemPosition());
            wardList = getLocalList(
                    DVHCHelper.WARD_LEVEL, districtOption.getValue());
            spWardListAdapter.setOptionList(wardList);
            spWardListAdapter.notifyDataSetChanged();

            // TRIGGER WARD SPINNER SELECTION FOR THE NEXT ACTIVITIES

            if (spWard.getSelectedItemPosition() == 0) {
                spWardTriggeredActivities();
            } else {
                spWard.setSelection(0, true);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void spWardTriggeredActivities() {
        SpinnerOption wardOption = wardList.get(spWard.getSelectedItemPosition());
    }

    public SpinnerOption getSelectedLocal(int localLevel) {
        switch (localLevel) {
            case PROVINCE_LEVEL:
                return provinceList.get(spProvince.getSelectedItemPosition());
            case DISTRICT_LEVEL:
                return districtList.get(spDistrict.getSelectedItemPosition());
            case WARD_LEVEL:
                return wardList.get(spWard.getSelectedItemPosition());
            default:
                return new SpinnerOption("Tất cả", "-1");
        }
    }
}
