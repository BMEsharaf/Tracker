package memo.paaad.com.tracker;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by User on 1/27/2017.
 */
interface  onObjectSelected {
    void IndexSelectedItem(int index);
}
public class listObjects extends ListFragment {
    ArrayList<ObjectData> MyObjects = new ArrayList<ObjectData>() ;
    MycustomAdapter mycustomAdapter;
    onObjectSelected onItemSelected ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mycustomAdapter = new MycustomAdapter(inflater.getContext(),android.R.layout.simple_list_item_1,MyObjects);
        setListAdapter(mycustomAdapter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onItemSelected = (onObjectSelected) context;
        }catch (ClassCastException e){
            Log.i("Error","Please Implement listener");
        }
    }
    public void changeData (ObjectData objectData){
        MyObjects.add(0,objectData);
        mycustomAdapter.notifyDataSetChanged();
    }
    @Override
    public void onListItemClick(ListView l, View view, int position, long id) {

        if(onItemSelected!=null)
        onItemSelected.IndexSelectedItem(position);
        else{
            onItemSelected =(onObjectSelected) getActivity();
            onItemSelected.IndexSelectedItem(position);
        }

    }
}
