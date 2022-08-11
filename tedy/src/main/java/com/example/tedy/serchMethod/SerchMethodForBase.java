package com.example.tedy.serchMethod;

import android.content.Context;

import com.example.tedy.util.My_AndroidUtil;

public class SerchMethodForBase implements SearchMethod {
    @Override
    public void search(CharSequence s,String urlScheme, Context context) {
        My_AndroidUtil.filterInput(s);
    }
}
