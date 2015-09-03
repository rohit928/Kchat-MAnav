package in.thekites.project;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SubChat extends Activity {

    SQLiteDatabase db;
int val=0;
    JSONParser jsonParser = new JSONParser();
    String fnum="",usrnum;

    // url to create new product
    private static String url_create_chat = "http://jsr.esy.es/kchat/Create_chat.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
        private static final String TAG = "ChatActivity";

        ChatArrayAdapter chatArrayAdapter;
        ListView listView;
        EditText chatText;
        Button buttonSend;
        static String name;
        Intent intent;
        static  private boolean side = false;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Intent i = getIntent();
            setContentView(R.layout.activity_sub_chat);

            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
            usrnum = pref.getString("Number",null);
//            Toast.makeText(this, usrnum, Toast.LENGTH_SHORT).show();

            try {
                Bundle b = getIntent().getExtras();
                fnum = b.getString("FrndNum");
                getActionBar().setTitle(fnum+"");
            }
            catch(Exception ex)
            {

            }

            buttonSend = (Button) findViewById(R.id.buttonSend);

            listView = (ListView) findViewById(R.id.listView1);
            chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.activity_chat_singlemessage);
            listView.setAdapter(chatArrayAdapter);


            try{
                db=openOrCreateDatabase("chats.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);

            }
            catch(Exception ex)
            {

            }

            try{
                Cursor cr=db.rawQuery("select * from chat where fnum='"+fnum+"'",null);
                cr.moveToFirst();
                do
                {
                    val++;
                if(cr.getString(0).equals("")) {
                side=true;
                chatArrayAdapter.add(new ChatMessage(side, cr.getString(2)));
                    // side = !side;
                }
                    else
                {
                side=false;
                chatArrayAdapter.add(new ChatMessage(side, cr.getString(0)));

                }
       //             Toast.makeText(SubChat.this, val + "", Toast.LENGTH_LONG).show();
                }

                while(cr.moveToNext());

            }
            catch(Exception ex)
            {

            }

            chatText = (EditText) findViewById(R.id.chatText);
            chatText.setOnKeyListener(new OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        String msg=chatText.getText().toString();
                       // chatArrayAdapter.add(new ChatMessage(false, chatText.getText().toString()));
                        return sendChatMessage(msg);
                      //  return true;
                    }
                    return false;
                }
            });
            buttonSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    // Toast.makeText(SubChat.this,"Toast",Toast.LENGTH_SHORT).show();
                    String msg = chatText.getText().toString();
                    sendChatMessage(msg);
                    try {
                        new CreateChat().execute();

                    } catch (Exception ex) {
                        // Do Check Internet
                    }

                }
            });

            listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
            listView.setAdapter(chatArrayAdapter);
/*
            private void scrollMyListViewToBottom() {
                listView.post(new Runnable() {
                    @Override
                    public void run() {
                        // Select the last row so it will scroll into view...
                        listView.setSelection(chatArrayAdapter.getCount() - 1);
                    }
                });
            }
*/
//            listView.scrollTo(0,1);
//            listView.setSelection(chatArrayAdapter.getCount() - 1);

            //to scroll the list view to bottom on data change
            chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    listView.setSelection(chatArrayAdapter.getCount() - 1);
                }
            });

        }

    public  void update(String msgfrom)
    {
        try{
//            Cursor cr=db.rawQuery("select * from chat where fnum='"+fnum+"'",null);
            // do


                side=true;
            ChatMessage chatMessage = new ChatMessage(side, msgfrom);
                chatArrayAdapter.add(chatMessage);
                Log.e("3333kasbckckbcdabckabv", msgfrom);
                //listView.setAdapter(chatArrayAdapter);
                chatArrayAdapter.notifyDataSetChanged();
                // side = !side;

            //while(cr.moveToNext());

        }
        catch(Exception ex)
        {
            System.out.println(ex.toString()+"sdsad");

        }



    }
        private boolean sendChatMessage(String msg){
            try{
           //     Cursor cr=db.rawQuery("select * from chat where fnum='"+fnum+"'",null);
             //   cr.moveToFirst();
               // do
                {
                    side=false;
                    chatArrayAdapter.add(new ChatMessage(side,msg));
                   // side = !side;

                }
          //      while(cr.moveToNext());
            }
            catch(Exception ex)
            {

            }

            return true;
        }

    /**
     * Background Async Task to Create new product
     * */
    class CreateChat extends AsyncTask<String, String, String> {

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
            name = chatText.getText().toString();

            System.out.println(name);

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("userNum",usrnum));
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("Fnum",fnum));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create_chat,
                    "POST", params);

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

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
            chatText.setText("");

            try
            {
                db.execSQL("create table chat (mymsg text,fnum text,msgfrom text)");
            }
            catch(Exception ex)
            {

            }
            try
            {
                ContentValues cvchat=new ContentValues();
                cvchat.put("mymsg",name);
                cvchat.put("fnum",fnum);
                cvchat.put("msgfrom","");
                db.insert("chat",null,cvchat);

            }
            catch(Exception ex)
            {

            }


        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sub_chat, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent it = new Intent(SubChat.this,Scheduler.class);
            it.putExtra("fnum",fnum);
            startActivity(it);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}


