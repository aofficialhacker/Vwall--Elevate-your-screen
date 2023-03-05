package com.example.timepassapp;


import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends Fragment {

    private EditText etInterval;
    private Button btnSaveInterval;
    private SharedPreferences sharedPreferences;
    private Switch switchTheme;
    private Switch switchWallpaperByWeather;
    private boolean isWallpaperByWeatherEnabled;

    private static final int PERMISSION_REQUEST_CODE = 1001;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        // Remove the search views from the activity
        LinearLayout searchLayout = getActivity().findViewById(R.id.ll);
        searchLayout.setVisibility(View.GONE);

        switchTheme = view.findViewById(R.id.switch_theme);


        boolean isDarkMode = sharedPreferences.getBoolean(getString(R.string.pref_key_dark_mode), false);
        switchTheme.setChecked(isDarkMode);


        switchTheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(getString(R.string.pref_key_dark_mode), isChecked);
                editor.apply();

                // Change the theme according to the switch state
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });


        etInterval = view.findViewById(R.id.edit_seconds);
        btnSaveInterval = view.findViewById(R.id.btn_apply_interval);
        btnSaveInterval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyInterval(view);
            }
        });

        // Set the current interval value to the EditText
        etInterval.setText(String.valueOf(getInterval()));

        return view;
    }

    public void applyInterval(View view) {
        String intervalStr = etInterval.getText().toString().trim();
        int interval = intervalStr.isEmpty() ? 0 : Integer.parseInt(intervalStr);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(getString(R.string.pref_key_interval), interval);
        editor.apply();

        // Change the wallpaper automatically using the new interval
        ((MainActivity) getActivity()).changeWallpaperAutomatically(interval);
    }

    public int getInterval() {
        return sharedPreferences.getInt(getString(R.string.pref_key_interval), 0);
    }


}







