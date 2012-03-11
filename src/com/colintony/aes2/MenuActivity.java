package com.colintony.aes2;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MenuActivity extends Activity
{
	public static final String PREFS_NAME = "Aes2Preferences";
	static SharedPreferences saves;

	int max;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        
        saves = getSharedPreferences(PREFS_NAME, 0);
        max = saves.getInt("max", 0);
        
        Button StartGameButton = (Button)findViewById(R.id.StartGame);
        StartGameButton.setOnClickListener(new OnClickListener() {
            
            public void onClick(View v) {
                Intent StartGameIntent = new Intent(MenuActivity.this,Aesthetic2Activity.class);
                startActivity(StartGameIntent);
            }
        });
        
        Button TutorialButton = (Button)findViewById(R.id.Tutorial);
        TutorialButton.setOnClickListener(new OnClickListener() {
            
            public void onClick(View v) {
                Intent TutorialIntent = new Intent(MenuActivity.this,TutorialActivity.class);
                startActivity(TutorialIntent);
            }
        });
    }
    
    @Override
    public void onPause(){ 
	    super.onPause();
    }
}
