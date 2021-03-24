var updateCheckerPlugin = (function (exports, core) {
    'use strict';

    var __awaiter = (undefined && undefined.__awaiter) || function (thisArg, _arguments, P, generator) {
        function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
        return new (P || (P = Promise))(function (resolve, reject) {
            function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
            function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
            function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
            step((generator = generator.apply(thisArg, _arguments || [])).next());
        });
    };
    class UpdateCheckerWeb extends core.WebPlugin {
        constructor() {
            super({
                name: 'UpdateChecker',
                platforms: ['web'],
            });
        }
        echo(options) {
            return __awaiter(this, void 0, void 0, function* () {
                console.log('ECHO', options);
                return options;
            });
        }
        getAppVersion() {
            return __awaiter(this, void 0, void 0, function* () {
                return {};
            });
        }
        runUpdateChecker() {
            return __awaiter(this, void 0, void 0, function* () {
                return null;
            });
        }
        completeUpdate() {
            return __awaiter(this, void 0, void 0, function* () {
                return null;
            });
        }
    }
    const UpdateChecker = new UpdateCheckerWeb();
    core.registerWebPlugin(UpdateChecker);

    exports.UpdateChecker = UpdateChecker;
    exports.UpdateCheckerWeb = UpdateCheckerWeb;

    Object.defineProperty(exports, '__esModule', { value: true });

    return exports;

}({}, capacitorExports));
//# sourceMappingURL=plugin.js.map
