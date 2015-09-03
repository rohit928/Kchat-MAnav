package in.thekites.project;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class StatusFragment extends ListFragment {

    EditText et;
    static final String[] STATUS = new String[]{"Available", "Busy", "At School",
            "At the movies", "At work", "can't talk ", "At the Gym", "Sleeping",
            "Urgent Calls only"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_status, container, false);

        setListAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,
                STATUS));

        et = (EditText) rootView.findViewById(R.id.status);

        return rootView;


    }

    public void onListItemClick(ListView parent, View v, int position,
                                long id) {
        et.setText(STATUS[position]);
    }
}