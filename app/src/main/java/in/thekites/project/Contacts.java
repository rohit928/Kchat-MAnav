package in.thekites.project;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Contacts extends ListActivity {

    List q;
    ArrayList<HashMap<String, String>> productsList;
    JSONParser jsonParser = new JSONParser();
    String contatcs="";
    SQLiteDatabase db;
    String phoneNumber;
    JSONObject json;
    JSONArray jarray;
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    private static String url_create_cont = "http://jsr.esy.es/kchat/chat_contatct.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);


            productsList = new ArrayList<HashMap<String, String>>();
            q=new ArrayList();

            try {
                new CreateContacts().execute();
                }

            catch(Exception ex)
            {
            }
        }

    //---------Getting Contacts And send to server-----------//

    class CreateContacts extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        /**
         * Creating Contacts
         * */
        protected String doInBackground(String... args) {

            Cursor phones = getApplicationContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            while (phones.moveToNext())
            {
             //   String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                String ar[]=phoneNumber.split(" ");
                phoneNumber="";
                for(String a2:ar)
                {
                    phoneNumber+=a2;
                }
                System.out.println(".................." + phoneNumber);

                //aa.add(phoneNumber);
                contatcs+=phoneNumber+",";
            }
            phones.close();// close cursor


            params.add(new BasicNameValuePair("contacts", contatcs));


            // getting JSON Object
            // Note that create product url accepts POST method
            json = jsonParser.makeHttpRequest(url_create_cont,
                    "POST", params);

            try {

               jarray=json.getJSONArray("contact");
                Log.d("response",json.toString()+jarray);
                for (int i = 0; i < jarray.length(); i++) {
                    JSONObject c = jarray.getJSONObject(i);

                    // Storing each json item in variable
                    String contact = c.getString("contact");
                    Log.d("response", c + contact);
                                       // creating new HashMap
                   q.add(contact);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {

            try {
               db = openOrCreateDatabase("contacts.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);

                // updating listview
                setListAdapter(new ArrayAdapter<String>(Contacts.this, android.R.layout.simple_list_item_1, q));

            }

            catch(Exception ex) {
            }

            try {

               db.execSQL("create table contacts (number text)");
                // updating listview

            }
            catch (Exception ex)
            {

            }
            try {

 //               db.execSQL("delete from contacts");
                // updating listview
                for(int i=0;i<jarray.length();i++)
                {
                    ContentValues cv=new ContentValues();
                    cv.put("number",q.get(i)+"");
                    db.insert("contacts", null, cv);
                    Log.d("qindex",q.get(i)+"");
                }
            }

            catch(Exception ex) {
            }

            try{

                Intent it = new Intent(Contacts.this,MainScreen.class);
                startActivity(it);
                finish();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

}
