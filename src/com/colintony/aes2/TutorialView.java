package com.colintony.aes2;

import java.util.LinkedList;
import java.util.Stack;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;



class TutorialView extends SurfaceView implements SurfaceHolder.Callback {
	
	Canvas c;
	Boolean set = false;
	
	private boolean levelEnd[] = new boolean[10];
	private boolean won = false;
	private boolean tut = true;
	
    private int curLevel = 1;
	private int state = 0;
	private int jState = 1;
	private int jUpDown;
	private int click = 0;
	int life = 3;
	final static private int MAX_SIZE = 6;

	private float mx;
	private float my;
    private float jHeight = 0, jScale = 0.75f;
    private float endx;
    private float colx;

	private float oscale = 0;
	
	/** Height and Width */
	private int mCanvasHeight = 1000;
	private int mCanvasWidth = 1000;
    
    int mapLevel[][], mapCollectables[][];
    int lines[][] = new int [4][8];
    Levels level = new Levels();
    int pointer = 0;
    int row = 0, col = 3;
    int rowy = 0;
    int jump = 0;
    int cont = 0;
    
    	/** images */
    private Bitmap BackgroundImage;
    private Bitmap tutback;
    private Bitmap character;
    private Bitmap shadow;
    private Bitmap[] land = new Bitmap[10];
    private Bitmap[] arrows = new Bitmap[3];
    private Bitmap[] collectables = new Bitmap[9];
    private Bitmap[] bars = new Bitmap[12];
    Sprite csprite;
    
    /** containers */
    private Stack<Integer> gemStack = new Stack<Integer>();
    private LinkedList<Integer> gemQueue = new LinkedList<Integer>();
        
    class TutorialThread extends Thread {
        

        /** Handle to the surface manager object we interact with */
        private SurfaceHolder SurfaceHolder = null;
        
        /** Indicate whether the surface has been created & is ready to draw */
        private boolean mRun = false;
        
        /** Used to figure out elapsed time between frames */
        private long mLastTime = 0;
        
        public TutorialThread(SurfaceHolder surfaceHolder, Context context, Handler handler) {
            // get handles to some important objects
            SurfaceHolder = surfaceHolder;
            level.setLevel(0);
            mapLevel = level.getLevelMap();
            mapCollectables = level.getCollectableMap();
            state = 0;
            
            lines[0][0] = 7;
            lines[0][1] = 8;
            lines[0][2] = 9;
            lines[0][3] = 9;
            lines[0][4] = 9;
            lines[0][5] = 9;

            

            lines[3][0] = 7;
            lines[3][1] = 8;
            lines[3][2] = 9;
            lines[3][3] = 9;
            lines[3][4] = 9;
            lines[3][5] = 0;

            pointer = 5;
            
        }
        
        
        public TutorialThread(SurfaceHolder surfaceHolder, Handler handler) {
        	SurfaceHolder = surfaceHolder;
        }

