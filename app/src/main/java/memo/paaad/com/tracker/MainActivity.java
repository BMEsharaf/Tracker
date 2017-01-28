package memo.paaad.com.tracker;


import android.app.FragmentTransaction;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements OnNewObjectSelectedListener ,onObjectSelected {

    static String[] objectsNames ;
    listObjects listobjects ;
    FragmentManager fragmentManager ;
    FragmentTransaction fragmentTransaction ;
    Button enterobject ;
    Dialogfragment dialogfragment ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Define Name of object
        objectsNames = getResources().getStringArray(R.array.Object_names);
// For Test purpose

        dialogfragment = new Dialogfragment() ;
        enterobject = (Button) findViewById(R.id.enter);
        enterobject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               dialogfragment.show(getFragmentManager(),"Dialog");
            }
        });
        listobjects = new listObjects() ;
        fragmentManager = getFragmentManager() ;
        fragmentTransaction = fragmentManager.beginTransaction() ;
        fragmentTransaction.add(R.id.container1,listobjects);
        fragmentTransaction.commit();
    }

    @Override
    public void newObjectSelected(ObjectData objectData) {
        listobjects.changeData(objectData);
    }

    @Override
    public void IndexSelectedItem(int index) {
        Log.i("Debugging" , index+"");
    }
}
