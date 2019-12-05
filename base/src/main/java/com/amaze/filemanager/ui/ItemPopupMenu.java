package com.amaze.filemanager.ui;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.amaze.filemanager.R;
import com.amaze.filemanager.activities.MainActivity;
import com.amaze.filemanager.activities.superclasses.ThemedActivity;
import com.amaze.filemanager.adapters.data.LayoutElementParcelable;
//import com.example.dynamicencrypt.EncryptService;
import com.amaze.filemanager.filesystem.HybridFileParcelable;
import com.amaze.filemanager.filesystem.PasteHelper;
import com.amaze.filemanager.fragments.MainFragment;
import com.amaze.filemanager.fragments.preference_fragments.PreferencesConstants;
import com.amaze.filemanager.ui.dialogs.GeneralDialogCreation;
import com.amaze.filemanager.utils.DataUtils;
import com.amaze.filemanager.utils.files.EncryptDecryptUtils;
import com.amaze.filemanager.utils.files.FileUtils;
import com.amaze.filemanager.utils.provider.UtilitiesProvider;
import com.google.android.play.core.splitinstall.SplitInstallManager;
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory;
import com.google.android.play.core.splitinstall.SplitInstallRequest;
import com.google.android.play.core.splitinstall.SplitInstallSessionState;
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener;
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Set;

/**
 * This class contains the functionality of the PopupMenu for each file in the MainFragment
 *
 * @author Emmanuel
 *         on 25/5/2017, at 16:39.
 */

public class ItemPopupMenu extends PopupMenu implements PopupMenu.OnMenuItemClickListener {

    private Context context;
    private MainActivity mainActivity;
    private UtilitiesProvider utilitiesProvider;
    private MainFragment mainFragment;
    private SharedPreferences sharedPrefs;
    private LayoutElementParcelable rowItem;
    private int accentColor;
    private SplitInstallManager manager;
    private final String encryptModel = "DynamicEncrypt";
    private final String className = "com.example.dynamicencrypt.EncryptService";
    private final String classNameDe = "com.example.dynamicencrypt.DecryptService";
    Listener listener = new Listener();

