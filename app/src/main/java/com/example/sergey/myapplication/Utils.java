package com.example.sergey.myapplication;

import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import me.drakeet.support.toast.ToastCompat;

public class Utils {
    public static Toast makeToast(Context context, CharSequence text, int duration) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {
            return ToastCompat.makeText(context, text, duration);
        } else {
            return Toast.makeText(context, text, duration);
        }
    }
}
