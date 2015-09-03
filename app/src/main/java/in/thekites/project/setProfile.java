package in.thekites.project;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class setProfile extends Activity {

    private final int SELECT_PHOTO = 1;
    ImageView iview;
    RoundImage roundImage;
    EditText et;

    String Num;

    JSONParser jsonParser = new JSONParser();

    JSONObject status;

    private static final String url_update_name = "http://jsr.esy.es/kchat/update_name.php";
    private static final String url_set_pic_path = "http://jsr.esy.es/kchat/set_pic_path.php";
    private static final String TAG_SUCCESS = "success";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_profile);
        iview = (ImageView) findViewById(R.id.imageView3);
        et= (EditText) findViewById(R.id.Name);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        Num = pref.getString("Number",null);
        Toast.makeText(this,Num,Toast.LENGTH_SHORT).show();
    }

    public void onClick(View arg0) {

        Intent intent = new Intent();

        // call android default gallery

        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        // ******** code for crop image

        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 0);
        intent.putExtra("aspectY", 0);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);

        try {

            intent.putExtra("return-data", false);
            startActivityForResult(Intent.createChooser(intent,
                    "Complete action using"), SELECT_PHOTO);

        }
        catch (ActivityNotFoundException e) {
        // Do nothing for now
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data != null) {
            if (requestCode == SELECT_PHOTO) {
                Bundle extras2 = data.getExtras();
                if (extras2 != null) {
                    Bitmap photo = extras2.getParcelable("data");
                    roundImage = new RoundImage(photo);
                    //iview.setImageBitmap(photo);
                    iview.setImageDrawable(roundImage);
                }

            }
        }
    }

    public void mainScreen(View v){

        try {
            new SaveNameDetails().execute();
        }
        catch (Exception ex)
        {
            // Do check Internet
        }
    }

    class SaveNameDetails extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        protected String doInBackground(String... args) {


            String name = et.getText().toString();


            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user_num", Num));
            params.add(new BasicNameValuePair("user_name", name));


            JSONObject json122 = jsonParser.makeHttpRequest(url_update_name,
                    "POST", params);


            try {
                int success = json122.getInt(TAG_SUCCESS);

                if (success == 1) {

                } else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }


        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product uupdated
            //       pDialog.dismiss();
            Intent i = new Intent(setProfile.this, Contacts.class);
            startActivity(i);
            finish();
        }
    }
}
