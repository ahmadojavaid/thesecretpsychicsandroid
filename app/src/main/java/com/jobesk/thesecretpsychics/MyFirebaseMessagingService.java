package com.jobesk.thesecretpsychics;


import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.jobesk.thesecretpsychics.AdvisorActivities.AdvisorChat.AdvisorGetChat;
import com.jobesk.thesecretpsychics.AdvisorActivities.AdvisorDrawerActivity;
import com.jobesk.thesecretpsychics.AdvisorActivities.AdvisorEventBus.AdvisorOrderRefresh;
import com.jobesk.thesecretpsychics.ClientActivities.Chat.ClientGetChat;
import com.jobesk.thesecretpsychics.ClientActivities.ClientDrawerActivity;
import com.jobesk.thesecretpsychics.ClientActivities.EventBusClient.EventOrderRefresh;
import com.jobesk.thesecretpsychics.SplashScreen.WelcomeActivity;
import com.jobesk.thesecretpsychics.SplashScreen.WelcomeScreen_5;
import com.jobesk.thesecretpsychics.Utils.GlobalClass;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Random;

import static android.support.v4.app.NotificationCompat.BADGE_ICON_SMALL;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private SharedPreferences sharedpreferences;
    private Intent intent = null;
    private String NOTIFICATION_CHANNEL_ID = "1";
    private String type = "";
    private String body, reciever_id, sender_id, chatRate, profileImage, senderName;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        Log.d("remoteMessage", "FROM:" + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {
            Log.d("remoteMessage", "Message data: " + remoteMessage.getData());
        }
        if (remoteMessage.getNotification() != null) {
            Log.d("remoteMessage", "Mesage notification:" + remoteMessage.getNotification().getBody());

        }


        type = remoteMessage.getData().get("type");
        String userType = GlobalClass.getPref("userType", getApplicationContext());
        if (userType.equalsIgnoreCase("client")) {

            String clientID = GlobalClass.getPref("clientID", getApplicationContext());

            if (type.equals("chatMsg")) {
                body = remoteMessage.getData().get("body");
                sender_id = remoteMessage.getData().get("sender_id");
                reciever_id = remoteMessage.getData().get("reciever_id");
                senderName = remoteMessage.getData().get("senderName");
                profileImage = remoteMessage.getData().get("profileImage");
                chatRate = remoteMessage.getData().get("chat_rate");


                if (isAppIsInBackground(getApplicationContext()) == true) {

                    if (clientID.equals(sender_id)) {
                        sender_id = reciever_id;

                    } else {
                        sender_id = sender_id;

                    }

                    intent = new Intent(this, WelcomeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("sendTo", sender_id);
                    bundle.putString("sendBy", clientID);
                    bundle.putString("preName", senderName);
                    bundle.putString("preChatRate", chatRate);
                    bundle.putString("type", "chat");
                    bundle.putString("preImage", profileImage);
                    intent.putExtras(bundle);

                } else {
                    if (clientID.equals(sender_id)) {
                        sender_id = reciever_id;

                    } else {
                        sender_id = sender_id;

                    }
                    intent = new Intent(this, ClientGetChat.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("sendTo", sender_id);
                    bundle.putString("sendBy", clientID);
                    bundle.putString("preName", senderName);
                    bundle.putString("preChatRate", chatRate);
                    bundle.putString("type", "chat");
                    bundle.putString("preImage", profileImage);
                    intent.putExtras(bundle);

                }

                CreateNoti(remoteMessage);
            } else {


                if (isAppIsInBackground(getApplicationContext()) == true) {

                    intent = new Intent(this, WelcomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    body = remoteMessage.getData().get("body");
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "order");
                    intent.putExtras(bundle);


                } else {
// event bus to refresh orders


                    intent = new Intent(this, WelcomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    body = remoteMessage.getData().get("body");
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "order");
                    intent.putExtras(bundle);

                    EventBus.getDefault().post(new EventOrderRefresh());
                }

                CreateNoti(remoteMessage);
            }

        } else {
            String advisorID = GlobalClass.getPref("advisorID", getApplicationContext());

            if (type.equals("chatMsg")) {

                body = remoteMessage.getData().get("body");
                sender_id = remoteMessage.getData().get("sender_id");
                reciever_id = remoteMessage.getData().get("reciever_id");
                senderName = remoteMessage.getData().get("senderName");

                profileImage = remoteMessage.getData().get("profileImage");

                if (isAppIsInBackground(getApplicationContext()) == true) {


                    if (advisorID.equals(sender_id)) {
                        sender_id = reciever_id;

                    } else {
                        sender_id = sender_id;

                    }


                    intent = new Intent(this, WelcomeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("sendTo", advisorID);
                    bundle.putString("sendBy", sender_id);
                    bundle.putString("preName", senderName);
                    bundle.putString("preChatRate", "10");
                    bundle.putString("type", "chat");
                    bundle.putString("preImage", profileImage);
                    intent.putExtras(bundle);

                } else {


                    if (advisorID.equals(sender_id)) {
                        sender_id = reciever_id;

                    } else {
                        sender_id = sender_id;

                    }

                    intent = new Intent(this, AdvisorGetChat.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("sendTo", advisorID);
                    bundle.putString("sendBy", sender_id);
                    bundle.putString("preName", senderName);
                    bundle.putString("preChatRate", "10");
                    bundle.putString("type", "chat");
                    bundle.putString("preImage", profileImage);
                    intent.putExtras(bundle);
                    intent.putExtras(bundle);

                }
                CreateNoti(remoteMessage);
            } else {


                if (isAppIsInBackground(getApplicationContext()) == true) {
                    intent = new Intent(this, WelcomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    body = remoteMessage.getData().get("body");
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "order");
                    intent.putExtras(bundle);


                } else {
                    // event bus to refresh orders

                    intent = new Intent(this, WelcomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    body = remoteMessage.getData().get("body");
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "order");
                    intent.putExtras(bundle);


                    EventBus.getDefault().post(new AdvisorOrderRefresh());
                }


                CreateNoti(remoteMessage);
            }

        }


    }

    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    private void CreateNoti(RemoteMessage mRemoteMsg) {

        String channelName = getApplicationContext().getResources().getString(R.string.miscellaneous);
        Random r = new Random();
        int randomNo = r.nextInt(9999999 - 0) + 0;


//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);


        NotificationManager notificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new
                    NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName,
                    NotificationManager.IMPORTANCE_DEFAULT);

            String channelDesCriptions = getApplicationContext().getResources().getString(R.string.all_notifications);
            notificationChannel.setDescription(channelDesCriptions);
            notificationChannel.enableLights(true);

            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500,
                    1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);


        }


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setVibrate(new long[]{0, 100, 100, 100, 100, 100})
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getApplicationContext().getResources().getString(R.string.app_name))
                .setContentText(body)
                .setBadgeIconType(BADGE_ICON_SMALL)
                .setBadgeIconType(1);


        builder.setContentIntent(pendingIntent);
        notificationManager.notify(randomNo, builder.build());

    }

}
