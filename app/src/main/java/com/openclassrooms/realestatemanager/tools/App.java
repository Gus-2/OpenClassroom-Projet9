package com.openclassrooms.realestatemanager.tools;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

public class App extends Application {

    public static final String CHANNEL_1_ID = "add_house";
    public static final String CHANNEL_1_NAME = "Add House Successfully";
    public static final String CHANNEL_1_DESCRIPTION = "Notification when a house is added successfully";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    CHANNEL_1_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription(CHANNEL_1_DESCRIPTION);

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel1);
            } else {
                Log.e("ERROR", "Couldn't set channel for notification manage !");
            }
        }
    }
}
