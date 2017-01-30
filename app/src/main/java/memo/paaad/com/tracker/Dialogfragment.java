package memo.paaad.com.tracker;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by User on 1/27/2017.
 */

interface OnNewObjectSelectedListener{
    public void newObjectSelected(ObjectData objectData);
}
public class Dialogfragment extends DialogFragment {

    OnNewObjectSelectedListener onNewObjectSelectedListener ;
    ListView listView ;
    MycustomAdapter mycustomAdapter ;
    ArrayList<ObjectData> objectDatas = new ArrayList<ObjectData>();

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            this.getDialog().setTitle("Select your object");
            objectDatas = getData();
            mycustomAdapter = new MycustomAdapter(inflater.getContext()  ,R.layout.listview_item_data,objectDatas );
            View v = inflater.inflate(R.layout.dialog_list, container, false);
            listView = (ListView) v.findViewById(R.id.dialog_id);
            listView.setAdapter(mycustomAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(onNewObjectSelectedListener==null){
                        onNewObjectSelectedListener = (OnNewObjectSelectedListener) getActivity();
                        onNewObjectSelectedListener.newObjectSelected(objectDatas.get(position));
                    }else{
                        onNewObjectSelectedListener.newObjectSelected(objectDatas.get(position));
                    }
                }
            });

            return v;
        }

    private ArrayList<ObjectData> getData() {
        ArrayList<ObjectData> objects = new ArrayList<ObjectData>();
        objects.add(0,new ObjectData(MainActivity.objectsNames[0],R.mipmap.laptop,true));
        objects.add(0,new ObjectData(MainActivity.objectsNames[1],R.mipmap.wallet,true));
        objects.add(0,new ObjectData(MainActivity.objectsNames[2],R.mipmap.bag,true));
        objects.add(0,new ObjectData(MainActivity.objectsNames[3],R.mipmap.mobile,true));
        objects.add(0,new ObjectData(MainActivity.objectsNames[4],R.mipmap.keys,true));
        objects.add(0,new ObjectData(MainActivity.objectsNames[5],R.mipmap.bicycle,true));
        return objects ;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onNewObjectSelectedListener = (OnNewObjectSelectedListener)context;
    }
}
