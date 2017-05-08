package com.example.android.sunshine.sync;

import android.content.Context;
import android.content.Intent;

// TODOCOMPLETED (9) Create a class called SunshineSyncUtils
public class SunshineSyncUtils {

    //  TODOCOMPLETED (10) Create a public static void method called startImmediateSync
    public static void startImmediateSync(Context context) {
        Intent sunshineSyncServiceIntent = new Intent(context, SunshineSyncIntentService.class);
        context.startService(sunshineSyncServiceIntent);
    }
    //  TODOCOMPLETED (11) Within that method, start the SunshineSyncIntentService
}