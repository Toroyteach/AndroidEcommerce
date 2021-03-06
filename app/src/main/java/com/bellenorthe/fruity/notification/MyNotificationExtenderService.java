package com.bellenorthe.fruity.notification;

import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationDisplayedResult;
import com.onesignal.OSNotificationReceivedResult;

import java.math.BigInteger;

public class MyNotificationExtenderService extends NotificationExtenderService {
    @Override
    protected boolean onNotificationProcessing(OSNotificationReceivedResult receivedResult) {
        OverrideSettings overrideSettings = new OverrideSettings();
        overrideSettings.extender = new NotificationCompat.Extender() {
            @Override
            public NotificationCompat.Builder extend(NotificationCompat.Builder builder) {
                // Sets the background notification color to Red on Android 5.0+ devices.
                /*Bitmap icon = BitmapFactory.decodeResource(CustomApplication.getContext().getResources(),
                        R.mipmap.logo);
                builder.setLargeIcon(icon);*/
                return builder.setColor(new BigInteger("FF0000FF", 16).intValue());
            }
        };


        OSNotificationDisplayedResult displayedResult = displayNotification(overrideSettings);

        Log.d("OneSignalExample", "Notification displayed with id: " + displayedResult.androidNotificationId);

        return true;
    }
}
