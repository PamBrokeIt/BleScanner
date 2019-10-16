package com.mamoumar.example.blescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.Context;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {


    //------------------------------------------
    //Variables
    //------------------------------------------
    BluetoothManager miBlueManager;
    BluetoothAdapter miBlueAdapter;
    BluetoothLeScanner miBlueScanner;
    //------------------------------------------
    //Variables
    //------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Creamos los objetos blManager blAdapter y blScanner
        miBlueManager = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
        miBlueAdapter = miBlueManager.getAdapter();
        miBlueScanner = miBlueAdapter.getBluetoothLeScanner();

    }
}
