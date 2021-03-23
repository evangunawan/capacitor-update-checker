import { PluginListenerHandle } from "@capacitor/core";

declare module '@capacitor/core' {
  interface PluginRegistry {
    UpdateChecker: UpdateCheckerPlugin;
  }
}

export interface UpdateCheckerPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
  getAppVersion(): Promise<{versionName: string, versionCode: number}>;
  runUpdateChecker(): Promise<any>;
  completeUpdate(): Promise<any>;
  addListener(
    eventName: string,
    listenerFunc: (state: any) => void,
  ): PluginListenerHandle;
}
