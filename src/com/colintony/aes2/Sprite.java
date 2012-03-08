package com.colintony.aes2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Sprite {
	private Bitmap animation;
	private int xPos;
	private int yPos;
	private Rect sRectangle;
	public int numFrames;
	public int currentFrame;
	private int spriteHeight;
	private int spriteWidth;
	private int update;
	
	public Sprite(Bitmap bitmap, int frameCount, int x, int y) {
		sRectangle = new Rect(0, 0, 0, 0);
		currentFrame = 0;
		xPos = x;
		yPos = y;
		
		this.animation = bitmap;
		this.spriteHeight = bitmap.getHeight();
		this.spriteWidth = bitmap.getWidth()/frameCount;
		this.sRectangle.top = 0;
		this.sRectangle.bottom = spriteHeight;
		this.sRectangle.left = 0;
		this.sRectangle.right = spriteWidth;
		this.numFrames = frameCount;
	}
	
	
	public int getXPos() {
		return xPos;
	}
	
	public int getYPos() {
		return yPos;
	}
	
	public void setXPos(int value) {
		xPos = value;
	}
	
	public void setYPos(int value) {
		yPos = value;
	}
	
	public void Update() {
		update++;
		if(update%2 == 0){
			if( currentFrame < numFrames ) currentFrame++;
			
			sRectangle.left = currentFrame * spriteWidth;
			sRectangle.right = sRectangle.left + spriteWidth;
		}
	}
	
	public void draw(Canvas canvas, int mx, int my) {
		Rect dest = new Rect(getXPos()+mx, getYPos()+my, getXPos()+mx + spriteWidth,
										getYPos()+my + spriteHeight);
		canvas.drawBitmap(animation, sRectangle, dest, null);
	}
}
