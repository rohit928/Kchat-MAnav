package in.thekites.project;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import in.thekites.project.util.SystemUiHider;

public class Scheduler extends Activity {

    DatePicker pickerDate;
    TimePicker pickerTime;
    Button buttonSetAlarm;
    TextView info;
    EditText set;
    String fnums="";
    JSONParser jsonParser = new JSONParser();
String msg="";
    String fnum="";
    final static int RQS_1 = 1;
SQLiteDatabase dbz;
    private static String url = "http://jsr.esy.es/kchat/send_message.php";
String TAG="success";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduler);
        try {
            Bundle bundle = getIntent().getExtras();
            fnum = bundle.getString("fnum");
        }
        catch(Exception ex)
        {
            // catch Except
        }



        info = (TextView)findViewById(R.id.info);
        pickerDate = (DatePicker)findViewById(R.id.pickerdate);
        pickerTime = (TimePicker)findViewById(R.id.pickertime);
        set=(EditText)findViewById(R.id.mymsg);
        Calendar now = Calendar.getInstance();

        try {
                pickerDate.init(
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH),
                null);
            pickerTime.setCurrentHour(now.get(Calendar.HOUR_OF_DAY));
            pickerTime.setCurrentMinute(now.get(Calendar.MINUTE));
            }

        catch (Exception ex)
        {
            // catch Exception here
        }
        buttonSetAlarm = (Button)findViewById(R.id.setalarm);
        buttonSetAlarm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Calendar current = Calendar.getInstance();

                Calendar cal = Calendar.getInstance();
                cal.set(pickerDate.getYear(),
                        pickerDate.getMonth(),
                        pickerDate.getDayOfMonth(),
                        pickerTime.getCurrentHour(),
                        pickerTime.getCurrentMinute(),
                        00);

                if (cal.compareTo(current) <= 0) {
                    //The set Date/Time already passed
                    Toast.makeText(getApplicationContext(),
                            "Invalid Date/Time",
                            Toast.LENGTH_LONG).show();
                } else {
                    setAlarm(cal);
                }

            }
        });
    }

    private void setAlarm(Calendar targetCal){


        try{
            dbz=openOrCreateDatabase("chats.db",SQLiteDatabase.CREATE_IF_NECESSARY,null);
            dbz.execSQL("create table chat(mymsg text,fnum text,msgfrom text)");

        }
        catch (Exception ex)
        {

        }
try
{
    ContentValues cv=new ContentValues();
    cv.put("mymsg",set.getText().toString()+"");
    cv.put("fnum",fnum);
    cv.put("msgfrom","");
    dbz.insert("chat",null,cv);
}
catch(Exception ex)
{

}
        Intent intent = new Intent(getBaseContext(), MessageReciver.class);
        intent.putExtra("fnum",fnum);
        intent.putExtra("message",set.getText().toString()+"");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), RQS_1, intent, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
      //  System.out.print(fnum+"effwef");
     //   Intent send=new Intent(Scheduler.this,SubChat.class);
       // startActivity(send);
        finish();

    }

public void schedule(String num,String message)
{
fnums=message;
    msg=num;
try {
    new CreateChatwithTimer().execute();
}
catch (Exception ex)
{

}
}

    class CreateChatwithTimer extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {


            super.onPreExecute();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {
/*
           try{
                dbz=openOrCreateDatabase("scheduler.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
                Cursor cr=dbz.rawQuery("select * from schedule",null);
                cr.moveToFirst();
                do {
message=cr.getString(0);
                    fnum=cr.getString(1);


                }
                while (cr.moveToNext());

            }
            catch(Exception ex)
            {

            }*/

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
params.add(new BasicNameValuePair("fnum",fnums));
            params.add(new BasicNameValuePair("message",msg));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url,
                    "GET", params);


            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG);

                if (success == 1) {



                } else {
                    // failed to create product
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done


        }

    }





}
