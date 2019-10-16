package com.mamoumar.example.blescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    //------------------------------------------
    //Variables
    //------------------------------------------------------------------------------------
    BluetoothManager miBlueManager;
    BluetoothAdapter miBlueAdapter;
    BluetoothLeScanner miBlueScanner;

    private final static int REQUEST_ENABLE_BT = 1;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

    private String TAG = "MainActivity";

    Button botonEmpezarEscaneo;
    Button botonPararEscaneo;
    //------------------------------------------------------------------------------------
    //Variables
    //------------------------------------------

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

        botonEmpezarEscaneo = (Button) findViewById(R.id.botonEmpezar);
        botonEmpezarEscaneo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                empezarEscaneo();
            }
        });

        botonPararEscaneo = (Button) findViewById(R.id.botonParar);
        botonPararEscaneo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pararEscaneo();
            }
        });

        // Ya que coarse_location es un permiso "peligroso" debemos de pedir permiso para acceder a el
        if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Se necesita el acceso a la ubicación");
            builder.setMessage("Por favor acepte el acceso a la app para poder detectar dispositivos.");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                }
            });
            builder.show();
        }

    }


    // Escaner callback
    private ScanCallback escanerCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult resultado) {

            Log.i( TAG, "Dispositivo: " + resultado.getScanRecord().getManufacturerSpecificData(76));

        }
    };

    //------------------------------------------
    //Empezar - Parar escaner
    //------------------------------------------------------------------------------------
    private void empezarEscaneo(){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "Empezamos el scan");
                miBlueScanner.startScan(escanerCallback);

            }
        });
    }

    private void pararEscaneo(){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                miBlueScanner.stopScan(escanerCallback);
            }
        });
    }
    //------------------------------------------------------------------------------------
    //Empezar - Parar escaner
    //------------------------------------------
}