		/**
         * Draws
         */
        private void doDraw(Canvas canvas)
        {
        	canvas.save();
        	canvas.scale(oscale,oscale,0,0);
        	
        	canvas.save();
        	canvas.scale(0.5f,0.5f,0,0);
	        canvas.drawBitmap(BackgroundImage, 0, 0, null);
	        canvas.restore();
	        
	        canvas.save();
	        canvas.scale(0.6f,0.6f,0,mCanvasHeight);
	        drawLand(mapLevel, canvas);
	        canvas.restore();

			canvas.save();
	        canvas.scale(0.6f,0.6f,0,mCanvasHeight);
			if(!levelEnd[curLevel]) drawShadows(mapCollectables, canvas);
	        canvas.restore();
	        
	        canvas.save();
	        jScale = (float)(0.75*(1-jHeight/180));
            canvas.scale(jScale, jScale, shadow.getWidth()/2, shadow.getHeight()*3/4);
            canvas.drawBitmap(shadow, (float)((150+(int)endx)/jScale), (float)(100+rowy*3/4)/jScale, null);
            canvas.restore();
	        canvas.scale(0.75f,0.75f,0,0);
            csprite.draw(canvas, 200+(int)endx, 180+rowy-Math.round(jHeight));
			canvas.restore();
			
			canvas.save();
	        canvas.scale(0.6f,0.6f,0,mCanvasHeight);
			if(!levelEnd[curLevel]) drawCollectables(mapCollectables, canvas);
	        canvas.restore();
			
			canvas.save();
			canvas.scale(0.25f,0.25f,0,0);
			canvas.drawBitmap(arrows[0],-4*endx+4*(mCanvasWidth)-arrows[0].getWidth()-40, 300, null);
			canvas.drawBitmap(arrows[1],-4*endx+4*(mCanvasWidth)-arrows[0].getWidth()-40, 4*mCanvasHeight-arrows[1].getHeight() - 300, null);
			canvas.drawBitmap(arrows[2],-4*endx+4*(mCanvasWidth)-arrows[0].getWidth()-40, 4*mCanvasHeight/2-arrows[2].getHeight()/2, null);
			canvas.restore();
			
			
			canvas.save();
			canvas.scale(0.33f,0.33f,0,0);
			if(!won) canvas.drawBitmap(bars[8],-2*endx+700,3*endx+1500, null);
			else canvas.drawBitmap(bars[7],-2*endx+700,3*endx+1500, null);
			canvas.drawBitmap(bars[10],-2*endx+1300,3*endx+1500, null);
			if(click == 3)canvas.drawBitmap(bars[11],-2*endx+1300,3*endx+1500, null);
			if(click == 4)canvas.drawBitmap(bars[9],-2*endx+700,3*endx+1500, null);
			canvas.restore();
			
			
			canvas.save();
			canvas.scale(0.66f,0.66f,0,0);
			if(click == 1)canvas.drawBitmap(bars[5],(int)(-1*endx+0)-5,5, null);
			if(click == 2)canvas.drawBitmap(bars[6],(int)(-1*endx+0)+80,5, null);
			canvas.drawBitmap(bars[0],(int)(-1*endx+0),0, null);
			canvas.drawBitmap(bars[1],(int)(-1.4*endx+0),0, null);
			canvas.drawBitmap(bars[2],-1*endx,-1*endx+0, null);
			canvas.drawBitmap(bars[2],-1*endx,2*endx+0-40+800, null);
			canvas.drawBitmap(bars[4-Math.min(1,(life/3))],mCanvasWidth+2*bars[3].getWidth()-220,endx+60, null);
			canvas.drawBitmap(bars[4-Math.min(1,(life/2))],mCanvasWidth-220,endx+60, null);
			canvas.drawBitmap(bars[4-Math.min(1,(life/1))],mCanvasWidth-2*bars[3].getWidth()-220,endx+60, null);
			canvas.restore();
			
			canvas.save();
			canvas.scale(0.25f,0.25f,0,0);
//			System.out.println(lineQueue.length + " " + lineStack.length + " " + lineQueue.toString() + lineStack.toString());
			for(int i = 0; i < 6; i++){
				if(lines[0][i] != 0) canvas.drawBitmap(collectables[lines[0][i]-1],870+260*i-(int)(4*.66*endx),10-(int)(4*.66*endx), null);
			}
			for(int i = 0; i < gemQueue.size(); i++){
				if(gemQueue.get(i) != 0) canvas.drawBitmap(collectables[gemQueue.get(i)-1],54+(int)(-4*.66*endx+0),340+(gemQueue.size()-i-1)*210, null);
			}
			for(int i = 0; i < gemStack.size(); i++){
				if(gemStack.get(i) != 0) canvas.drawBitmap(collectables[gemStack.get(i)-1],245+(int)(-4*.66*1.4*endx+0),340+(5-i)*210, null);
			}
			if(levelEnd[curLevel]){
				for(int i = 0; i < 6; i++){
					if(lines[3][i] != 0) canvas.drawBitmap(collectables[lines[3][i]-1],870+260*i-(int)(4*.66*endx),410-(int)(4*.66*endx), null);
				}
			}
			canvas.restore();
			
			
			canvas.save();
			canvas.scale(0.66f,0.66f,0,0);
			if (tut) canvas.drawBitmap(tutback,0,0, null);
			canvas.restore();
        }
          
        
        private void drawLand(int[][] map, Canvas canvas){
        	for(int j = 0; j < map.length; j++){
        		canvas.drawBitmap(land[2], -mx/0.6f+151*j, my+60*4, null);
				for(int i = 0; i < map[j].length; i++){
					try{
						if(map[j][i] != 0)
						    canvas.drawBitmap(land[map[j][i]-1], -mx/0.6f+151*j, my+60*i-120, null);
					}
					catch(Exception E){}
				}
				
        	}
		}
        
