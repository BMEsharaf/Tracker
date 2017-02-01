package memo.paaad.com.tracker;



import android.app.FragmentTransaction;
import android.app.FragmentManager;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static android.Manifest.permission.BLUETOOTH;

public class MainActivity extends AppCompatActivity implements OnNewObjectSelectedListener ,onObjectSelected , OnEnterFragmentListener ,BluetoothSelectedListener{

    static String[] objectsNames;
    listObjects listobjects;
    List<ObjectData> objectData = new ArrayList<>() ;
    EnterFragment enterFragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Dialogfragment dialogfragment;
    Bluetoothdialog bluetoothdialog ;
    Handler mhandler;
    BluetoothAdapter bluetoothAdapter;
    IntentFilter filter ;
    ArrayList<BluetoothDevice> bluetoothDevices ,secondBluetoothDevice ;
    Set<BluetoothDevice> bondedDevices ;
    List<BluetoothSocket> bluetoothSocket = new ArrayList<>();
    List<ConnectedThread> connectedThread = new ArrayList<ConnectedThread>() ;
    final static int ENABLE_REQUEST = 1;
    final static char CALLING_REQUEST ='2' ;
    final static char [] SENDING_REQUEST = {'2'};
    ProgressDialog pDialog ;
    int count  =  0 ;

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
        secondBluetoothDevice  = new ArrayList<>();
        bondedDevices    = bluetoothAdapter.getBondedDevices();
        final MediaPlayer mp = MediaPlayer.create(this , R.raw.adawea);
        pDialog = new ProgressDialog(this);
        pDialog.setTitle("Finding object ..");
        pDialog.setCancelable(false);
        // Updating UI Thread
        mhandler = new Handler(Looper.getMainLooper()) {

            @Override
            public void handleMessage(Message inputMessage) {

                if(inputMessage.what==ConnectedThread.handlerState){
                    Log.i("incoming Data",inputMessage.arg1+"");
                    if(inputMessage.arg1==CALLING_REQUEST){ // Make android Calling
                        mp.start();
                    }
                }

            }

        };

    }

    @Override
    public void newObjectSelected(ObjectData objectData2) {
       objectData.add(objectData2) ;
        dialogfragment.dismiss();

        // Check if bluetooth is on or off
        if (!bluetoothAdapter.isEnabled()) {

            startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE),ENABLE_REQUEST);

        } else if (bluetoothAdapter.isEnabled() && !bluetoothAdapter.isDiscovering()) {

            bluetoothAdapter.startDiscovery();

        }
    }


    @Override
    public void IndexSelectedItem(int index) {
        Log.i("Debugging", index + "");

        if(connectedThread.get(index)!=null){
            connectedThread.get(index).write(SENDING_REQUEST);
        }
    }

    @Override
    public void onenterFragment() {
        dialogfragment.show(getFragmentManager(), "Dialog");
    }

    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {
        if (requestCode == ENABLE_REQUEST) {
            if (resultCode == RESULT_OK) {
                bluetoothAdapter.startDiscovery();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    @Override
    public void selectedBluetoothDevice(BluetoothDevice bluetoothDevice) {

        if(bondedDevices.contains(bluetoothDevice)){
        Log.i("Bluetooth" ,bluetoothDevice.getName() );
            listobjects.addNewItem(objectData.get(count));
            Log.i("Bluetooth" , "You are connected");
            connectAsClient(bluetoothDevice , count );
            connectedThread.add(new ConnectedThread(bluetoothSocket.get(count),mhandler));
            connectedThread.get(connectedThread.size()-1).start();
            count++;
        }
        bluetoothdialog.dismiss();
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action_name = intent.getAction();
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action_name)) {

                secondBluetoothDevice.clear();
                bluetoothDevices.clear();
                pDialog.show();
                Log.i("Bluetooth","Start");

            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action_name)) {

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
            }else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action_name)||(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action_name))){
                BluetoothDevice remoteDevice =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.i("Name is" ,remoteDevice.getName() );
                for (int i = 0 ; i<count ; i++){
                    try{
                        bluetoothSocket.get(i).connect() ;
                    }catch (IOException e){
                        Log.i("Disconnection" , "This Device is disconnected "+ listobjects.MyObjects.get(i).getName());
                        if(connectedThread!=null)
                        connectedThread.get(i).interrupt();
                        objectData.get(i).setStatus(false);
                        listobjects.changePreviousItem(objectData.get(i),i);
                        makeNotification(objectData.get(i) , i);
                    }
                }
            }

        }

    };
    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    private void connectAsClient(BluetoothDevice objectDevice , int index) {

        try {
            bluetoothSocket.add(objectDevice.createInsecureRfcommSocketToServiceRecord(objectDevice.getUuids()[0].getUuid()));
        } catch (IOException ConnectionError) {
            Log.i("Bluetooth debug","Error in connection");
        }
        try {
            bluetoothSocket.get(index).connect();

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
            filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
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
            {
                for(int i = 0 ; i<connectedThread.size() ; i++) {
                    connectedThread.get(i).interrupt();
                    bluetoothSocket.get(i).close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }catch (UnsupportedOperationException e){
Log.i("Error","Error in stop thread");
        }
    }

    private  void makeNotification (ObjectData objectData , int id){
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(objectData.getImage())
                .setAutoCancel(true)
                .setContentTitle("Missing : "+objectData.getName())
                .setSound(soundUri);
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,mBuilder.build());
    }

}
