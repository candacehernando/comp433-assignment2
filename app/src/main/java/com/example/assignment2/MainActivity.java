package com.example.assignment2;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
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

public class MainActivity extends AppCompatActivity {

    private MyDrawingArea mda;

    private ImageView sketch1IV = null;
    private ImageView sketch2IV = null;
    private ImageView sketch3IV = null;

    private Bitmap BM1 = null;
    private Bitmap BM2 = null;
    private Bitmap BM3 = null;


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

    public void ballDrop(View view) {
        mda.putBallsOnPath();
    }


    public void eraseDrawing(View view) {
        mda.clearDrawing();
    }
}