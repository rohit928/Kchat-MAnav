package in.thekites.project;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MessageReciver extends BroadcastReceiver {

    @Override
    public void onReceive(Context arg0, Intent arg1) {
        Toast.makeText(arg0, "Notification Sent to"+arg1.getStringExtra("fnum"), Toast.LENGTH_LONG).show();
        //new Scheduler().new CreateChatwithTimer().execute();n
        new Scheduler().schedule(arg1.getStringExtra("message"),arg1.getStringExtra("fnum"));
        //Toast.makeText(arg0, "Notification Sent"+arg1.getStringExtra("message")+arg1.getStringExtra("fnum")+"", Toast.LENGTH_LONG).show();
    }

}