package kr.ac.pusan.cs.bookforyou;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Noti_TEMP extends AppCompatActivity {
    private BackPressCloseHandler backPressCloseHandler;
    String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";
    NotificationManager mNotificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
        mNotificationManager =(NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        backPressCloseHandler = new BackPressCloseHandler(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);
            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap mLargeIconForNoti =
                        BitmapFactory.decodeResource(getResources(),R.drawable.firebase_lockup_400);
                PendingIntent mPendingIntent = PendingIntent.getActivity(
                        Noti_TEMP.this,
                        0,
                        new Intent(getApplicationContext(),Noti_TEMP.class),
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(Noti_TEMP.this,NOTIFICATION_CHANNEL_ID)
                                .setSmallIcon(R.drawable.firebase_lockup_400)
                                .setContentTitle("노티제목")
                                .setContentText("노티내용")
                                .setDefaults(NotificationCompat.DEFAULT_SOUND)
                                .setLargeIcon(mLargeIconForNoti)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setAutoCancel(true)
                                .setContentIntent(mPendingIntent)
                        ;
                mNotificationManager.notify(0,mBuilder.build());
            }
        });

    }
    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }


}
