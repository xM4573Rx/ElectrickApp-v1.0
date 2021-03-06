package com.example.jorge.controlbt;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserInterfaz extends AppCompatActivity {

    //1)
    public static final String DATO_KEY = "DATO";
    public static final String DATO_KEY1 = "DATO1";
    public static final String DATO_KEY2 = "DATO2";
    public String Dato;
    public String WattHora;
    public String ActualWatt;
    public String Costtext;
    public String Watts;

    public double cost;
    public double wattactual;
    public double costwatt;

    public int Hello;
    public String Prueba;

    TabLayout MyTabs;
    ViewPager MyPage;

    //-------------------------------------------
    Handler bluetoothIn;
    final int handlerState = 0;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder DataStringIN = new StringBuilder();
    private ConnectedThread MyConexionBT;
    // Identificador unico de servicio - SPP UUID
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    // String para la direccion MAC
    private static String address = null;
    //-------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_interfaz);
        //2)
        //Enlaza los controles con sus respectivas vistas

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        MyTabs = (TabLayout)findViewById(R.id.MyTabs);
        MyPage = (ViewPager)findViewById(R.id.MyPage);

        btAdapter = BluetoothAdapter.getDefaultAdapter(); // get Bluetooth adapter
        VerificarEstadoBT();

        setSupportActionBar(toolbar);
        SetUpViewPager(MyPage);
        MyTabs.setupWithViewPager(MyPage);

        getSupportActionBar().setTitle("ElectrickApp");

        MyTabs.getTabAt(0).setIcon(R.drawable.selector_one);
        MyTabs.getTabAt(1).setIcon(R.drawable.selector_two);
        MyTabs.getTabAt(2).setIcon(R.drawable.selector_three);
    }

    public class MyViewPageAdapter extends FragmentPagerAdapter {
        private List<Fragment> MyFragment = new ArrayList<>();
        public MyViewPageAdapter(FragmentManager manager){
            super(manager);
        }

        public void AddFragmentPage(Fragment Frag){
            MyFragment.add(Frag);
        }

        @Override
        public Fragment getItem(int position) {
            return MyFragment.get(position);
        }

        @Override
        public int getCount() {
            return MyFragment.size();
        }
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException
    {
        //crea un conexion de salida segura para el dispositivo
        //usando el servicio UUID
        return device.createRfcommSocketToServiceRecord(BTMODULEUUID);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        //Consigue la direccion MAC desde DeviceListActivity via intent
        Intent intent = getIntent();
        //Consigue la direccion MAC desde DeviceListActivity via EXTRA
        address = intent.getStringExtra(DispositivosBT.EXTRA_DEVICE_ADDRESS);//<-<- PARTE A MODIFICAR >->->
        //Setea la direccion MAC
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        try
        {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "La creación del Socket fallo", Toast.LENGTH_LONG).show();
        }
        // Establece la conexión con el socket Bluetooth.
        try
        {
            btSocket.connect();
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {}
        }
        MyConexionBT = new ConnectedThread(btSocket);
        MyConexionBT.start();

        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {

                    //String Dato;
                    //String WattHora;
                    String readMessage = (String) msg.obj;
                    DataStringIN.append(readMessage);

                    int StartOfLineIndex = DataStringIN.indexOf("[");

                    if ((StartOfLineIndex > -1) ) {

                        int EndOfLineIndex=DataStringIN.indexOf("]",StartOfLineIndex);
                        if((EndOfLineIndex > -1)) {
                            Dato=DataStringIN.substring(StartOfLineIndex+1, EndOfLineIndex);//<-<- PARTE A MODIFICAR >->->
                            if(!(Dato.length()<=9)) {

                                Watts = Dato.substring(0, Dato.indexOf("#"));
                                WattHora = Dato.substring(Dato.indexOf("#") + 1, Dato.indexOf("*"));
                                wattactual = Float.valueOf(WattHora)* 0.001;
                                costwatt = 466.14;
                                cost = wattactual * costwatt;
                                Costtext = String.valueOf(new DecimalFormat("##.##").format(cost));
                                ActualWatt = String.valueOf(new DecimalFormat("##.###").format(wattactual));
                                /*watts.setText(Dato.substring(0, Dato.indexOf("#")));*/

                            }else{
                                Toast.makeText(UserInterfaz.this,"MODULO SIN ALIMENTACION", Toast.LENGTH_SHORT).show();
                            }

                            StartOfLineIndex = -1;
                            EndOfLineIndex = -1;
                            DataStringIN.delete(0, DataStringIN.length());
                        }
                    }
                }
                SharedPreferences preferencias = getSharedPreferences("dato", Context.MODE_PRIVATE);
                SharedPreferences.Editor Obj_editor = preferencias.edit();
                Obj_editor.putString("DATO", Watts);
                Obj_editor.commit();

                SharedPreferences preferencias1 = getSharedPreferences("dato1", Context.MODE_PRIVATE);
                SharedPreferences.Editor Obj_editor1 = preferencias1.edit();
                Obj_editor1.putString("DATO1", ActualWatt);
                Obj_editor1.commit();

                SharedPreferences preferencias2 = getSharedPreferences("dato2", Context.MODE_PRIVATE);
                SharedPreferences.Editor Obj_editor2 = preferencias2.edit();
                Obj_editor2.putString("PRUEB", Costtext);
                Obj_editor2.commit();
            }
        };
    }

    private OneFragment newInstance2(String transfer, String transfer1, String transfer2) {
        Bundle bundle = new Bundle();
        bundle.putString(DATO_KEY, transfer);
        bundle.putString(DATO_KEY1, transfer1);
        bundle.putString(DATO_KEY2, transfer2);
        OneFragment fragment = new OneFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    public void SetUpViewPager (ViewPager viewpage){
        MyViewPageAdapter Adapter = new MyViewPageAdapter(getSupportFragmentManager());

        SharedPreferences preferences = getSharedPreferences("dato", Context.MODE_PRIVATE);
        String watts = preferences.getString("DATO", "0");

        SharedPreferences preferences1 = getSharedPreferences("dato1", Context.MODE_PRIVATE);
        String actualwatt = preferences1.getString("DATO1", "0");

        SharedPreferences preferences2 = getSharedPreferences("dato2", Context.MODE_PRIVATE);
        String costtext = preferences2.getString("PRUEB", "0");

        Adapter.AddFragmentPage(newInstance2(watts, costtext, actualwatt));
        Adapter.AddFragmentPage(new TwoFragment());
        Adapter.AddFragmentPage(new ThreeFragment());
        //We Need Fragment class now
        viewpage.setAdapter(Adapter);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        try
        { // Cuando se sale de la aplicación esta parte permite
            // que no se deje abierto el socket
            btSocket.close();
        } catch (IOException e2) {}
    }

    //Comprueba que el dispositivo Bluetooth Bluetooth está disponible y solicita que se active si está desactivado
    private void VerificarEstadoBT() {

        if(btAdapter==null) {
            Toast.makeText(getBaseContext(), "El dispositivo no soporta bluetooth", Toast.LENGTH_LONG).show();
        } else {
            if (btAdapter.isEnabled()) {
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    //Crea la clase que permite crear el evento de conexion
    private class ConnectedThread extends Thread
    {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket)
        {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try
            {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run()
        {
            byte[] buffer = new byte[16];
            int bytes;

            // Se mantiene en modo escucha para determinar el ingreso de datos
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);
                    String readMessage = new String(buffer, 0, bytes);
                    // Envia los datos obtenidos hacia el evento via handler
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }
        //Envio de trama
        public void write(String input)
        {
            try {
                mmOutStream.write(input.getBytes());
            }
            catch (IOException e)
            {
                //si no es posible enviar datos se cierra la conexión
                Toast.makeText(getBaseContext(), "La Conexión fallo", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    public void Precio() {

    }
}