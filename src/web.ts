import { WebPlugin } from '@capacitor/core';
import { UpdateCheckerPlugin } from './definitions';

export class UpdateCheckerWeb extends WebPlugin implements UpdateCheckerPlugin {
  constructor() {
    super({
      name: 'UpdateChecker',
      platforms: ['web'],
    });
  }

  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }

  async getAppVersion(): Promise<{versionName?: string, versionCode?: number}> {
    return {};
  }
  
  async runUpdateChecker() {
    return null;
  }

  async completeUpdate() {
    return null;
  }

  // addListener(eventName: string, listenerFunc: (data: any) => void) {

  // }
}

const UpdateChecker = new UpdateCheckerWeb();

export { UpdateChecker };

import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(UpdateChecker);
