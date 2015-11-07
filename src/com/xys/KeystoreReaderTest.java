package com.xys;

import com.xys.certificatereader.KeystoreInfo;
import com.xys.certificatereader.KeystoreReader;
import com.xys.installchecker.InstallChecker;

/**
 * Created by XiongYongshun on 15/8/17.
 * yongshun1228@gmail.com
 */
public class KeystoreReaderTest {
    public static void main(String[] argv) throws Exception {

        KeystoreInfo info = KeystoreReader.getKeystoreInfo("/Users/xiongyongshun/works/MySignedKey.jks", "158158", "mysignedkey");

        InstallChecker.initKeystoreInfo(info);


        System.out.println("Can install?" + InstallChecker.check("/Users/xiongyongshun/Downloads/temp/wandoujia_signed.apk"));
//
//        KeystoreInfo info2 = KeystoreReader.getKeystoreInfo("/Users/xiongyongshun/Downloads/temp/publickey.cert");
//        System.out.print(info2.toString());
    }
}
