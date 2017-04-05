package com.clam314.rxrank.fragment;


import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.clam314.rxrank.GlobalConfig;
import com.clam314.rxrank.R;
import com.clam314.rxrank.activity.WebActivity;
import com.clam314.rxrank.entity.Item;
import com.clam314.rxrank.util.ConfigUtil;


public class SettingFragment extends PreferenceFragmentCompat {

    String openModeKey = "";

    public SettingFragment() {
    }

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        this.getPreferenceManager().setSharedPreferencesName(GlobalConfig.NAME_PREFERENCE_SP);
        addPreferencesFromResource(R.xml.setting);
        String showModeKey = getResources().getString(R.string.setting_show_mode_key);
        openModeKey = getResources().getString(R.string.read_mode_key);
        initPreference();
        Preference listPreference = this.findPreference(showModeKey);
        if( listPreference instanceof ListPreference){
            listPreference.setSummary(((ListPreference) listPreference).getValue());
        }
        listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if(preference instanceof ListPreference){
                    preference.setSummary((String)newValue);
                    ConfigUtil.changeNightMode(getActivity(),(String)newValue);
                }
                return true;
            }
        });
//        this.getPreferenceManager().findPreference(openModeKey).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//            @Override
//            public boolean onPreferenceChange(Preference preference, Object newValue) {
//                if(preference instanceof CheckBoxPreference){
//                    ConfigUtil.setOpenPageBySystem((Boolean)newValue);
//                }
//                return false;
//            }
//        });
    }

    private void initPreference(){
        Preference author = this.getPreferenceManager().findPreference("author");
        Preference version = this.getPreferenceManager().findPreference("version");
        Preference resource = this.getPreferenceManager().findPreference("resource_code");
        author.setTitle("作者");
        author.setSummary("clam314@163.com");
        resource.setSummary("https://github.com/clam314/ReRank");
        PackageInfo info = ConfigUtil.getVersionInfo(getContext());
        if(info != null){
            version.setSummary(info.versionName);
        }else {
            version.setSummary(" ");
        }
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        if("resource_code".equals(preference.getKey())){
            String url = preference.getSummary().toString();
            String desc = preference.getTitle().toString();
            Item item = new Item();
            item.setUrl(url);
            item.setDesc(desc);
            Intent intent = new Intent(getContext(), WebActivity.class);
            intent.putExtra(WebActivity.PARAM_ITEM,item);
            startActivity(intent);
            return true;
        }else if(openModeKey.equals(preference.getKey())){
            if(preference instanceof CheckBoxPreference){
                ConfigUtil.setOpenPageBySystem(((CheckBoxPreference)preference).isChecked());
            }
        }
        return super.onPreferenceTreeClick(preference);
    }
}
