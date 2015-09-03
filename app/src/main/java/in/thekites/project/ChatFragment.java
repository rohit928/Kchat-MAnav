package in.thekites.project;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatFragment extends ListFragment {

    // Progress Dialog
    private ProgressDialog pDialog;
    List list;
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> productsList;

    // url to get all products list
    //private static String url_all_products = "http://10.0.2.2/kchat/get_chat_contacts.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "products";
    private static final String TAG_PID = "pid";
    private static final String TAG_NAME = "name";

    // products JSONArray
    JSONArray products = null;

    ListView lv;
    SQLiteDatabase dc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);
        // Hashmap for ListView
        productsList = new ArrayList<HashMap<String, String>>();
        list = new ArrayList();
        // Loading products in Background Thread
        // new LoadAllProducts().execute();

        try {
            dc = getActivity().openOrCreateDatabase("chats.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);

        } catch (Exception ex) {

        }

       // list.add("jmdvshanio");
        try {
            Cursor cr = dc.rawQuery("select DISTINCT fnum from chat", null);
            cr.moveToFirst();
            do {
                list.add(cr.getString(0)+"");
            } while (cr.moveToNext());
        } catch (Exception ex) {

        }


        // Get listview

        //setListAdapter(new IconicAdapter());
        //setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list));

        setListAdapter(new IconicAdapter());
        // on seleting single product
        // launching Edit Product Screen


        return rootView;
    }


    class IconicAdapter extends ArrayAdapter<String> {
        IconicAdapter() {
            super(getActivity(), R.layout.imagerow, R.id.row, list);
        }

        public View getView(int position, View convertView,
                            ViewGroup parent) {
                 View abc=super.getView(position, convertView, parent);
            //    ImageView icon=(ImageView)row.findViewById(R.id.icon);
               return(abc);
        }
    }


    public void onListItemClick(ListView parent, View view, int position, long id) {
        //Toast.makeText(getActivity(),"Item Clickd",Toast.LENGTH_SHORT).show();
        Intent it = new Intent(getActivity(), SubChat.class);
        it.putExtra("FrndNum",list.get(position).toString());
        startActivity(it);
    }

}