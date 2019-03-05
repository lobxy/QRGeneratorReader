package com.lobxy.qrgeneratorreader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class ReadCodeActivity extends AppCompatActivity {

    private TextView text_value;

    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_code);

        Button readCode = findViewById(R.id.readCode_read);
        text_value = findViewById(R.id.readCode_value);

        //intializing scan object
        qrScan = new IntentIntegrator(this);

    }

    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data

                try {
                    //set text_value to textView
                    JSONObject jsonObject = new JSONObject(result.getContents());
                    text_value.setText(jsonObject.getString("name"));

                } catch (JSONException e) {
                    e.printStackTrace();

                    //if json isn't created, show the contents in a toast.
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();

                    Log.i("READER", "onActivityResult: " + result.getContents());
                }

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void readQrCode(View view) {
        //read qr code
        qrScan.initiateScan();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
