package com.example.dynamicftp;

import androidx.annotation.ColorInt;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import jahirfiquitiva.libs.fabsmenu.FABsMenu;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import com.amaze.filemanagerZichongTest.activities.MainActivity;
import com.amaze.filemanagerZichongTest.activities.superclasses.ThemedActivity;
import com.amaze.filemanagerZichongTest.fragments.preference_fragments.PreferencesConstants;
import com.amaze.filemanagerZichongTest.ui.colors.ColorPreferenceHelper;
import com.amaze.filemanagerZichongTest.ui.views.appbar.SearchView;
import com.amaze.filemanagerZichongTest.utils.PreferenceUtils;
import com.google.android.material.appbar.AppBarLayout;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import static android.os.Build.VERSION.SDK_INT;
import static com.amaze.filemanagerZichongTest.utils.PreferenceUtils.getStatusColor;

public class FtpActivity extends ThemedActivity {

    private Toolbar toolbar;
    private AppBarLayout appbarLayout;
    private SearchView searchView;
    private AppBarLayout appBarLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ftpac", "zichong");

        //setTheme(com.amaze.filemanagerZichongTest.R.style.overflow_light);
        View v = getWindow().getDecorView();
        v.setFitsSystemWindows(true);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        setContentView(com.amaze.filemanagerZichongTest.R.layout.main_toolbar);
        FABsMenu floatingActionButton = findViewById(com.amaze.filemanagerZichongTest.R.id.fabs_menu);
        floatingActionButton.getMenuButton().hide();
        FrameLayout frame = findViewById(com.amaze.filemanagerZichongTest.R.id.buttonbarframe);
        frame.setVisibility(View.GONE);
        toolbar = this.findViewById(com.amaze.filemanagerZichongTest.R.id.action_bar);
        toolbar.setTitle("FTP Server");
        toolbar.setBackgroundColor(Color.parseColor("#3f51b5"));

        if (SDK_INT >= 21) toolbar.setElevation(0);
        setSupportActionBar(toolbar);
        initialiseViews();
        //bar.setBackgroundDrawable(new ColorDrawable(Color.BLUE));
        appbarLayout = this.findViewById(com.amaze.filemanagerZichongTest.R.id.lin);
        appbarLayout.setExpanded(true, true);
        //searchView = new SearchView(this, a, searchListener);
        Log.d("ftpac", "zichong2");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Log.d("ftpac", "zichong3");
        transaction.replace(com.amaze.filemanagerZichongTest.R.id.content_frame, new FtpServerFragment());
        Log.d("ftpac", "zichong4");
        transaction.commit();
        Log.d("ftpac", "zichong5");
    }
    void initialiseViews() {
        //appBarLayout = getAppbar().getAppbarLayout();

        //buttonBarFrame.setBackgroundColor(Color.parseColor(currentTab==1 ? skinTwo : skin));

        //setSupportActionBar(getAppbar().getToolbar());

        int skin_color = getCurrentColorPreference().primaryFirstTab;
        int skinTwoColor = getCurrentColorPreference().primarySecondTab;

        this.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(MainActivity.currentTab==1 ?
                skinTwoColor : skin_color));

        // status bar0
        if (SDK_INT == Build.VERSION_CODES.KITKAT_WATCH || SDK_INT == Build.VERSION_CODES.KITKAT) {
            Log.d("ftpac", "zichong6");
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(Color.parseColor("#3f51b5"));
            FrameLayout.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) findViewById(com.amaze.filemanagerZichongTest.R.id.drawer_layout).getLayoutParams();
            SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
        } else if (SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.d("ftpac", "zichong7");
            Window window = getWindow();
            int currentTab = getPrefs().getInt(PreferencesConstants.PREFERENCE_CURRENT_TAB, PreferenceUtils.DEFAULT_CURRENT_TAB);
            @ColorInt int currentPrimary = ColorPreferenceHelper.getPrimary(getCurrentColorPreference(), MainActivity.currentTab);
            int skinStatusBar = PreferenceUtils.getStatusColor(currentPrimary);
            window.setStatusBarColor(Color.parseColor("#3f51b5"));
            //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

}
