package com.xys.certificatereader;

import java.security.PublicKey;

/**
 * Created by XiongYongshun on 15/8/17.
 * yongshun1228@gmail.com
 */
public class KeystoreInfo {
    public String sigAlgName;
    public String serialNumber;
    public PublicKey pubkey;
    public String subjectDN;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SigAlgName: " + sigAlgName + "\n");
        builder.append("SerialNumber: " + serialNumber + "\n");
        builder.append("PublicKey: " + pubkey + "\n");
        builder.append("SubjectDN: " + subjectDN + "\n");

        return builder.toString();
    }
}