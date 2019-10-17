package com.mamoumar.example.blescanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    //------------------------------------------
    //Variables
    //------------------------------------------------------------------------------------
    BluetoothManager miBlueManager;
    BluetoothAdapter miBlueAdapter;
    BluetoothLeScanner miBlueScanner;

    private final static int REQUEST_ENABLE_BT = 1;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 2;

    private String TAG = "MainActivity";

    Button botonEmpezarEscaneo;
    Button botonPararEscaneo;

    private FusedLocationProviderClient client;
    Location miUbicacion = new Location("");
    //------------------------------------------------------------------------------------
    //Variables
    //------------------------------------------


    //------------------------------------------
    //OnCreate
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

        // Ya que coarse_location es un permiso "peligroso" debemos de pedir permiso para acceder a él
        if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Se necesita el acceso a la ubicación");
            builder.setMessage("Por favor acepte el acceso a la app para poder detectar los dispositivos y su ubicación.");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                }
            });
            builder.show();
        }


        client = LocationServices.getFusedLocationProviderClient(this);
    }
    //------------------------------------------------------------------------------------
    //OnCreate
    //------------------------------------------



    //------------------------------------------
    // Escaner empezar - Parar - callback
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

    // Escaner callback
    private ScanCallback escanerCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult resultado) {

            dondeEstoy();

            Log.i(TAG, "Dipositivo: " + resultado.getScanRecord());
            /*
            Log.i( TAG, "Manufacterer: " + resultado.getScanRecord().getManufacturerSpecificData(76));

            TramaIBeacon tib = new TramaIBeacon( bytes ); //decomponemos la trama para poder llamar a metodos que nos den la info que queremos
            Log.i(TAG, "UUID: " + tib.getUUID());
            Log.i(TAG, "Compañia: " + tib.getCompanyID());
            */

            byte[] bytes = null;
            bytes = resultado.getScanRecord().getBytes();

            TramaIBeacon tib = new TramaIBeacon(bytes);
            Utilidades util = new Utilidades();


            Log.i("Debug", "UUID: " +  util.bytesToHexString(tib.getUUID()));
            Log.i("Debug", "Major: " +  util.bytesToIntOK(tib.getMajor()));



        }
    };
    //------------------------------------------------------------------------------------
    //Escaner empezar - Parar - callback
    //------------------------------------------


    //------------------------------------------
    //Localizacion
    //------------------------------------------------------------------------------------
    private void dondeEstoy(){
        client.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location localizacion) {
                if(localizacion != null){
                    Log.d("UBICACION: ", localizacion.toString());
                    miUbicacion = localizacion;
                }
            }
        });
    }
    //------------------------------------------------------------------------------------
    //Localizacion
    //------------------------------------------
}
