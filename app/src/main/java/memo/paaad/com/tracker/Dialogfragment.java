package memo.paaad.com.tracker;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by User on 1/27/2017.
 */

public class Dialogfragment extends DialogFragment {

    ListView listView ;
    MycustomAdapter mycustomAdapter ;
    ArrayList<ObjectData> objectDatas = new ArrayList<ObjectData>();

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            objectDatas = getData();
            mycustomAdapter = new MycustomAdapter(inflater.getContext()  ,R.layout.listview_item_data,objectDatas );
            View v = inflater.inflate(R.layout.dialog_list, container, false);
            listView = (ListView) v.findViewById(R.id.dialog_id);
            return v;
        }

    private ArrayList<ObjectData> getData() {
        ArrayList<ObjectData> objects = new ArrayList<ObjectData>();
        objects.add(0,new ObjectData("Laptop",R.mipmap.laptop,true));
        objects.add(0,new ObjectData("Laptop",R.mipmap.laptop,true));
        objects.add(0,new ObjectData("Laptop",R.mipmap.laptop,true));
        objects.add(0,new ObjectData("Laptop",R.mipmap.laptop,true));
        return objects ;
    }

}
