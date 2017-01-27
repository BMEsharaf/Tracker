package memo.paaad.com.tracker;

import android.app.FragmentTransaction;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    listObjects listobjects ;
    FragmentManager fragmentManager ;
    FragmentTransaction fragmentTransaction ;
    Button enterobject ;
    Dialogfragment dialogfragment ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}
