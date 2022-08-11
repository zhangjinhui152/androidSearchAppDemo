package com.example.tedy.bean;

public class ApKData {
    public String pkgName;
    public String apkName;
    public String firstName;
    public byte[] apkIcon;

    public byte[] getApkIcon() {
        return apkIcon;
    }

    public void setApkIcon(byte[] apkIcon) {
        this.apkIcon = apkIcon;
    }



    public ApKData(String pkgName, String apkName, String firstName, byte[] apkIcon) {
        this.pkgName = pkgName;
        this.apkName = apkName;
        this.firstName = firstName;
        this.apkIcon = apkIcon;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public String getApkName() {
        return apkName;
    }

    public void setApkName(String apkName) {
        this.apkName = apkName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String toString() {
        return "ApKData{" +
                "pkgName='" + pkgName + '\'' +
                ", apkName='" + apkName + '\'' +
                ", firstName='" + firstName + '\'' +
                '}';
    }
}
