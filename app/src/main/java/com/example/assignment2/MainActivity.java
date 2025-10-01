package com.example.assignment2;

import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private MyDrawingArea mda;

    private ImageView sketch1IV = null;
    private ImageView sketch2IV = null;
    private ImageView sketch3IV = null;

    private Bitmap BM1 = null;
    private Bitmap BM2 = null;
    private Bitmap BM3 = null;

    private SensorManager sm;
    private Sensor s;
    private float accelCurrent;
    private float accelLast;
    private float shakeThreshold = 12f;


    private int numDrawn = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        mda = findViewById(R.id.drawing);
        sketch1IV = findViewById(R.id.drawing1);
        sketch2IV = findViewById(R.id.drawing2);
        sketch3IV = findViewById(R.id.drawing3);
        updateViews();

        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        s = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (s == null) {
            Log.e("sensor", "accelerometer sensor not available");
        } else {
            sm.registerListener(this, s, 1000000);
        }
        accelCurrent = SensorManager.GRAVITY_EARTH;
        accelLast = SensorManager.GRAVITY_EARTH;

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // saves bitmap
    public void onClick(View view) {
        Bitmap bMap = mda.getBitmap();
        try {
            File sketchFile = createFile();
            FileOutputStream fos = new FileOutputStream(sketchFile);
            bMap.compress(Bitmap.CompressFormat.PNG,100,fos);
            Toast.makeText(this, "Saved at: " + sketchFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
            numDrawn ++;
            switchBitmaps(bMap);
            updateViews();
            fos.flush();
            fos.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        eraseDrawing(mda);
    }

    // function to create file to store bitmap
    private File createFile() throws IOException {
        String sketchNumber = String.valueOf(numDrawn);
        String sketchFileName = "/mysketch" + sketchNumber + ".png";
        File sketchFIle= new File(getFilesDir().getAbsolutePath() + sketchFileName);
        return sketchFIle;
    }

    // switch bitmaps so they are in order of most recent
    private void switchBitmaps(Bitmap newBM) {
        BM3 = BM2;
        BM2 = BM1;
        BM1 = newBM;
    }

    // update ImageViews
    private void updateViews() {
        sketch1IV.setImageBitmap(BM1);
        sketch2IV.setImageBitmap(BM2);
        sketch3IV.setImageBitmap(BM3);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            accelLast = accelCurrent;
            // acceleration magnitude
            accelCurrent = (float) Math.sqrt((x * x + y * y + z * z));
            // change in acceleration
            float delta = accelCurrent - accelLast;

            if (delta > shakeThreshold) {
                mda.putBallsOnPath();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        sm.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sm.registerListener(this,
                sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                1000000);
    }

    public void eraseDrawing(View view) {
        mda.clearDrawing();
    }
}