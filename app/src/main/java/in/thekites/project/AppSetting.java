package in.thekites.project;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class AppSetting extends ListActivity {

    static final String[] SETTINGS = new String[] { "Help", "Profile", "Account",
            "Chat Settings" };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_setting);

        setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                SETTINGS));
    }

    public void onListItemClick(ListView parent, View v, int position,long id) {

if(position==0)
{

}

    else if(position==1) {
    AlertDialog.Builder ab = new AlertDialog.Builder(this);
    ab.setTitle("About App");
    ab.setIcon(R.drawable.ic_launcher);
    ab.setMessage("Start Chat with KChat");
    AlertDialog alertDialog1 = ab.create();
 //   alertDialog1.setIcon(R.drawable.ic_launcher);
    alertDialog1.show();


}

        if(position==2)
        {
            Toast.makeText(getApplicationContext(),"Account is Activated",Toast.LENGTH_LONG).show();
        }
else if(position==3)
        {
            Intent intent=new Intent(AppSetting.this,SettingsActivity.class);
            startActivity(intent);

        }

    }

}
