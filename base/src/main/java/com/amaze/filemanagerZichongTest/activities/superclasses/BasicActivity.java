package com.amaze.filemanagerZichongTest.activities.superclasses;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.amaze.filemanagerZichongTest.ui.colors.ColorPreferenceHelper;
import com.amaze.filemanagerZichongTest.utils.application.AppConfig;
import com.amaze.filemanagerZichongTest.utils.provider.UtilitiesProvider;
import com.amaze.filemanagerZichongTest.utils.theme.AppTheme;

/**
 * Created by rpiotaix on 17/10/16.
 */
public class BasicActivity extends AppCompatActivity {

    protected AppConfig getAppConfig() {
        return (AppConfig) getApplication();
    }

    public ColorPreferenceHelper getColorPreference() {
        return getAppConfig().getUtilsProvider().getColorPreference();
    }
    @Override
    protected void onCreate(Bundle App){
        super.onCreate(App);
        Log.d("BaseAtivity", getClass().getSimpleName());
    }

    public AppTheme getAppTheme() {
        return getAppConfig().getUtilsProvider().getAppTheme();
    }

    public UtilitiesProvider getUtilsProvider() {
        return getAppConfig().getUtilsProvider();
    }
}
