package in.thekites.project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TopRatedFragment extends Fragment {

    SQLiteDatabase dbse;
    List contacts;
    ListView lv;
    String Num;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_top_rated, container, false);

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        Num = pref.getString("Number",null);
       // Toast.makeText(getActivity(),Num,Toast.LENGTH_SHORT).show();

        contacts=new ArrayList();

        lv =(ListView) rootView.findViewById(R.id.listview);

    try
    {
        dbse=getActivity().openOrCreateDatabase("contacts.db",SQLiteDatabase.CREATE_IF_NECESSARY,null);
        Cursor cr= dbse.rawQuery("select DISTINCT * from contacts",null);
        cr.moveToFirst();

        do
        {
        contacts.add(cr.getString(0));
        }

        while(cr.moveToNext());
    }

    catch(Exception ex)
    {
    Toast.makeText(getActivity(),ex.toString(),Toast.LENGTH_LONG).show();
    }

        lv.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,contacts));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getActivity(),contacts.get(position)+"",Toast.LENGTH_SHORT).show();
        Intent it = new Intent(getActivity(),SubChat.class);
        it.putExtra("FrndNum",contacts.get(position).toString());
        startActivity(it);

    }
});
        return rootView;
    }

}