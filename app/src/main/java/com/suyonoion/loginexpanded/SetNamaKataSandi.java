package com.suyonoion.loginexpanded;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.provider.Settings;
import android.text.TextUtils;

/**
 * Created by Suyono on 10/6/2015.
 * Copyright (c) 2015 by Suyono (ion).
 * All rights reserved.
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */
public class SetNamaKataSandi extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    public int setResource(String name, String Type)
    {
        return getBaseContext().getResources().getIdentifier(name, Type, getBaseContext().getPackageName());
    }

    protected void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);

        addPreferencesFromResource(setResource("pref_login_expanded","xml"));

        // preference judul 1
        EditTextPreference editTxt_pref = (EditTextPreference)findPreference("key_nama");
        String isi_txt_judul1 = Settings.System.getString(getContentResolver(), "uri_nama");

        if(!TextUtils.isEmpty(isi_txt_judul1)){
            editTxt_pref.setSummary(isi_txt_judul1.replaceAll(".","-"));
        }

        // preference judul 2
        EditTextPreference editTxt_pref2 = (EditTextPreference)findPreference("key_katasandi");
        String isi_txt_judul2 = Settings.System.getString(getContentResolver(), "uri_katasandi");
        if(!TextUtils.isEmpty(isi_txt_judul2)){
            editTxt_pref2.setSummary(isi_txt_judul2.replaceAll(".","-"));
        }


    }

    @Override
    public void onResume(){
        super.onResume();

        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        //jika key_judul1 cocok/sama
        if (key.equals("key_nama")){
            Preference pref_judul1= findPreference(key);
            String strJudul1 = "";
            if(pref_judul1 instanceof EditTextPreference){
                strJudul1 = ((EditTextPreference)pref_judul1).getText();
            }
            pref_judul1.setSummary(strJudul1.replaceAll(".","-"));
            Settings.System.putString(getContentResolver(), "uri_nama", strJudul1);
        }

        //jika key_judul2 cocok/sama
        if (key.equals("key_katasandi")){
            Preference pref_judul2= findPreference(key);
            String strJudul2 = "";
            if(pref_judul2 instanceof EditTextPreference){
                strJudul2 = ((EditTextPreference)pref_judul2).getText();
            }
            pref_judul2.setSummary(strJudul2.replaceAll(".","-"));
            Settings.System.putString(getContentResolver(), "uri_katasandi", strJudul2);
        }


    }


}
