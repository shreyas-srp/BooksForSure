package booksforsure.galaxyworks.com.booksforsure;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import booksforsure.galaxyworks.com.booksforsure.History;
import booksforsure.galaxyworks.com.booksforsure.R;

/**
 * Created by Aqeel on 15-02-2016.
 */
public class PushMessageBroadcast extends ParsePushBroadcastReceiver {
    String title,content,TAG="HAHAHAHAH";
    @Override
    public void onPushOpen(Context context, Intent intent) {
        //Log.d("The push", "open");

    }



    @Override
    protected Notification getNotification(Context context, Intent intent) {
        // TODO Auto-generated method stub
        return super.getNotification(context, intent);
    }

    @Override
    protected void onPushDismiss(Context context, Intent intent) {
        // TODO Auto-generated method stub
        super.onPushDismiss(context, intent);
    }

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        Parse.setLogLevel(Parse.LOG_LEVEL_VERBOSE);
        Log.i(TAG, "Notification received");
        Bundle extras = intent.getExtras();
        String message = extras != null ? extras.getString("com.parse.Data") : "error";
        JSONObject jObject;
        String alert = null;
        String title = null;
        String orderid = null;
        try {
            jObject = new JSONObject(message);
            alert = (jObject.getString("alert"));
            title = (jObject.getString("title"));
            //orderid = (jObject.getString("orderid"));
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i(TAG,"alert is " + alert);
        Log.i(TAG,"title is " + title);
        NotificationCompat.Builder notification =
                (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.bfs5)
                        .setContentTitle(alert)
                        .setContentText(title);
        //    notification.flags |= Notification.FLAG_AUTO_CANCEL | Notification.FLAG_SHOW_LIGHTS;
// create intent to start your activity
        Intent activityIntent = new Intent(context, History.class);

// create pending intent and add activity intent
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, activityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);

// Add as notification
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notification.setAutoCancel(true);
        notification.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(1, notification.build());

    }
}

