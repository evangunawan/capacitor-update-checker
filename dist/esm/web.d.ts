import { WebPlugin } from '@capacitor/core';
import { UpdateCheckerPlugin } from './definitions';
export declare class UpdateCheckerWeb extends WebPlugin implements UpdateCheckerPlugin {
    constructor();
    echo(options: {
        value: string;
    }): Promise<{
        value: string;
    }>;
    getAppVersion(): Promise<{
        versionName?: string;
        versionCode?: number;
    }>;
    runUpdateChecker(): Promise<null>;
    completeUpdate(): Promise<null>;
}
declare const UpdateChecker: UpdateCheckerWeb;
export { UpdateChecker };
