package com.colintony.aes2;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.content.res.Configuration;

/**
 * This is a simple Aesthetic2 activity that houses a single Aesthetic2View.
 */
public class Aesthetic2Activity extends Activity {

	private Bitmap[] bits = null;
	
    /** A handle to the View in which the game is running. */
    private Aesthetic2View mAesthetic2View;

    /**
     * Invoked when the Activity is created.
     * 
     * @param savedInstanceState a Bundle containing state saved from a previous
     *        execution, or null if this is a new execution
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // tell system to use the layout defined in our XML file
        setContentView(R.layout.aes2activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Window window = getWindow();  
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        // get handles to the Aesthetic2View from XML, and its Aesthetic2Thread
        mAesthetic2View = (Aesthetic2View) findViewById(R.id.aes2activity);
        
        System.gc();
        
        if(bits == null)
        {
            bits = new Bitmap[40];
            bits[0] = BitmapFactory.decodeResource(getResources(), R.drawable.clouds4);
            bits[1] = BitmapFactory.decodeResource(getResources(), R.drawable.grassblock);
            bits[2] = BitmapFactory.decodeResource(getResources(), R.drawable.stoneblock);
            bits[3] = BitmapFactory.decodeResource(getResources(), R.drawable.dirtblock);
            bits[4] = BitmapFactory.decodeResource(getResources(), R.drawable.wallblock);
            bits[5] = BitmapFactory.decodeResource(getResources(), R.drawable.woodblock);
            bits[6] = BitmapFactory.decodeResource(getResources(), R.drawable.plainblock);
            bits[7] = BitmapFactory.decodeResource(getResources(), R.drawable.shuma2);
            bits[8] = BitmapFactory.decodeResource(getResources(), R.drawable.arrow1);
            bits[9] = BitmapFactory.decodeResource(getResources(), R.drawable.arrow2);
            bits[10] = BitmapFactory.decodeResource(getResources(), R.drawable.circle);
            bits[11] = BitmapFactory.decodeResource(getResources(), R.drawable.gameover);
            
            bits[13] = BitmapFactory.decodeResource(getResources(), R.drawable.shadow);
            bits[14] = BitmapFactory.decodeResource(getResources(), R.drawable.gemblue2);
            bits[15] = BitmapFactory.decodeResource(getResources(), R.drawable.gemgreen2);
            bits[16] = BitmapFactory.decodeResource(getResources(), R.drawable.gemorange2);
            bits[17] = BitmapFactory.decodeResource(getResources(), R.drawable.gemblue);
            bits[18] = BitmapFactory.decodeResource(getResources(), R.drawable.gemgreen);
            bits[19] = BitmapFactory.decodeResource(getResources(), R.drawable.gemorange);
            bits[20] = BitmapFactory.decodeResource(getResources(), R.drawable.blue1);
            bits[21] = BitmapFactory.decodeResource(getResources(), R.drawable.green1);
            bits[22] = BitmapFactory.decodeResource(getResources(), R.drawable.orange1);
            
            bits[23] = BitmapFactory.decodeResource(getResources(), R.drawable.queue);
            bits[24] = BitmapFactory.decodeResource(getResources(), R.drawable.stack);
            bits[25] = BitmapFactory.decodeResource(getResources(), R.drawable.topbar);
            bits[26] = BitmapFactory.decodeResource(getResources(), R.drawable.heart1);
            bits[27] = BitmapFactory.decodeResource(getResources(), R.drawable.heart2);
            bits[28] = BitmapFactory.decodeResource(getResources(), R.drawable.queue2);
            bits[29] = BitmapFactory.decodeResource(getResources(), R.drawable.stack2);
            bits[30] = BitmapFactory.decodeResource(getResources(), R.drawable.check);
            bits[31] = BitmapFactory.decodeResource(getResources(), R.drawable.check2);
            bits[32] = BitmapFactory.decodeResource(getResources(), R.drawable.check3);
            bits[33] = BitmapFactory.decodeResource(getResources(), R.drawable.undo);
            bits[34] = BitmapFactory.decodeResource(getResources(), R.drawable.undo2);
            
            bits[35] = BitmapFactory.decodeResource(getResources(), R.drawable.rock);
            
    	    mAesthetic2View.setBits(bits);
        }

        System.gc();
    }

    /**
     * Invoked when the Activity loses user focus.
     */
    @Override
    protected void onPause() {
        super.onPause();
        finish();
        System.gc();
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.gc();
    }

    
    /**
     * Notification that something is about to happen, to give the Activity a
     * chance to save state.
     * 
     * @param outState a Bundle into which this Activity should save its state
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // just have the View's thread save its state into our Bundle
        super.onSaveInstanceState(outState);
        //mAesthetic2Thread.saveState(outState);
        Log.w(this.getClass().getName(), "SIS called");
    }
    
}