package memo.paaad.com.tracker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 1/27/2017.
 */


public class MycustomAdapter extends ArrayAdapter<ObjectData> {

    static class ViewHolder {
        TextView textView ;
        ImageView imageView ;
        RadioButton radioButton ;
    }
    ArrayList<ObjectData> objects ;
    ViewHolder holder ;
    public MycustomAdapter(Context context, int resource, ArrayList<ObjectData> objects) {
        super(context, resource, objects);
        this.objects = objects ;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView ==null){
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item_data,null);
            holder = new ViewHolder();
            holder.textView = (TextView)convertView.findViewById(R.id.object_name);
            holder.imageView = (ImageView)convertView.findViewById(R.id.object_image);
            holder.radioButton = (RadioButton)convertView.findViewById(R.id.object_status);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
            holder.textView.setText(objects.get(position).getName());
            holder.imageView.setImageResource(objects.get(position).getImage());
            holder.radioButton.setChecked(objects.get(position).isStatus());
        }
        return convertView ;
    }
    void addObject (ObjectData objectData){
        objects.add(0,objectData);
        this.notifyDataSetChanged();
    }
}
