import { PluginListenerHandle } from "@capacitor/core";
declare module '@capacitor/core' {
    interface PluginRegistry {
        UpdateChecker: UpdateCheckerPlugin;
    }
}
export interface UpdateCheckerPlugin {
    /**
     * __Android Only__
     *
     * Get app version (versionName and versionCode) from build.gradle
     * */
    getAppVersion(): Promise<{
        versionName?: string;
        versionCode?: number;
    }>;
    /**
     * __Android Only__
     *
     * Run update checker instance. Returns status wheter an update is available or not.
     * If update is available, it will automatically prompt update intent to user,
     * and download the update package when user tap the Update button.
     * Use `completeUpdate()` to finish update process.
     * */
    runUpdateChecker(): Promise<any>;
    /**
     * __Android Only__
     *
     * Complete and install an update when the update is successfully downloaded.
     * */
    completeUpdate(): Promise<any>;
    addListener(eventName: string, listenerFunc: (state: any) => void): PluginListenerHandle;
}
