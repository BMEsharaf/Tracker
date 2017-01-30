package memo.paaad.com.tracker;


import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements OnNewObjectSelectedListener ,onObjectSelected , OnEnterFragmentListener ,BluetoothSelectedListener{

    static String[] objectsNames;
    listObjects listobjects;
    ObjectData objectData ;
    EnterFragment enterFragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Dialogfragment dialogfragment;
    Bluetoothdialog bluetoothdialog ;
    Handler mhandler;
    BluetoothAdapter bluetoothAdapter;
    IntentFilter filter ;
    ArrayList<BluetoothDevice> bluetoothDevices ,secondBluetoothDevice;
    Set<BluetoothDevice> bondedDevices ;
    BluetoothSocket bluetoothSocket;
    ConnectedThread connectedThread ;
    final static int ENABLE_REQUEST = 1;
    final static int CALLING_REQUEST =2 ;;
    ProgressDialog pDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Define Name of object
        objectData = new ObjectData();
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
        secondBluetoothDevice  = new ArrayList<>();
        bondedDevices    = bluetoothAdapter.getBondedDevices();
        final MediaPlayer mp = MediaPlayer.create(this , R.raw.alarmtone);
        pDialog = new ProgressDialog(this);
        pDialog.setTitle("Finding object ..");
        pDialog.setCancelable(false);
        // Updating UI Thread
        mhandler = new Handler(Looper.getMainLooper()) {
            /*
                     * handleMessage() defines the operations to perform when
                     * the Handler receives a new Message to process.
                     */
            @Override
            public void handleMessage(Message inputMessage) {

                if(inputMessage.what==ConnectedThread.handlerState){
                    //Update UI
                    if(inputMessage.arg1==CALLING_REQUEST){ // Make android Calling
                        mp.start();
                    }
                }

            }

        };

    }

    @Override
    public void newObjectSelected(ObjectData objectData2) {
       this.objectData = objectData2 ;
        dialogfragment.dismiss();

        // Check if bluetooth is on or off
        if (!bluetoothAdapter.isEnabled()) {

            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE),ENABLE_REQUEST);

        } else if (bluetoothAdapter.isEnabled() && !bluetoothAdapter.isDiscovering()) {

            bluetoothAdapter.startDiscovery();


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
                bluetoothAdapter.startDiscovery();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    @Override
    public void selectedBluetoothDevice(BluetoothDevice bluetoothDevice) {
        listobjects.changeData(objectData);
        bluetoothdialog.dismiss();
        Log.i("Bluetooth" ,bluetoothDevice.getName() );
        if(bondedDevices.contains(bluetoothDevice)){
            Log.i("Bluetooth" , "You are connected");
            connectAsClient(bluetoothDevice);
            connectedThread = new ConnectedThread(bluetoothSocket,mhandler);
            connectedThread.start();
        }
    }
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action_name = intent.getAction();
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action_name)) {
                // Start Showing dialog to wait
                secondBluetoothDevice.clear();
                bluetoothDevices.clear();
                pDialog.show();
                Log.i("Bluetooth","Start");

          //      showDialog(progress_bar_type);
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action_name)) {
                // End the dialog
          //      pDialog.dismiss();
                Log.i("Bluetooth","finish");
                bluetoothDevices.addAll(secondBluetoothDevice);
                bluetoothdialog.setBluetoothdevices(bluetoothDevices);
                bluetoothdialog.show(getFragmentManager(), "Dialog");
                pDialog.dismiss();
            } else if (BluetoothDevice.ACTION_FOUND.equals(action_name)) {
                String remoteDeviceName =
                        intent.getStringExtra(BluetoothDevice.EXTRA_NAME);

                BluetoothDevice remoteDevice =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                secondBluetoothDevice.add(remoteDevice);
                Log.i("Bluetooth found",remoteDeviceName);
            }

        }

    };
    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    private void connectAsClient(BluetoothDevice objectDevice) {

        try {
            bluetoothSocket = objectDevice.createInsecureRfcommSocketToServiceRecord(objectDevice.getUuids()[0].getUuid());
        } catch (IOException ConnectionError) {
            Log.i("Bluetooth debug","Error in connection");
        }
        try {
            bluetoothSocket.connect();
        } catch (IOException e) {

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (filter == null) {
            filter = new IntentFilter();
            filter.addAction(BluetoothDevice.ACTION_FOUND);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            registerReceiver(mReceiver, filter);
        } else {
            registerReceiver(mReceiver, filter);
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mReceiver);
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        try {
            if((connectedThread!=null)&&(bluetoothSocket!=null))
            {connectedThread.stop();
            bluetoothSocket.close();}
        } catch (IOException e) {
            e.printStackTrace();
        }catch (UnsupportedOperationException e){
Log.i("Error","Error in stop thread");
        }
    }

}