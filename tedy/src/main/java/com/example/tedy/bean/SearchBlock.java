package com.example.tedy.bean;


import com.example.tedy.serchMethod.SearchMethod;

public class SearchBlock {
    private String searchName;
    private String urlScheme;
    private Float x;
    private Float y;
    private String jsonStr;
    private byte[] apkIcon;
    private SearchType type;

    private SearchMethod searchMethod;

    public SearchMethod getSearchMethod() {
        return searchMethod;
    }

    public void setSearchMethod(SearchMethod searchMethod) {
        this.searchMethod = searchMethod;
    }

    public SearchBlock(String searchName, String urlScheme, String jsonStr, byte[] apkIcon, SearchType type) {
        this.searchName = searchName;
        this.urlScheme = urlScheme;
        this.jsonStr = jsonStr;
        this.apkIcon = apkIcon;
        this.type = type;
    }

    public SearchBlock(String searchName, String urlScheme, String jsonStr, SearchType type) {
        this.searchName = searchName;
        this.urlScheme = urlScheme;
        this.jsonStr = jsonStr;
        this.type = type;
    }

    public SearchBlock(String searchName, String urlScheme, Float x, Float y, String jsonStr, SearchType type) {
        this.searchName = searchName;
        this.urlScheme = urlScheme;
        this.x = x;
        this.y = y;
        this.jsonStr = jsonStr;
        this.type = type;
    }


    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public String getUrlScheme() {
        return urlScheme;
    }

    public void setUrlScheme(String urlScheme) {
        this.urlScheme = urlScheme;
    }

    public String getJsonStr() {
        return jsonStr;
    }

    public void setJsonStr(String jsonStr) {
        this.jsonStr = jsonStr;
    }

    public byte[] getApkIcon() {
        return apkIcon;
    }

    public void setApkIcon(byte[] apkIcon) {
        this.apkIcon = apkIcon;
    }

    public SearchType getType() {
        return type;
    }

    public void setType(SearchType type) {
        this.type = type;
    }
}
