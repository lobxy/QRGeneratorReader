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
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class MainActivity extends AppCompatActivity {

    private ImageView mImageView;

    private int mWidth = 700;
    private int mHeight = 700;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = findViewById(R.id.main_image);

        //hide keyboard
        ConstraintLayout constraintLayout = findViewById(R.id.parentView);
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        final EditText edit_codeValue = findViewById(R.id.main_message);

        Button btn_generateCode = findViewById(R.id.main_generateCode);

        btn_generateCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //INPUT DATA IN JSON FORMAT OR THINGS WON'T WORK AS EXPECTED.

                String value = edit_codeValue.getText().toString().trim();

                String data = "{name:" + value + "}";

                if (!data.isEmpty()) {
                    createCode(data);
                } else {
                    Toast.makeText(MainActivity.this, "Field is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button readCode = findViewById(R.id.main_readCode);
        readCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ReadCodeActivity.class));
            }
        });

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
