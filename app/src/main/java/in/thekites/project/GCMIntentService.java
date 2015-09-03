package in.thekites.project;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import static in.thekites.project.CommonUtilities.*;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {

    static String message="";
	private static final String TAG = "GCMIntentService";
    SQLiteDatabase db;
    public GCMIntentService() {
        super(SENDER_ID);
    }

    /**
     * Method called on device registered
     **/
    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.i(TAG, "Device registered: regId = " + registrationId);
        displayMessage(context, "Your device registred with GCM");
  //      Log.d("NAME", MainActivity.name);

        ServerUtilities.register(context,registrationId,MainActivity.deviceid);
    }

    /**
     * Method called on device un registred
     * */
    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");
     //   displayMessage(context, getString(R.string.gcm_unregistered));
        ServerUtilities.unregister(context, registrationId);
    }

    /**
     * */
    @Override
    protected void onMessage(Context context, Intent intent) {
        Log.i(TAG, "Received message");
             message = intent.getExtras().getString("product");
            String fnub = intent.getExtras().getString("fnum");

         try {
            db=openOrCreateDatabase("chats.db",SQLiteDatabase.CREATE_IF_NECESSARY,null);
         }
         catch(Exception ex)
            {
                Toast.makeText(GCMIntentService.this,ex.toString(),Toast.LENGTH_LONG).show();
                Log.e("Exception DB", ex.toString());
            }
        try
        {
            db.execSQL("create table chat(mymsg text,fnum text,msgfrom text)");
        Log.d("test","test");
        }
        catch(Exception ex)
        {
            Toast.makeText(GCMIntentService.this,ex.toString(),Toast.LENGTH_LONG).show();
            Log.e("Exception DB",ex.toString());
        }
    try
    {
        ContentValues cvchat=new ContentValues();
        cvchat.put("mymsg","");
        cvchat.put("fnum",fnub);
        cvchat.put("msgfrom",message);
        db.insert("chat",null,cvchat);
        System.out.println("advc");
    }
        catch(Exception ex)
        {
            Toast.makeText(GCMIntentService.this,ex.toString(),Toast.LENGTH_LONG).show();
            Log.e("Exception DB",ex.toString());
        }

        System.out.println("adcadv"+message);

        //displayMessage(context, message);

        new SubChat().update(message);

        message=fnub+": "+message;

        // notifies user
        generateNotification(context, message);
    }

    /**
     * */
    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");
        String message = getString(R.string.action_settings, total);
        displayMessage(context, message);
        // notifies user
        generateNotification(context, message);
    }

    /**
     * Method called on Error
     * */
    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
        displayMessage(context, getString(R.string.action_settings, errorId));
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
        displayMessage(context, getString(R.string.action_settings,
                errorId));
        return super.onRecoverableError(context, errorId);
    }

    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    private static void generateNotification(Context context, String message) {
        int icon = R.drawable.ic_launcher;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(icon, message, when);
        
        //String title = context.getString(R.string.app_name);
        String title="New Message";
        Intent notificationIntent = new Intent(context,SubChat.class);
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent =
                PendingIntent.getActivity(context, 0, notificationIntent, 0);
        notification.setLatestEventInfo(context, title, message, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        
        // Play default notification sound
        notification.defaults |= Notification.DEFAULT_SOUND;
        
        //notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "your_sound_file_name.mp3");
        
        // Vibrate if vibrate is enabled
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(0, notification);      

    }
}