        private void drawCollectables(int[][] mapCollect, Canvas canvas)
        {
           for(int i = 0; i < mapCollect.length; i++)
           {
               for(int j = 0; j < mapCollect[i].length; j++)
               {
                   if(mapCollect[i][j] > 0){
                	   canvas.save();
                   	   canvas.scale(.5f,.5f,0,0);
                       canvas.drawBitmap(collectables[mapCollect[i][j]-1], 2*(-mx/0.6f+151*i), 2*(my+120*j-60)+16f*(float)Math.cos(cont/10f), null);
                       canvas.restore();
                   }
               }
           }
        }
        
        private void drawShadows(int[][] mapCollect, Canvas canvas)
        {
           for(int i = 0; i < mapCollect.length; i++)
           {
               for(int j = 0; j < mapCollect[i].length; j++)
               {
                   if(mapCollect[i][j] > 0){
                	   canvas.save();
                   	   canvas.scale(.5f,.5f,0,0);
                   	   canvas.drawBitmap(shadow, 2*(-mx/0.6f+151*i), 2*(my+120*j), null);
                       canvas.restore();
                   }
               }
           }
        }


		/***
         * Updates the game each time it is called
         */
        private void update() {
        	//Measure time
            long now = System.currentTimeMillis();
            double elapsed = (now - mLastTime);
            
            //Update the default gameplay
            if(state == 0)
            {
	            if(elapsed > 7)
	            {
	            	mLastTime = now;
	            	cont++;
	            	if(!levelEnd[curLevel]) {
	            		csprite.Update2();
		                if(endx != 0)
		                    endx +=2;
	            	}
	            	else csprite.setEnd();

	            	if(mx < 151*(mapLevel.length-59)*0.6*(curLevel))
	            	{
	            	    mx+=5;
	            	    colx+=5;
	            	}
	            	else if(!levelEnd[curLevel])
	            	{
	            	    levelEnd[curLevel] = true;
	            	    row = 2;
	            	}
	            }
	            
                if(rowy < row*96)
                {
                    rowy += Math.max((row*96-rowy)/8, 5);
                    if(rowy > row*96)
                        rowy = row*96;
                }
                else if(rowy > row*96)
                {
                    rowy += Math.min((row*96-rowy)/8, -5);
                    if(rowy < row*96)
                        rowy = row*96;
                }
                
                //collision
                if(colx > 90.6 && col < mapLevel.length-9)
                {
                    colx -= 90.6;
                    col++;
                }
                
                //gem is on ground
                if(mapCollectables[col][row] != 0)
                {
                    int temp = mapCollectables[col][row];
                    mapCollectables[col][row] = 0;
                    if(temp < 4)//queue
                    {
                        gemQueue.add(temp);
                        if(gemQueue.size() > MAX_SIZE)
                            gemQueue.remove();
                    }
                    else if(gemStack.size() < MAX_SIZE)//stack
                    {
                        gemStack.push(temp);
                    }
                }
                
                //jump
	            if(jump == 1)
	            {
	                if(jUpDown == 1)
	                {
	                    jHeight += 60/(Math.pow(jState++,1.3));
	                    if(jHeight > 150)
	                    {
	                        jHeight = 150;
	                        jUpDown = 0;
	                    }
	                }
	                else
	                {
	                    jHeight -= 60/(Math.pow(jState--,1.3));
	                    if(jHeight < 10)
	                    {
	                        jHeight = 0;
	                        jump = 0;
	                        jState = 1;
	                    }
	                }
	            }
	            
	            //end level animation
	            if(levelEnd[curLevel])
	            {
	                if(endx != -200)
	                    endx -=2;
	            }
	            
	            
	            //win check
	            won = true;
	            for(int i = 0; i<6; i++){
	            	if(lines[3][i]%3 != lines[0][i]%3 || lines[3][i] == 0) won = false;
	            }
            }
            
        }

