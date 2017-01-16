package me.mortuza.drawline;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

public class MainActivity extends AppCompatActivity {

    MyCanvas myCanvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        myCanvas = new MyCanvas(this);
        setContentView(myCanvas);

    }

    @Override
    protected void onResume() {
        super.onResume();
        myCanvas.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        myCanvas.stop();
        myCanvas.stopSoundMeter();
    }

    @Override
    protected void onStop() {
        super.onStop();
        myCanvas.stopSoundMeter();
    }
}
