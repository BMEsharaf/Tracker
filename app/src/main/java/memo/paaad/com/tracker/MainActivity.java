package memo.paaad.com.tracker;


import android.app.FragmentTransaction;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements OnNewObjectSelectedListener ,onObjectSelected , OnEnterFragmentListener {

    static String[] objectsNames ;
    listObjects listobjects ;
    EnterFragment enterFragment ;
    FragmentManager fragmentManager ;
    FragmentTransaction fragmentTransaction ;
    Dialogfragment dialogfragment ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Define Name of object
        objectsNames = getResources().getStringArray(R.array.Object_names);
        enterFragment = new EnterFragment();
        dialogfragment = new Dialogfragment() ;
        listobjects = new listObjects() ;
        fragmentManager = getFragmentManager() ;
        fragmentTransaction = fragmentManager.beginTransaction() ;
        fragmentTransaction.add(R.id.container2,enterFragment);
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

    @Override
    public void onenterFragment() {
        dialogfragment.show(getFragmentManager(),"Dialog");
    }
}
