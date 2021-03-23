package id.periksa.plugin.updatechecker;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;

import com.getcapacitor.ui.Toast;
import com.google.android.play.core.appupdate.*;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.google.android.play.core.install.model.ActivityResult.RESULT_IN_APP_UPDATE_FAILED;

@NativePlugin(requestCodes = { UpdateChecker.REQUEST_FLEXIBLE })
public class UpdateChecker extends Plugin {
    protected static final int REQUEST_FLEXIBLE = 15;

    private AppUpdateManager _updateManager;
    private InstallStateUpdatedListener _installStateListener;

    public void load() {
        this._updateManager = AppUpdateManagerFactory.create(this.getContext());
    }

    @PluginMethod
    public void echo(PluginCall call) {
        String value = call.getString("value");

        JSObject ret = new JSObject();
        ret.put("value", value);
        call.resolve(ret);
    }

    @PluginMethod
    public void getAppVersion(PluginCall call) {
        try {
            PackageInfo pInfo = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0);
            String version = pInfo.versionName;
            int code = pInfo.versionCode;

            JSObject ret = new JSObject();
            ret.put("versionName", version);
            ret.put("versionCode", code);
            call.resolve(ret);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            call.reject("Something Error!");
        }
    }

    @PluginMethod
    public void runUpdateChecker(PluginCall call) {
        saveCall(call);

        this._installStateListener = state -> {
            int installStatus = state.installStatus();
            JSObject ret = new JSObject();
            ret.put("installStatus", installStatus);
            if (installStatus == InstallStatus.DOWNLOADING) {
                ret.put("bytesDownloaded", state.bytesDownloaded());
                ret.put("totalBytesToDownload", state.totalBytesToDownload());
            }
            notifyListeners("installStatus", ret);
        };

        this._updateManager.registerListener(this._installStateListener);
        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = this._updateManager.getAppUpdateInfo();
        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            JSObject ret = new JSObject();
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                // Request the update.
                try {
                    ret.put("code", "UPDATE_AVAILABLE");
                    ret.put("message", "Update available");
                    call.resolve(ret);
                    this._updateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.FLEXIBLE, this.getBridge().getActivity(), 0);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            } else {
                ret.put("code", "UPDATE_NOT_AVAILABLE");
                ret.put("message", "Update not available");
                call.resolve(ret);
            }
        });
    }

    @PluginMethod
    public void completeUpdate(PluginCall call) {
        this._updateManager.completeUpdate();
        call.resolve();
    }

    @Override
    protected void handleOnActivityResult(int requestCode, int resultCode, Intent data) {
        super.handleOnActivityResult(requestCode, resultCode, data);
        PluginCall savedCall = getSavedCall();

        JSObject ret = new JSObject();
        if (resultCode == RESULT_OK) {
            ret.put("code", "RESULT_OK");
            ret.put("message", "The user has accepted the update.");
        } else if (resultCode == RESULT_CANCELED) {
            ret.put("code", "RESULT_CANCELED");
            ret.put("message", "The user has denied or cancelled the update.");
        } else if (resultCode == RESULT_IN_APP_UPDATE_FAILED) {
            ret.put("code", "RESULT_IN_APP_UPDATE_FAILED");
            ret.put("message", "Some other error prevented either the user from providing consent or the update to proceed.");
        } else {
            ret.put("code", "UNKNOWN");
            ret.put("message", "I don't know whats happening :/");
        }

        Toast.show(getContext(), "OnActivityResult: " + requestCode + ", " + resultCode);

        notifyListeners("updateResult", ret);
        savedCall.resolve(ret);

        if (requestCode == this.REQUEST_FLEXIBLE) {
            this._updateManager.unregisterListener(this._installStateListener);
            this._installStateListener = null;
        }
    }

    @Override
    protected void handleOnResume() {
        super.handleOnResume();
        this._updateManager.getAppUpdateInfo().addOnSuccessListener(updateInfo -> {
            if (updateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                JSObject ret = new JSObject();
                ret.put("installStatus", updateInfo.installStatus());
                notifyListeners("installStatus", ret);
            }
        });
    }
}
