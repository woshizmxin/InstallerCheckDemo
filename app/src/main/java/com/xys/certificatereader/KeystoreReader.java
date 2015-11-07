package com.xys.certificatereader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;


public class KeystoreReader {
    public static KeystoreInfo getKeystoreInfo(String certificateFilePath) throws Exception {

        File f = new File(certificateFilePath);
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));

        KeystoreInfo info = null;
        if (in.available() > 0) {
            X509Certificate cert = (X509Certificate)cf.generateCertificate(in);

            info = new KeystoreInfo();
            info.pubkey = cert.getPublicKey();
            info.serialNumber = cert.getSerialNumber().toString();
            info.sigAlgName = cert.getSigAlgName();
            info.subjectDN = cert.getSubjectDN().toString();
        }
        in.close();

        return info;
    }

    public static KeystoreInfo getKeystoreInfo(String keystorePath, String keystorePassword, String keystoreAlias) {
        InputStream is = null;
        try {
            // 读取keystore证书文件
            File file = new File(keystorePath);
            is = new FileInputStream(file);
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            // 输入密码
            keystore.load(is, keystorePassword.toCharArray());
            // 指定证书中项目的别名
            X509Certificate cert = (X509Certificate)keystore.getCertificate(keystoreAlias);

            KeystoreInfo info = new KeystoreInfo();
            info.pubkey = cert.getPublicKey();
            info.serialNumber = cert.getSerialNumber().toString();
            info.sigAlgName = cert.getSigAlgName();
            info.subjectDN = cert.getSubjectDN().toString();
            return info;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != is) try {
                is.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return new KeystoreInfo();
    }
}