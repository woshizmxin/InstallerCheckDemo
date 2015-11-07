package com.xys.installchecker;

import android.util.Log;

import com.xys.certificatereader.APKReader;
import com.xys.certificatereader.KeystoreInfo;
/**
 * Created by XiongYongshun on 15/8/17.
 * yongshun1228@gmail.com
 */
public class InstallChecker {
    private static KeystoreInfo keystoreInfo = null;
    public static void initKeystoreInfo(KeystoreInfo info) {
        keystoreInfo = info;
    }
    public static boolean check(String apk_path) {
        if (keystoreInfo == null) {
            throw new NullPointerException();
        }
        KeystoreInfo apk_info = APKReader.getKeystoreInfo(apk_path);

        Log.d("xys", "APK_INFO: " + apk_info.toString());
        Log.d("xys", "KeystoreInfo " + keystoreInfo.toString());

        return keystoreInfo.pubkey.equals(apk_info.pubkey);
    }
}
