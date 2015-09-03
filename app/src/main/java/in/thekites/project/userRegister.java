
package in.thekites.project;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class userRegister extends Activity implements AdapterView.OnItemSelectedListener {

    Spinner spinner1;
    String countries[];
    EditText et, et1;
    String number;
    String deviceid="";
    JSONParser jsonParser = new JSONParser();

    private static final String url_update_user = "http://jsr.esy.es/kchat/update_num.php";
    private static final String TAG_SUCCESS = "success";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        et = (EditText) findViewById(R.id.editText);
        et1 = (EditText) findViewById(R.id.num);
        spinner1 = (Spinner) findViewById(R.id.country);

        countries = getResources().getStringArray(R.array.Choose_Country);

        Bundle bundle=getIntent().getExtras();
        deviceid=bundle.getString("devid");

        ArrayAdapter<String> aa = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,countries);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(aa);
        spinner1.setOnItemSelectedListener(this);
    }

    public void next(View v) {

        number = et1.getText().toString();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("Number", number);
        editor.commit();
        if(number != null && !number.isEmpty()){
        try {
            new SaveNumber().execute();
        }
        catch (Exception ex)
        {
            // Do Check Internet
        }
        }
        else
        {
            showAlertDialog(userRegister.this, "No Number Entered",
                    "Please Enter your Number.", false);
        }

    }

    public void onItemSelected(AdapterView<?> parent,View view,int pos, long id){
        String item = parent.getItemAtPosition(pos).toString();
        switch (item){
            case "India":{
                et.setText("91"); break;
            }
            case "US":{
                et.setText("1"); break;
            }
            case "UK":{
                et.setText("44"); break;
            }
            case "Canada":{
                et.setText("1"); break;
            }
            case "Singapore":{
                et.setText("65"); break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    class SaveNumber extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }


        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user_num", number));
            params.add(new BasicNameValuePair("devid",deviceid));

            JSONObject json2 = jsonParser.makeHttpRequest(url_update_user,
                    "POST", params);


            try {
                int success = json2.getInt(TAG_SUCCESS);

                if (success == 1) {


                } else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }


        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product updated
            Intent i = new Intent(userRegister.this, setProfile.class);
            startActivity(i);
            finish();

        }
    }

    //-------------------Alert Dialoug Manager--------------------------------//

    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting alert dialog icon
        alertDialog.setIcon((status) ? R.drawable.applyd : R.drawable.failure);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    //-----------------------------------------------------------------------//

}
