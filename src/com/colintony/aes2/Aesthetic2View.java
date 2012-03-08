package com.colintony.aes2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;



class Aesthetic2View extends SurfaceView implements SurfaceHolder.Callback {
	
	Canvas c;
	Boolean set = false;
	
	private int state = 0;

	private float mx, mx2;
	private float my, my2;

	private float oscale = 0;
	
	/** Height and Width */
	private int mCanvasHeight = 1000;
	private int mCanvasWidth = 1000;
	
    private Paint paint = new Paint();
    
    
    	/** images */
    private Bitmap BackgroundImage;
 
        
    class Aesthetic2Thread extends Thread {
        

        /** Handle to the surface manager object we interact with */
        private SurfaceHolder SurfaceHolder = null;
        
        /** Indicate whether the surface has been created & is ready to draw */
        private boolean mRun = false;
        
        /** Used to figure out elapsed time between frames */
        private long mLastTime = 0;
        

        
        
        
        
        
        
        
        
        public Aesthetic2Thread(SurfaceHolder surfaceHolder, Context context, Handler handler) {
            // get handles to some important objects
            SurfaceHolder = surfaceHolder;

            state = 0;
        }
        
        
        public Aesthetic2Thread(SurfaceHolder surfaceHolder, Handler handler) {
        	SurfaceHolder = surfaceHolder;
        }
        
        
        
        
 








		/**
         * Draws
         */
        private void doDraw(Canvas canvas) {
        	canvas.save();
        	
        	canvas.scale(0.5f,0.5f,mx,my);
	        canvas.drawBitmap(BackgroundImage, mx, my, null);
	        	
			canvas.restore();
        }
          
          
          

        
        
        
        
        
        
        
        
        /***
         * Updates the game each time it is called
         */
        private void update() {
        	//Measure time
            long now = System.currentTimeMillis();
            double elapsed = (now - mLastTime);
            
            
            //Update the default gameplay
            if(state == 0){
	            if(elapsed > 10){
	            	mLastTime = now;
	            	
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
    private Aesthetic2Thread thread;

    
    
    public Aesthetic2View(Context context, AttributeSet attrs) {
        super(context, attrs);

        // register our interest in hearing about changes to our surface
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        holder.setSizeFromLayout();
        //holder.
        // create thread only; it's started in surfaceCreated()
        thread = new Aesthetic2Thread(holder, context, new Handler() {
        });

        setFocusable(true); // make sure we get key events
    }

    
    /**
     * Fetches the animation thread corresponding to this Aesthetic2View.
     * 
     * @return the animation thread
     */
    public Aesthetic2Thread getThread() {
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
        	thread = new Aesthetic2Thread(getHolder(), getHandler());
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
   
    protected void setBits(Bitmap bits[]){
        BackgroundImage = bits[0];
        set = true;
    }



    
    
 

	boolean move = false;

    double oldDist;
    
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
	  if(state == 0){
		int point;
		int eventaction = event.getAction();
		int actionCode = eventaction & MotionEvent.ACTION_MASK;
		
  	  	int pickx;
  	  	int picky;
  	  	
  	    if (actionCode == MotionEvent.ACTION_POINTER_DOWN) {

  	  	}
  	    	  	
		if (eventaction == MotionEvent.ACTION_DOWN) {
			
		}
  	  	
  	  	if (actionCode == MotionEvent.ACTION_POINTER_UP) {
  
  	  	}
  	  	
		if (eventaction == MotionEvent.ACTION_UP) {
			 point = event.getPointerId(0);
			
		}
			
		if (eventaction == MotionEvent.ACTION_MOVE) {
	
		}

		return true;
	  }
	  
	  
	  
	  
	  
	  return true;

}

}
