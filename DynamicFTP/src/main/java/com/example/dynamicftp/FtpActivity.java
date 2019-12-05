package com.example.dynamicftp;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import jahirfiquitiva.libs.fabsmenu.FABsMenu;

import android.app.ActionBar;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.amaze.filemanager.activities.MainActivity;
import com.amaze.filemanager.activities.superclasses.BasicActivity;
import com.amaze.filemanager.activities.superclasses.ThemedActivity;
import com.amaze.filemanager.database.CloudHandler;
import com.amaze.filemanager.database.TabHandler;
import com.amaze.filemanager.fragments.preference_fragments.PreferencesConstants;
import com.amaze.filemanager.ui.colors.ColorPreferenceHelper;
import com.amaze.filemanager.ui.views.appbar.AppBar;
import com.amaze.filemanager.ui.views.appbar.BottomBar;
import com.amaze.filemanager.ui.views.appbar.SearchView;
import com.amaze.filemanager.ui.views.drawer.Drawer;
import com.amaze.filemanager.utils.MainActivityHelper;
import com.amaze.filemanager.utils.PreferenceUtils;
import com.amaze.filemanager.utils.application.AppConfig;
import com.amaze.filemanager.utils.theme.AppTheme;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import static android.os.Build.VERSION.SDK_INT;
import static com.amaze.filemanager.fragments.preference_fragments.PreferencesConstants.PREFERENCE_COLORED_NAVIGATION;
import static com.amaze.filemanager.utils.PreferenceUtils.getStatusColor;

public class FtpActivity extends ThemedActivity {

    private Toolbar toolbar;
    private AppBarLayout appbarLayout;
    private SearchView searchView;
    private AppBarLayout appBarLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ftpac", "zichong");

        //setTheme(com.amaze.filemanager.R.style.overflow_light);
        View v = getWindow().getDecorView();
        v.setFitsSystemWindows(true);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        setContentView(com.amaze.filemanager.R.layout.main_toolbar);
        FABsMenu floatingActionButton = findViewById(com.amaze.filemanager.R.id.fabs_menu);
        floatingActionButton.getMenuButton().hide();
        FrameLayout frame = findViewById(com.amaze.filemanager.R.id.buttonbarframe);
        frame.setVisibility(View.GONE);
        toolbar = this.findViewById(com.amaze.filemanager.R.id.action_bar);
        toolbar.setTitle("FTP Server");
        toolbar.setBackgroundColor(Color.parseColor("#3f51b5"));

        if (SDK_INT >= 21) toolbar.setElevation(0);
        setSupportActionBar(toolbar);
        initialiseViews();
        //bar.setBackgroundDrawable(new ColorDrawable(Color.BLUE));
        appbarLayout = this.findViewById(com.amaze.filemanager.R.id.lin);
        appbarLayout.setExpanded(true, true);
        //searchView = new SearchView(this, a, searchListener);
        Log.d("ftpac", "zichong2");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Log.d("ftpac", "zichong3");
        transaction.replace(com.amaze.filemanager.R.id.content_frame, new FtpServerFragment());
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
            FrameLayout.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) findViewById(com.amaze.filemanager.R.id.drawer_layout).getLayoutParams();
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
