package com.example.tedy.serchMethod;


import android.content.Context;

import java.io.Serializable;

public interface SearchMethod extends Serializable {
    void search(CharSequence s,String urlScheme, Context context);
}
