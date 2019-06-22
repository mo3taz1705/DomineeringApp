package com.example.gpc.domineering.utils;

import android.content.Context;
import android.widget.Toast;

public class Utils {
    public static void showToast(Context context, String toastMessage){
        Toast toast = Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT);
        toast.show();
    }
}