        @Override
        public void run() {
            while (mRun && set) {
               	c = null;
                try {
                    c = SurfaceHolder.lockCanvas(null);
                    synchronized (SurfaceHolder) {
                        //if (mode == STATE_RUNNING) 
                    	update();
                        doDraw(c);
                    }
                } finally {
                    // do this in a finally so that if an exception is thrown
                    // during the above, we don't leave the Surface in an
                    // inconsistent state
                    if (c != null) {
                        SurfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }
        }

        public void setRunning(boolean b) {
            mRun = b;
        }
        
    }

    /** The thread that actually draws the animation */
    private TutorialThread thread;
    
    public TutorialView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // register our interest in hearing about changes to our surface
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        holder.setSizeFromLayout();
        //holder.
        // create thread only; it's started in surfaceCreated()
        thread = new TutorialThread(holder, context, new Handler() {
        });

        setFocusable(true); // make sure we get key events
    }

    
    /**
     * Fetches the animation thread corresponding to this TutorialView.
     * 
     * @return the animation thread
     */
    public TutorialThread getThread() {
        return thread;
    }

    
    /**
     * Standard override to get key-press events.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent msg) {
        //return thread.doKeyDown(keyCode, msg);
    	return true;
    }

    
    /**
     * Standard override for key-up. We actually care about these.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent msg) {
        //return thread.doKeyUp(keyCode, msg);
    	return true;
    }

    
    /**
     * Standard window-focus override. Notice focus lost so we can pause on
     * focus lost. e.g. user switches to take a call.
     */
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        //if (!hasWindowFocus) thread.setRunning(false);
    }

