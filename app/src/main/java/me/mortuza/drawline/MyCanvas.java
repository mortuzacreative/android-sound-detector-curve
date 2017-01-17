package me.mortuza.drawline;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by mortuza on 1/14/2017.
 */

public class MyCanvas extends SurfaceView implements Runnable {

    SurfaceHolder surfaceHolder;
    Canvas canvas;
    SoundMeter soundMeter;
    boolean canDraw = false;
    Context context;
    int displayX;
    int displayY;
    Paint paint;
    Thread thread;
    Bitmap bitmapBg;
    Random random;
    ArrayList<ModelCoordinate> coordinatesList;
    int portion = 100;
    int xStart = 0;
    int yStart = 0;
    int xEnd = 0;
    int yEnd = 0;
    static int INCREASE_VALUE;


    public MyCanvas(Context context) {
        super(context);
        this.context = context;
        initializeAll();
    }


    public void initializeAll() {
        soundMeter = new SoundMeter();
        try {
            soundMeter.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        coordinatesList = new ArrayList<>();
        surfaceHolder = getHolder();
        random = new Random();
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(2);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        displayX = displaymetrics.widthPixels;
        displayY = displaymetrics.heightPixels;
        INCREASE_VALUE = displayX / portion;

        coordinatesList.add(new ModelCoordinate(0, displayY / 2, displayX / portion, displayY/2));

        bitmapBg = BitmapFactory.decodeResource(context.getResources(), R.drawable.bg);
        bitmapBg = Bitmap.createScaledBitmap(bitmapBg, displayX, displayY, true);
    }


    @Override
    public void run() {
        while (canDraw) {
            if (!surfaceHolder.getSurface().isValid()) {
                continue;
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            canvas = surfaceHolder.lockCanvas();
            // make random num between 150 to 250
            //int randomNum = random.nextInt((250 - 150) + 1) + 150;

            //--- get current amplitude ------
            int amplitude = soundMeter.getAmplitude();
            double db =  20 * Math.log10((double)Math.abs(amplitude));
            db = (db > 10 && db < 200) ? db : 30;
            int dbRate = displayY/2 - (int) db;

            canvas.drawBitmap(bitmapBg, 0, 0, paint);
            drawCurve(dbRate, db);
            //canvas.drawLine(0, displayY/2, displayX, displayY/2, paint);
            surfaceHolder.unlockCanvasAndPost(canvas);

        }
    }



    private void drawCurve(int randomNum, double db){
        Paint paint1 = new Paint();
        paint1.setColor(Color.WHITE);
        paint1.setTextSize(50);
        int dbOriginal = (int) db;
        canvas.drawText("dB Rate: "+dbOriginal, displayX/4, displayY/4, paint1);

        //---- first time draw in whole display ------
        if (coordinatesList.size() < portion+8) {
            drawLines();
            //-- update arrayList---
            xStart += displayX / portion;
            yStart = yEnd;
            xEnd += displayX / portion;
            coordinatesList.add(new ModelCoordinate(xStart, yStart, xEnd, randomNum));
            canvas.drawLine(xStart, yStart, xEnd, randomNum, paint);
        } else {
            //-- remove first index from arrayList ----
            coordinatesList.remove(0);
            //--- update whole arraylist---
            updateList(randomNum);
            drawLines();
        }
    }


    //--- draw curve from arrayList----
    private void drawLines() {

        for (int i = 0; i < coordinatesList.size(); i++) {
            ModelCoordinate modelCoordinate;
            modelCoordinate = coordinatesList.get(i);
            xStart = modelCoordinate.getxStart();
            yStart = modelCoordinate.getyStart();
            xEnd = modelCoordinate.getxEnd();
            yEnd = modelCoordinate.getyEnd();
            canvas.drawLine(xStart, yStart, xEnd, yEnd, paint);
        }

    }

    //---- update each list xStart and xEnd position & add a new one -------
    private void updateList(int random) {
        for (int i = 0; i < coordinatesList.size(); i++) {
            ModelCoordinate modelCoordinate = coordinatesList.get(i);
            coordinatesList.set(i, new ModelCoordinate(modelCoordinate.getxStart() - displayX / portion, modelCoordinate.getyStart(),
                    modelCoordinate.getxEnd() - displayX / portion, modelCoordinate.getyEnd()));
        }
        ModelCoordinate modelCoordinate1 = coordinatesList.get(coordinatesList.size() - 1);
        coordinatesList.add(new ModelCoordinate(modelCoordinate1.getxEnd(), modelCoordinate1.getyEnd(),
                modelCoordinate1.getxEnd() + INCREASE_VALUE, random));
    }


    public void start() {
        canDraw = true;
        thread = new Thread(this);
        thread.start();
        try {
            soundMeter.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void stop() {
        canDraw = false;
        while (true) {
            try {
                thread.join();
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        thread = null;
    }


    public void stopSoundMeter(){
        soundMeter.stop();
    }


}
