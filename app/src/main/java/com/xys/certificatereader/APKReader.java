package com.xys.certificatereader;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashSet;
/**
 * Created by XiongYongshun on 15/8/17.
 * yongshun1228@gmail.com
 */
public class APKReader {

    public static KeystoreInfo getKeystoreInfo(String apk_path) {
        Signature[] signatures = getSignatures(apk_path);
        KeystoreInfo info = new KeystoreInfo();

        if (signatures == null) {
            return info;
        }

        try {
            final CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            final ByteArrayInputStream bais = new ByteArrayInputStream(signatures[0].toByteArray());
            final X509Certificate cert = (X509Certificate)certFactory.generateCertificate(bais);

            info.pubkey = cert.getPublicKey();
            info.serialNumber = cert.getSerialNumber().toString();
            info.sigAlgName = cert.getSigAlgName();
            info.subjectDN = cert.getSubjectDN().toString();

        }catch (CertificateException e) {
            e.printStackTrace();
        }
        return info;
    }

//
//    private static Signature[] getSignatures(String archiveFilePath){
//        PackageParser packageParser = new PackageParser(archiveFilePath);
//        DisplayMetrics metrics = new DisplayMetrics();
//        metrics.setToDefaults();
//        final File sourceFile = new File(archiveFilePath);
//        PackageParser.Package pkg = packageParser.parsePackage(
//                sourceFile, archiveFilePath, metrics, 0);
//        if (pkg == null) {
//            return null;
//        }
//
//        packageParser.collectCertificates(pkg, 0);
//
//        PackageUserState state = new PackageUserState();
//        int flags = PackageManager.GET_SIGNATURES;
//        return PackageParser.generatePackageInfo(pkg, null, flags, 0, 0, null, state).signatures;
//    }

    private static Signature[] getSignatures(String archiveFilePath) {
        //必须使用反射机制才能获取未安装apk的签名信息
        final String PACKAGEPARSER_CLASS_NAME = "android.content.pm.PackageParser";
        final String PARSEPACKAGE_METHOD_NAME = "parsePackage";
        final String SUBCLASS_PACKAGE_CLASS_NAME = "Package";
        final String COLLECTCERTIFICATES_METHOD_NAME = "collectCertificates";
        final String GENERATEPACKAGEINFO_METHOD_NAME = "generatePackageInfo";
        try {
            // 根据反射获取隐藏类
            Class<?> parserClass = Class.forName(PACKAGEPARSER_CLASS_NAME);
            Class<?> packageUserState = Class.forName("android.content.pm.PackageUserState");

            // 获取类的构造方法
            Constructor<?> cons = parserClass.getConstructor(String.class);

            // 获取类的构造方法
            Constructor<?> packageUserState_cons = packageUserState.getConstructor();
            Object state = packageUserState_cons.newInstance();

            // 调用构造方法，构造类实例
            Object packageParser = cons.newInstance(archiveFilePath);

            // 获取名为"parsePackage"的方法
            Method parseMethod = parserClass.getMethod(PARSEPACKAGE_METHOD_NAME, File.class, String.class, DisplayMetrics.class, int.class);

            // 遍历内部类, 找到Package内部类
            Class<?>[] subClasses = parserClass.getClasses();
            Class<?> packageClass = null;
            for (int i = 0; i < subClasses.length; i++) {
                if (subClasses[i].getName()
                        .equals(PACKAGEPARSER_CLASS_NAME + "$" + SUBCLASS_PACKAGE_CLASS_NAME)) {
                    packageClass = subClasses[i];
                    break;
                        }
            }

            DisplayMetrics metrics = new DisplayMetrics();

            final File sourceFile = new File(archiveFilePath);
            // 调用parseMethod方法，获取Package的实例
            Object pkg = parseMethod.invoke(packageParser, sourceFile, archiveFilePath, metrics, 0);
            if (pkg == null) {
                Log.e("xys", "Failed to parse package");
                return null;
            }

            // 获取名为"collectCertificates"的方法
            Method certMethod = parserClass.getMethod(COLLECTCERTIFICATES_METHOD_NAME, packageClass, int.class);

            // 调用collectCertificates方法，收集证书
            certMethod.invoke(packageParser, pkg, 0);

            // 获取名为"generatePackageInfo"的方法
            Method genMethod = parserClass.getMethod(GENERATEPACKAGEINFO_METHOD_NAME, packageClass, int[].class, int.class, long.class, long.class, HashSet.class, packageUserState);

            // 最后调用generatePackageInfo方法，得到签名信息
            PackageInfo pi = (PackageInfo)genMethod.invoke(packageParser, pkg, null, PackageManager.GET_SIGNATURES, 0, 0, null, state);
            return pi.signatures;

        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