    /*
     * Callback invoked when the Surface has been created and is ready to be
     * used.
     */
    @Override
	public void surfaceCreated(SurfaceHolder holder) {
        // start the thread here so that we don't busy-wait in run()
        // waiting for the surface to be created
        try{
        	thread.start();
        	thread.setRunning(true);
        }catch(Exception ex){
        	thread = new TutorialThread(getHolder(), getHandler());
        	thread.start();
        	thread.setRunning(true);
        }  

    }

    
    /*
     * Callback invoked when the Surface has been destroyed and must no longer
     * be touched. WARNING: after this method returns, the Surface/Canvas must
     * never be touched again!
     */
    @Override
	public void surfaceDestroyed(SurfaceHolder holder) {
        // we have to tell thread to shut down & wait for it to finish, or else
        // it might touch the Surface after we return and explode
    	
        boolean retry = true;
        thread.setRunning(false);
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
        System.gc();
    }
    

    
    /* Callback invoked when the surface dimensions change. */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height) {
    	onSizeChanged(width, height, mCanvasWidth, mCanvasHeight);
	}	
    

  /* 
      Called automatically when the view's orientation changes or gets resized etc. 
  */
    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld){
        super.onSizeChanged(xNew, yNew, xOld, yOld);
        mCanvasWidth = xNew;
        mCanvasHeight = yNew;
        /*
          the onSizeChanged() method will be called automatically before the View gets 
           layed out, or drawn the first time. Also, whenever the orientation changes, 
           this view's onSizeChanged() method is automatically called again and, hence, 
           this view's new dimensions are obtained. 
           */
        oscale = xNew/800;
    }
   
    protected void setBits(Bitmap bits[])
    {
        BackgroundImage = bits[0];
        land[0] = bits[1];
        land[1] = bits[2];
        land[2] = bits[3];
        land[3] = bits[4];
        land[4] = bits[5];
        land[5] = bits[6];
        land[6] = bits[11];
        character = bits[7];
        csprite = new Sprite(character, 16, 0, 0);
        arrows[0] = bits[8];
        arrows[1] = bits[9];
        arrows[2] = bits[10];
        shadow = bits[13];
        collectables[0] = bits[14];
        collectables[1] = bits[15];
        collectables[2] = bits[16];
        collectables[3] = bits[17];
        collectables[4] = bits[18];
        collectables[5] = bits[19];
        collectables[6] = bits[20];
        collectables[7] = bits[21];
        collectables[8] = bits[22];
        bars[0] = bits[23];
        bars[1] = bits[24];
        bars[2] = bits[25];
        bars[3] = bits[26];
        bars[4] = bits[27];
        bars[5] = bits[28];
        bars[6] = bits[29];
        bars[7] = bits[30];
        bars[8] = bits[31];
        bars[9] = bits[32];
        bars[10] = bits[33];
        bars[11] = bits[34];
        
        tutback = bits[35];
        
        set = true;
    }
    
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		
	  if(state == 0)
	  {
		int point;
		int eventAction = event.getAction();
		int actionCode = eventAction & MotionEvent.ACTION_MASK;
		
  	  	int pickx;
  	  	int picky;
  	  	
  	    if (actionCode == MotionEvent.ACTION_POINTER_DOWN) {

  	  	}
  	    	  	
		if (eventAction == MotionEvent.ACTION_DOWN) {
			point = event.getPointerId(0);
			pickx = (int)event.getX(point);
            picky = (int)event.getY(point);
             
            if(!levelEnd[curLevel]){
	            if(pickx > mCanvasWidth - 70 )
	            {
	                if(picky < mCanvasHeight / 3)
	                {
	                    if(row > 0)
	                        row--;
	                }
	                else if(picky < mCanvasHeight * 2 / 3)
	                {
	                    if(jump != 1)
	                    {
	                        jump = 1;
	                        jUpDown = 1;
	                    }
	                }
	                else if(row < 2)
	                    row++;
	            }
            }
            else if (endx == -200){
	            if(pickx > mCanvasWidth/8 && pickx < mCanvasWidth/4)
	            {
	            	if( pointer < 6 && !gemQueue.isEmpty()){
	            		lines[3][pointer] = gemQueue.removeFirst();		      
		            	pointer++;
		            	click = 1;
	            	}
	            }
	            else if(pickx > mCanvasWidth/4 && pickx < mCanvasWidth/3)
	            {
	            	if( pointer < 6 && !gemStack.isEmpty()){
		            	lines[3][pointer] = gemStack.pop();
		            	pointer++;
		            	click = 2;
	            	}
	            }
	            else if(pickx > mCanvasWidth/4*3 && pickx < mCanvasWidth/8*7 && picky > mCanvasHeight/3*2)
	            {
	            	if(pointer>0){
		            	if(lines[3][pointer-1] < 4) gemQueue.addFirst(lines[3][pointer-1] );
		            	else if( lines[3][pointer-1] < 7) gemStack.push(lines[3][pointer-1] );
		            	lines[3][pointer-1] = 0;
		            	pointer--;
	            		click = 3;	            		
	            	}
	            }
	            else if(pickx > mCanvasWidth/2 && pickx < mCanvasWidth/5*3 && picky > mCanvasHeight/3*2)
	            {
	            	if(won) {
	            		click = 4;
	            		curLevel++;
	            		won = false;
	            		pointer = 0;
	            		lines[3] = new int[8];
	            		life = 3;
	            		while(!gemStack.isEmpty()) gemStack.pop();
	            		while(!gemQueue.isEmpty()) gemQueue.remove();
	            	}
	            }
            }
		}
  	  	
  	  	if (actionCode == MotionEvent.ACTION_POINTER_UP) {
  	  		
  	  	}
  	  	
		if (eventAction == MotionEvent.ACTION_UP) {
			click = 0;
		}
			
		if (eventAction == MotionEvent.ACTION_MOVE) {
	
		}

		return true;
	  }
	  
	  
	  
	  
	  
	  return true;

}

}
