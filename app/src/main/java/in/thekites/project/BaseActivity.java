package in.thekites.project;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;


public class BaseActivity extends Activity {

    // flag for Internet connection status
    Boolean isInternetPresent = false;

    // Connection detector class
    ConnectionDetector cdr;
    //-----------------------Alert Dialoug----------------------//
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
                recreate();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
    //---------------------------------------------------------//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        //----------------Getting Internet Status-------------//

        cdr = new ConnectionDetector(getApplicationContext());

        // get Internet status
        isInternetPresent = cdr.isConnectingToInternet();

        // check for Internet status
        // Ask user to connect to Internet
        if (!isInternetPresent) {
            // Internet Connection is not Present
            showAlertDialog(BaseActivity.this, "No Internet Connection",
                    "You don't have internet connection.", false);

        }

        else {

   //         Toast.makeText(this,"else block",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        //-------------- Internet Close ----------------------//

    }

}
