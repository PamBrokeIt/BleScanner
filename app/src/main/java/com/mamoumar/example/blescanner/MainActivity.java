package com.mamoumar.example.blescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {


    //------------------------------------------------------------------------------------
    //Variables
    //------------------------------------------------------------------------------------
    BluetoothManager miBlueManager;
    BluetoothAdapter miBlueAdapter;
    BluetoothLeScanner miBlueScanner;

    private final static int REQUEST_ENABLE_BT = 1;

    private String TAG = "MainActivity";
    //------------------------------------------------------------------------------------
    //Variables
    //------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Creamos los objetos blManager, blAdapter y blScanner
        miBlueManager = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
        miBlueAdapter = miBlueManager.getAdapter();
        miBlueScanner = miBlueAdapter.getBluetoothLeScanner();

        //Nos aseguramos que el usuario active el BT
        if (miBlueAdapter != null && !miBlueAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent,REQUEST_ENABLE_BT);
        }

    }


    //------------------------------------------------------------------------------------
    //Empezar - Parar scan
    //------------------------------------------------------------------------------------
    private void empezarEscaneo(){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                miBlueScanner.startScan();
            }
        });
    }

    private void pararEscaneo(){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                miBlueScanner.stopScan();
            }
        });
    }
    //------------------------------------------------------------------------------------
    //Empezar - Parar scan
    //------------------------------------------------------------------------------------
}
