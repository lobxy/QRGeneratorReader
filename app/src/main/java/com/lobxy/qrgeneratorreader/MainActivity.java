package com.lobxy.qrgeneratorreader;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private ImageView mImageView;

    private int mWidth = 700;
    private int mHeight = 700;

    private IntentIntegrator qrScan;

    private TextView text_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = findViewById(R.id.main_imageView);
        text_value = findViewById(R.id.main_textValue);

        ConstraintLayout constraintLayout = findViewById(R.id.parentView);
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        Button btn_readCode = findViewById(R.id.main_readButton);

        btn_readCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readQrCode();
            }
        });


        final EditText edit_codeValue = findViewById(R.id.main_edit);


        Button btn_generateCode = findViewById(R.id.main_generateCode);

        btn_generateCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //INPUT DATA IN JSON FORMAT OR THINGS WON'T WORK AS EXPECTED.

                //String data = edit_codeValue.getText().toString().trim();

                //same data.Remove to get edit text's string.
                String data = "{name:All hail NPE}";

                if (!data.isEmpty()) {
                    createCode(data);
                } else {
                    Toast.makeText(MainActivity.this, "Field is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
                    JSONObject jsonObject = new JSONObject(result.getContents());
                    text_value.setText(jsonObject.getString("name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    //if json isn't created, show the contents in the toast.

                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();

                    Log.i("READER", "onActivityResult: " + result.getContents());
                }

//                String a = result.getContents() + "\n" + result.getFormatName();
//
//                text_value.setText(a);
//
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void readQrCode() {
        //read qr code
        qrScan.initiateScan();
    }

    private void createCode(String message) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

        try {

            BitMatrix bitMatrix = multiFormatWriter.encode(message, BarcodeFormat.QR_CODE, mWidth, mHeight);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            Log.d("QR Generator", "onCreate: Created");
            mImageView.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }


}
