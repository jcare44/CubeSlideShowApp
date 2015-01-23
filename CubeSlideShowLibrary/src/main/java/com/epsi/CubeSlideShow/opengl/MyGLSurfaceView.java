/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.epsi.CubeSlideShow.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

/**
 * A view container where OpenGL ES graphics can be drawn on screen.
 * This view can also be used to capture touch events, such as a user
 * interacting with drawn objects.
 */
public class MyGLSurfaceView extends GLSurfaceView {

    private final MyGLRenderer mRenderer;
    private OnTouchListener onTouchListener;

    public MyGLSurfaceView(Context context) {
        super(context);

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);

        // Set the Renderer for drawing on the GLSurfaceView
        mRenderer = new MyGLRenderer(context);
        setRenderer(mRenderer);

        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float mPreviousX;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float x = e.getX();

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:

                float dx = (x - mPreviousX) / 5;

                mRenderer.setAngle(
                    mRenderer.getAngle() +
                        ((dx) * TOUCH_SCALE_FACTOR));  // = 180.0f / 320
                requestRender();
                Log.d("sdf Swipe angle", Float.toString(x));
                break;
            case MotionEvent.ACTION_DOWN:
                Log.d("sdf Swipe action down",Float.toString(x));
                break;
            case MotionEvent.ACTION_UP:
                Log.d("sdf Swipe action up",Float.toString(x));
                long time = e.getEventTime() - e.getDownTime();
                //Touch if time < 100ms
                if(time < 100) {
                    Log.d("sdf Swipe action up","theorical touch of " + time + "ms");
                    if(this.onTouchListener != null) {
                        this.onTouchListener.onTouch();
                    }
                } else {
                    mRenderer.comeBack();
                    requestRender();
                }
                break;
        }

        mPreviousX = x;
        return true;
    }

    interface OnTouchListener{
        public void onTouch();
    }

    /**
     * Listener to be attached to each row
     *
     * @param _listener
     */
    public void setOnTouch(OnTouchListener _listener){
        this.onTouchListener = _listener;
    }

}
