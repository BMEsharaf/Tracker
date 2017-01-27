package memo.paaad.com.tracker;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by User on 1/27/2017.
 */
interface  onItemSelected {
    void IndexSelectedItem(int index);
}
public class listObjects extends ListFragment {
    ObjectData objectData ;
    ArrayList<ObjectData> MyObjects = new ArrayList<ObjectData>() ;
    onItemSelected onItemSelected ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        objectData = new ObjectData();
        objectData.setImage(R.mipmap.laptop);
        objectData.setStatus(true);
        MyObjects.add(objectData);
        setListAdapter(new MycustomAdapter(inflater.getContext(),R.layout.listview,MyObjects));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onItemSelected = (onItemSelected) context ;
    }
}
