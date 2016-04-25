package com.goldrushcomputing.playsound;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import org.artoolkit.ar.base.ARToolKit;
import org.artoolkit.ar.base.rendering.ARRenderer;
import org.artoolkit.ar.base.rendering.Cube;

import android.content.Context;
import android.opengl.Matrix;
import android.util.Log;

public class DrumsRenderer extends ARRenderer {
	static String TAG = "DrumsRenderer";
	private Context context;
	private Example activity;
	
	private int drum1_MarkerID = -1;
	private int drum2_MarkerID = -1;
	private int drum3_MarkerID = -1;
	
	private long drum1LastSeen = -1;
	private long drum2LastSeen = -1;
	private long drum3LastSeen = -1;
	
	private Button drum1Button = new Button(1.0f,0.0f,0.0f);
	private Button drum2Button = new Button(1.0f,0.0f,0.0f);
	private Button drum3Button = new Button(1.0f,0.0f,0.0f);

	private Cube cube = new Cube(40.0f, 0.0f, 0.0f, 20.0f);
	
	public DrumsRenderer(Example activity){
		this.activity = activity;
	}
	
	@Override
	public boolean configureARScene() {
		
		/*
		drum1_MarkerID = ARToolKit.getInstance().addMarker("single;Data/patt.drum1;88");
		drum2_MarkerID = ARToolKit.getInstance().addMarker("single;Data/patt.drum2;88");
		drum3_MarkerID = ARToolKit.getInstance().addMarker("single;Data/patt.drum3;88");
		*/
		
		
		drum1_MarkerID = ARToolKit.getInstance().addMarker("single;Data/C.pat;64");
		drum2_MarkerID = ARToolKit.getInstance().addMarker("single;Data/Dm.pat;64");
		drum3_MarkerID = ARToolKit.getInstance().addMarker("single;Data/Em.pat;64");
		
		
		if (drum1_MarkerID < 0 || drum2_MarkerID<0 || drum3_MarkerID<0) return false;

		return true;
	}
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		super.onSurfaceCreated(gl,config);
		
		/*
		// Load the texture for the square
		guitar.loadGLTexture(gl,this.context,R.drawable.android);
		
		gl.glEnable(GL10.GL_TEXTURE_2D);			//Enable Texture Mapping ( NEW )
		gl.glShadeModel(GL10.GL_SMOOTH); 			//Enable Smooth Shading
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f); 	//Black Background
		gl.glClearDepthf(1.0f); 					//Depth Buffer Setup
		gl.glEnable(GL10.GL_DEPTH_TEST); 			//Enables Depth Testing
		gl.glDepthFunc(GL10.GL_LEQUAL); 			//The Type Of Depth Testing To Do
		
		//Really Nice Perspective Calculations
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST); 
		*/
	}

	/**
	 * Override the draw function from ARRenderer.
	 */
	@Override
	public void draw(GL10 gl) {
		ARToolKit ar=ARToolKit.getInstance();
		long now=System.currentTimeMillis();
		
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		if(ar.queryMarkerVisible(drum1_MarkerID)){
			drum1LastSeen=now;
		}
		else{
			if(drum1LastSeen>0 && (now-drum1LastSeen)<1000){
				//((DrumsApplication)(DrumsApplication.getInstance())).playSound(DrumsApplication.drum1_SoundID,1.0f,1.0f);
				Log.d(TAG,  "detected -> playSound1");
				this.activity.playSound(0);
				drum1LastSeen=-1;
			}
		}
		
		if(ar.queryMarkerVisible(drum2_MarkerID)){
			drum2LastSeen=now;
		}
		else{
			if(drum2LastSeen>0 && (now-drum2LastSeen)<1000){
				//((DrumsApplication)(DrumsApplication.getInstance())).playSound(DrumsApplication.drum2_SoundID,1.0f,1.0f);
				Log.d(TAG,  "detected -> playSound2");
				this.activity.playSound(1);
				drum2LastSeen=-1;
			}
		}
		
		if(ar.queryMarkerVisible(drum3_MarkerID)){
			drum3LastSeen=now;
		}
		else{
			if(drum3LastSeen>0 && (now-drum3LastSeen)<1000){
				//((DrumsApplication)(DrumsApplication.getInstance())).playSound(DrumsApplication.drum3_SoundID,1.0f,1.0f);
				Log.d(TAG,  "detected -> playSound3");
				this.activity.playSound(2);
				drum3LastSeen=-1;
			}
		}
		
		if(getDebug()){
    		gl.glMatrixMode(GL10.GL_PROJECTION);
    		gl.glLoadMatrixf(ARToolKit.getInstance().getProjectionMatrix(), 0);
			gl.glRotatef(180,0.0f,0.0f,1.0f);
    		
    		gl.glEnable(GL10.GL_CULL_FACE);
            gl.glShadeModel(GL10.GL_SMOOTH);
            gl.glEnable(GL10.GL_DEPTH_TEST);        
        	gl.glFrontFace(GL10.GL_CW);
    		
    		gl.glMatrixMode(GL10.GL_MODELVIEW);
    		
    		if(ar.queryMarkerVisible(drum1_MarkerID)){
    			float mDrum1[]=ARToolKit.getInstance().queryMarkerTransformation(drum1_MarkerID);
    			if(mDrum1!=null){
    				gl.glLoadMatrixf(mDrum1, 0);
    				//drum1Button.draw(gl);
    				cube.draw(gl);
    			}
    		}
    		if(ar.queryMarkerVisible(drum2_MarkerID)){
    			float mDrum2[]=ARToolKit.getInstance().queryMarkerTransformation(drum2_MarkerID);
    			if(mDrum2!=null){
    				System.out.println("B");
    				gl.glLoadMatrixf(mDrum2, 0);
    				//gl.glScalef(1.0f, -1.0f, 1.0f);
    				//drum2Button.draw(gl);
    				cube.draw(gl);
    			}
    		}
    		if(ar.queryMarkerVisible(drum3_MarkerID)){
    			float mDrum3[]=ARToolKit.getInstance().queryMarkerTransformation(drum3_MarkerID);
    			if(mDrum3!=null){
    				System.out.println("C");
    				gl.glLoadMatrixf(mDrum3, 0);
    				//gl.glScalef(1.0f, 1.0f, 1.0f);
    				//drum3Button.draw(gl);
    				cube.draw(gl);
    			}
    		}
    	}
	}
	
	float [] getPosition(int markerID) {
		float result[]=new float[] {0,0,0,0};
		
		if (ARToolKit.getInstance().queryMarkerVisible(markerID)) {
			float m[]=ARToolKit.getInstance().queryMarkerTransformation(markerID);
			Matrix.multiplyMV(result,0,m,0,new float[]{0,0,0,1},0);
		}

		return(result);
	}
	
	float getDistance(float d[]){
		return((float)Math.sqrt((d[0]*d[0])+(d[1]*d[1])+(d[2]*d[2])));
	}
	
	boolean getDebug(){
		return true;
	}
}