    public ItemPopupMenu(Context c, MainActivity ma, UtilitiesProvider up, MainFragment mainFragment,
                         LayoutElementParcelable ri, View anchor, SharedPreferences sharedPreferences) {
        super(c, anchor);

        context = c;
        mainActivity = ma;
        utilitiesProvider = up;
        this.mainFragment = mainFragment;
        sharedPrefs = sharedPreferences;
        rowItem = ri;
        accentColor = mainActivity.getAccent();
        manager = SplitInstallManagerFactory.create(context);
        setOnMenuItemClickListener(this);
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                GeneralDialogCreation.showPropertiesDialogWithPermissions((rowItem).generateBaseFile(),
                        rowItem.permissions, (ThemedActivity) mainFragment.getActivity(),
                        mainActivity.isRootExplorer(), utilitiesProvider.getAppTheme());
                                /*
                                PropertiesSheet propertiesSheet = new PropertiesSheet();
                                Bundle arguments = new Bundle();
                                arguments.putParcelable(PropertiesSheet.KEY_FILE, rowItem.generateBaseFile());
                                arguments.putString(PropertiesSheet.KEY_PERMISSION, rowItem.getPermissions());
                                arguments.putBoolean(PropertiesSheet.KEY_ROOT, ThemedActivity.rootMode);
                                propertiesSheet.setArguments(arguments);
                                propertiesSheet.show(main.getFragmentManager(), PropertiesSheet.TAG_FRAGMENT);
                                */
                return true;
            case R.id.share:
                switch (rowItem.getMode()) {
                    case DROPBOX:
                    case BOX:
                    case GDRIVE:
                    case ONEDRIVE:
                        FileUtils.shareCloudFile(rowItem.desc, rowItem.getMode(), context);
                        break;
                    default:
                        ArrayList<File> arrayList = new ArrayList<>();
                        arrayList.add(new File(rowItem.desc));
                        FileUtils.shareFiles(arrayList,
                                mainFragment.getMainActivity(), utilitiesProvider.getAppTheme(),
                                accentColor);
                        break;
                }
                return true;
            case R.id.rename:
                mainFragment.rename(rowItem.generateBaseFile());
                return true;
            case R.id.cpy:
            case R.id.cut: {
                int op = item.getItemId() == R.id.cpy? PasteHelper.OPERATION_COPY:PasteHelper.OPERATION_CUT;
                PasteHelper pasteHelper = new PasteHelper(op, new HybridFileParcelable[]{rowItem.generateBaseFile()});
                mainFragment.getMainActivity().setPaste(pasteHelper);
                return true;
            }
            case R.id.ex:
                mainFragment.getMainActivity().mainActivityHelper.extractFile(new File(rowItem.desc));
                return true;
            case R.id.book:
                DataUtils dataUtils = DataUtils.getInstance();
                dataUtils.addBook(new String[]{rowItem.title, rowItem.desc}, true);
                mainFragment.getMainActivity().getDrawer().refreshDrawer();
                Toast.makeText(mainFragment.getActivity(), mainFragment.getString(R.string.bookmarks_added), Toast.LENGTH_LONG).show();
                return true;
            case R.id.delete:
                ArrayList<LayoutElementParcelable> positions = new ArrayList<>();
                positions.add(rowItem);
                GeneralDialogCreation.deleteFilesDialog(context,
                        mainFragment.getElementsList(),
                        mainFragment.getMainActivity(),
                        positions, utilitiesProvider.getAppTheme());
                return true;
            case R.id.open_with:
                boolean useNewStack = sharedPrefs.getBoolean(PreferencesConstants.PREFERENCE_TEXTEDITOR_NEWSTACK, false);
                FileUtils.openWith(new File(rowItem.desc), mainFragment.getActivity(), useNewStack);
                return true;
            case R.id.encrypt:
                if(manager.getInstalledModules().contains(encryptModel)){
                    //onSuccessfulLoad(aboutActivity, true);
                    final String TAG_SOURCE = "crypt_source";     // source file to encrypt or decrypt
                    final String TAG_ENCRYPT_TARGET = "crypt_target"; //name of encrypted file
                    final String TAG_DECRYPT_PATH = "decrypt_path";
                    final String TAG_OPEN_MODE = "open_mode";
                    //final Intent encryptIntent = new Intent(context, EncryptService.class);
                    final Intent encryptIntent = new Intent();
                    encryptIntent.setClassName(context.getPackageName(), className);
                    encryptIntent.putExtra(TAG_OPEN_MODE, rowItem.getMode().ordinal());
                    encryptIntent.putExtra(TAG_SOURCE, rowItem.generateBaseFile());

                    final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

                    final EncryptDecryptUtils.EncryptButtonCallbackInterface encryptButtonCallbackInterfaceAuthenticate =
                            new EncryptDecryptUtils.EncryptButtonCallbackInterface() {
                                @Override
                                public void onButtonPressed(Intent intent) {
                                }

                                @Override
                                public void onButtonPressed(Intent intent, String password) throws GeneralSecurityException, IOException {
                                    EncryptDecryptUtils.startEncryption(context,
                                            rowItem.generateBaseFile().getPath(), password, intent);
                                }
                            };

                    EncryptDecryptUtils.EncryptButtonCallbackInterface encryptButtonCallbackInterface =
                            new EncryptDecryptUtils.EncryptButtonCallbackInterface() {

                                @Override
                                public void onButtonPressed(Intent intent) throws GeneralSecurityException, IOException {
                                    // check if a master password or fingerprint is set
                                    if (!preferences.getString(PreferencesConstants.PREFERENCE_CRYPT_MASTER_PASSWORD,
                                            PreferencesConstants.PREFERENCE_CRYPT_MASTER_PASSWORD_DEFAULT).equals("")) {
                                        GeneralDialogCreation.showEncryptWithPresetPasswordSaveAsDialog(context, mainActivity,
                                                PreferencesConstants.ENCRYPT_PASSWORD_MASTER, encryptIntent);
                                    } else if (preferences.getBoolean(PreferencesConstants.PREFERENCE_CRYPT_FINGERPRINT,
                                            PreferencesConstants.PREFERENCE_CRYPT_FINGERPRINT_DEFAULT)) {
                                        GeneralDialogCreation.showEncryptWithPresetPasswordSaveAsDialog(context, mainActivity,
                                                PreferencesConstants.ENCRYPT_PASSWORD_FINGERPRINT, encryptIntent);
                                    } else {
                                        // let's ask a password from user
                                        GeneralDialogCreation.showEncryptAuthenticateDialog(context, encryptIntent,
                                                mainFragment.getMainActivity(), utilitiesProvider.getAppTheme(),
                                                encryptButtonCallbackInterfaceAuthenticate);
                                    }
                                }

                                @Override
                                public void onButtonPressed(Intent intent, String password) {
                                }
                            };

                    if (preferences.getBoolean(PreferencesConstants.PREFERENCE_CRYPT_WARNING_REMEMBER,
                            PreferencesConstants.PREFERENCE_CRYPT_WARNING_REMEMBER_DEFAULT)) {
                        // let's skip warning dialog call
                        try {
                            encryptButtonCallbackInterface.onButtonPressed(encryptIntent);
                        } catch (GeneralSecurityException | IOException e) {
                            e.printStackTrace();
                            Toast.makeText(context,
                                    mainFragment.getString(R.string.crypt_encryption_fail),
                                    Toast.LENGTH_LONG).show();
                        }
                    } else {

                        GeneralDialogCreation.showEncryptWarningDialog(encryptIntent,
                                mainFragment, utilitiesProvider.getAppTheme(), encryptButtonCallbackInterface);
                    }
                }else{
                    Log.d("Zichong",encryptModel);
                    Set<String> installedModules = manager.getInstalledModules();
                    Log.d("Zichong",installedModules.toArray(new String[0])[0]);
                    SplitInstallRequest request = SplitInstallRequest.newBuilder()
                            .addModule(encryptModel)
                            .build();

                    manager.startInstall(request);
                }
                return true;
            case R.id.decrypt:
                final String TAG_SOURCE = "crypt_source";     // source file to encrypt or decrypt
                final String TAG_ENCRYPT_TARGET = "crypt_target"; //name of encrypted file
                final String TAG_DECRYPT_PATH = "decrypt_path";
                final String TAG_OPEN_MODE = "open_mode";
                if(manager.getInstalledModules().contains(encryptModel)){
                    //Intent decryptIntent = new Intent();
                    //decryptIntent.setClassName(context.getPackageName(), classNameDe);
                    //EncryptDecryptUtils.passIntent(decryptIntent);
                    EncryptDecryptUtils.decryptFile(context, mainActivity, mainFragment,
                            mainFragment.openMode, rowItem.generateBaseFile(),
                            rowItem.generateBaseFile().getParent(context), utilitiesProvider, false);
                    return true;
                    //return true;
                }else{
                    Set<String> installedModules = manager.getInstalledModules();
                    SplitInstallRequest request = SplitInstallRequest.newBuilder()
                            .addModule(encryptModel)
                            .build();
                    manager.startInstall(request);
                }
            case R.id.return_select:
                mainFragment.returnIntentResults(rowItem.generateBaseFile());
                return true;
        }
        return false;
    }


    class Listener implements SplitInstallStateUpdatedListener {

        @Override
        public void onStateUpdate(SplitInstallSessionState state) {
            for (String module: state.moduleNames()) {
                switch (state.status()) {
                    case SplitInstallSessionStatus.DOWNLOADING:
                        break;
                    case SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION:
                        try {
                            mainActivity.startIntentSender(state.resolutionIntent().getIntentSender(), null, 0,0,0);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                        break;
                    case SplitInstallSessionStatus.INSTALLED:
                        if (26 <= Build.VERSION.SDK_INT) {
                            Log.d("angnuo_test", String.valueOf(Build.VERSION.SDK_INT));
                            //SplitInstallHelper.updateAppInfo(getApplicationContext());
                        }
                        //showRestartDialog();
                        break;
                }
            }
        }
    }




    private void onSuccessfulLoad(String moduleName, boolean launch) {
        if (launch) {
            try {
                launchAcitivity(moduleName);
            } catch (Exception e) {
                StringWriter erros = new StringWriter();
                e.printStackTrace(new PrintWriter(erros));
                //Log.d(TAG, erros.toString());
                // Log.d(TAG, getBaseContext().getPackageCodePath());
            }
        }
    }

    private void launchAcitivity(String className) {
        Log.d("MainA", "Zichong");
        Log.d("MainA", context.getPackageName());
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setClassName(context.getPackageName(), "com.example.dynamicfeature.AboutActivity");
        //startActivity(intent);
    }

}