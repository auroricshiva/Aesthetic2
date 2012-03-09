package com.colintony.aes2;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.content.res.Configuration;

/**
 * This is a simple Aesthetic2 activity that houses a single Athetic2View.
 */
public class Aesthetic2Activity extends Activity {

	private Bitmap[] bits = new Bitmap[10];
	
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
        setContentView(R.layout.main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Window window = getWindow();  
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        // get handles to the Aesthetic2View from XML, and its Aesthetic2Thread
        mAesthetic2View = (Aesthetic2View) findViewById(R.id.aes2);
        


       	bits = new Bitmap[10];
	    bits[0] = BitmapFactory.decodeResource(getResources(), R.drawable.clouds4);
	    bits[1] = BitmapFactory.decodeResource(getResources(), R.drawable.grassblock);
	    bits[2] = BitmapFactory.decodeResource(getResources(), R.drawable.stoneblock);
	    bits[3] = BitmapFactory.decodeResource(getResources(), R.drawable.dirtblock);
	    bits[4] = BitmapFactory.decodeResource(getResources(), R.drawable.wallblock);
	    bits[5] = BitmapFactory.decodeResource(getResources(), R.drawable.woodblock);
	    bits[6] = BitmapFactory.decodeResource(getResources(), R.drawable.plainblock);
	    bits[7] = BitmapFactory.decodeResource(getResources(), R.drawable.characterboy);
	    mAesthetic2View.setBits(bits);

        System.gc();
    }

    /**
     * Invoked when the Activity loses user focus.
     */
    @Override
    protected void onPause() {
        super.onPause();
        System.gc();
    }
    

    @Override
    protected void onStop() {
        super.onStop();
        //for(int i = 0; i < bits.length; i++) if(bits[i]!=null)bits[i].recycle();
        bits = null;
        System.gc();

        mAesthetic2View.getThread().setRunning(false); // pause game when Activity pauses
        //unbindDrawables(findViewById(R.id.aes2));
        System.gc();
    }
    
    private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
        view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
            unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
        ((ViewGroup) view).removeAllViews();
        }
    }

    @Override
	public void onConfigurationChanged(Configuration newConfig)  {
        //super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        onPause();
        onResume();
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
    
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}