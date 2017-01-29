package memo.paaad.com.tracker;


import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.FragmentManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements OnNewObjectSelectedListener ,onObjectSelected , OnEnterFragmentListener ,BluetoothSelectedListener{

    static String[] objectsNames;
    listObjects listobjects;
    EnterFragment enterFragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Dialogfragment dialogfragment;
    Bluetoothdialog bluetoothdialog ;
    Handler mhandler;
    BluetoothAdapter bluetoothAdapter;
    ArrayList<BluetoothDevice> bluetoothDevices ;
    final static int ENABLE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Define Name of object
        objectsNames = getResources().getStringArray(R.array.Object_names);
        enterFragment = new EnterFragment();
        dialogfragment = new Dialogfragment();
        bluetoothdialog = new Bluetoothdialog();
        listobjects = new listObjects();
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container2, enterFragment);
        fragmentTransaction.add(R.id.container1, listobjects);
        fragmentTransaction.commit();
        //Set Bluetooth settings
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothDevices = new ArrayList<>(bluetoothAdapter.getBondedDevices());
        // Updating UI Thread
        mhandler = new Handler(Looper.getMainLooper()) {
            /*
                     * handleMessage() defines the operations to perform when
                     * the Handler receives a new Message to process.
                     */
            @Override
            public void handleMessage(Message inputMessage) {
                // Gets the image task from the incoming Message object.

            }

        };

    }

    @Override
    public void newObjectSelected(ObjectData objectData) {
        listobjects.changeData(objectData);
        dialogfragment.dismiss();
        // Check if bluetooth is on or off
        if (!bluetoothAdapter.isEnabled()) {

            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), ENABLE_REQUEST);

        } else {

            bluetoothdialog.setBluetoothdevices(bluetoothDevices);
            bluetoothdialog.show(getFragmentManager(), "Dialog");

        }

    }


    @Override
    public void IndexSelectedItem(int index) {
        Log.i("Debugging", index + "");
    }

    @Override
    public void onenterFragment() {
        dialogfragment.show(getFragmentManager(), "Dialog");
    }

    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {
        if (requestCode == ENABLE_REQUEST) {
            if (resultCode == RESULT_OK) {
// Bluetooth has been enabled, initialize the UI.
                bluetoothdialog.setBluetoothdevices(bluetoothDevices);
                bluetoothdialog.show(getFragmentManager(), "Dialog");
                bluetoothDevices.clear();
                bluetoothDevices.addAll(bluetoothAdapter.getBondedDevices());
            }
        }
    }

    @Override
    public void selectedBluetoothDevice(BluetoothDevice bluetoothDevice) {

        bluetoothDevices.clear();
        bluetoothDevices.addAll(bluetoothAdapter.getBondedDevices());
    }
}