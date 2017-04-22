package com.greenthumb.greenthumb.CustomViews;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.greenthumb.greenthumb.LayoutMetrics.LayoutMetricsPojo;

import java.util.ArrayList;

/**
 * Created by raakeshpremkumar on 4/22/17.
 */

public class LayoutMaskingView extends FrameLayout {

    /*
     * My PorterDuff modes
     */
    private static final int MODE_ADD = 0;
    private static final int MODE_CLEAR = 1;
    private static final int MODE_DARKEN = 2;
    private static final int MODE_DST = 3;
    private static final int MODE_DST_ATOP = 4;
    private static final int MODE_DST_IN = 5;
    private static final int MODE_DST_OUT = 6;
    private static final int MODE_DST_OVER = 7;
    private static final int MODE_LIGHTEN = 8;
    private static final int MODE_MULTIPLY = 9;
    private static final int MODE_OVERLAY = 10;
    private static final int MODE_SCREEN = 11;
    private static final int MODE_SRC = 12;
    private static final int MODE_SRC_ATOP = 13;
    private static final int MODE_SRC_IN = 14;
    private static final int MODE_SRC_OUT = 15;
    private static final int MODE_SRC_OVER = 16;
    private static final int MODE_XOR = 17;

    private Activity activity;
    private Handler handler;
    private Paint paint,painta;
    private PorterDuffXfermode porterDuffXfermode=null;
    private Drawable drawableMask;
    private LayoutMetricsPojo metrics_object;
    // private Bitmap finalMaskBitmap=null;
    private String[] layoutPoints;
    private ArrayList<Path> path_list=new ArrayList<Path>();
    private ArrayList<String[]> layoutPoints_List=new ArrayList<String[]>();
    private String[] differentLayoutPaths;
    private int layoutheight=0,layoutwidth=0;
    private boolean isMainLayout;
    private Bitmap bmp;



    public LayoutMaskingView(Context context) {
        super(context);
    }

    public LayoutMaskingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LayoutMaskingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public LayoutMaskingView(Context context,String layoutPoints,LayoutMetricsPojo metrics_object, boolean isMainLayout, int layout_width_individual, int layout_height_individual){
        super(context);
        this.activity = (Activity) context;
        displaymetrics_calculator();
        setPaintProperties(6);
        setLayoutPoints(context, layoutPoints, metrics_object, isMainLayout, layout_width_individual, layout_height_individual);
    }

    public void setLayoutPoints(Context context, String layoutPoints,LayoutMetricsPojo metrics_object, boolean isMainLayout, int layout_width_individual, int layout_height_individual){
        this.layoutheight=layout_height_individual;
        this.layoutwidth=layout_width_individual;
        this.isMainLayout=isMainLayout;

        /*
        * Code to calculate the ratio of the current resolution to that of the standard resolution.
        */
        double height_ratio=layoutheight/768.0f;
        double width_ratio=layoutwidth/1024.0f;

        if(layoutPoints.contains("#")){ //NO I18N
            differentLayoutPaths=layoutPoints.split("#"); //NO I18N
        }
        else{
            differentLayoutPaths=new String[1];
            differentLayoutPaths[0]=layoutPoints;
        }

        /*
        * Code to convert the standard 1024*768 resolution points to needed resolution.
        */
        for (int j=0;j<differentLayoutPaths.length;j++){
            this.layoutPoints=differentLayoutPaths[j].split(";"); //NO I18N

            for (int i=0;i<this.layoutPoints.length;i++){
                String[] temp=this.layoutPoints[i].split(","); //NO I18N
                if(temp.length==2){
                    if(!temp[0].equals("1")){ //NO I18N
                        if(isMainLayout==false){
                            temp[0]=String.valueOf(Float.parseFloat(temp[0])*width_ratio-1);
                        }
                    }
                    if(!temp[1].equals("1")){ //NO I18N
                        temp[1]=String.valueOf(Float.parseFloat(temp[1])*height_ratio);
                    }
                    if(isMainLayout==true){
                        temp[0]=String.valueOf((Float.parseFloat(temp[0])*width_ratio)+((metrics_object.getOriginalwidth()-metrics_object.getWidth())/2.0));
                    }
                }
                String temp1=temp[0]+","+temp[1]; //NO I18N
                this.layoutPoints[i]=temp1;
            }
            layoutPoints_List.add(this.layoutPoints);
        }

        /*
        * Code for forming path which is used in onDraw();
        */
        for (int j=0;j<layoutPoints_List.size();j++) {
            this.layoutPoints = (String[]) layoutPoints_List.get(j);
            Pt[] myPath = new Pt[this.layoutPoints.length + 1];

            for (int i = 0; i < this.layoutPoints.length; i++) {
                String temp[] = this.layoutPoints[i].split(","); //NO I18N
                myPath[i] = new Pt(Float.parseFloat(temp[0]), Float.parseFloat(temp[1]));
                if (i == this.layoutPoints.length - 1) {
                    temp = this.layoutPoints[0].split(","); //NO I18N
                    myPath[myPath.length - 1] = new Pt(Float.parseFloat(temp[0]), Float.parseFloat(temp[1]));
                }
            }

            Path path = new Path();
            path.moveTo(myPath[0].x, myPath[0].y);
            for (int i = 1; i < myPath.length; i++) {
                path.lineTo(myPath[i].x, myPath[i].y);
            }
            path_list.add(path);
        }
        init(context);
    }

    private void init(Context context){
        handler =new Handler();

        setDrawingCacheEnabled(true);

        if (Build.VERSION.SDK_INT >= 11) {
            setLayerType(LAYER_TYPE_SOFTWARE, null); //Only works for software layers
        }

        paint=definePaint();

        Resources.Theme theme = context.getTheme();
        if (theme != null) {
            try {
                //Load the mask if specified in xml
                initMask(drawMask());

                //Load the mode if specified in xml
                porterDuffXfermode = getPorterDuffModeFromInteger(MODE_DST_IN);

                initMask(drawableMask);
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        registerMeasure();
    }

    /*
    * Function to calculate the display metrics of the specific device.
    */
    private void displaymetrics_calculator(){
        LayoutMetricsPojo metrics_object=new LayoutMetricsPojo();
        metrics_object.display_metrics_calculator(activity);
        this.metrics_object=metrics_object;
    }

    private void initMask(Drawable input) {
        if (input != null) {
            drawableMask = input;
            if (drawableMask instanceof AnimationDrawable) {
                drawableMask.setCallback(this);
            }
        }
    }

    public Drawable getDrawableMask() {
        return drawableMask;
    }

    public void setMask(int drawableRes) {
        Resources res = getResources();
        if (res != null) {
            setMask(res.getDrawable(drawableRes));
        }
    }

    public void setMask(Drawable input) {
        initMask(input);
        swapBitmapMask(makeBitmapMask(drawableMask));
        invalidate();
    }

    /*
     * Code to change the size on resize.
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setSize(w, h);
    }

    private void setSize(int width, int height) {
        if (width > 0 && height > 0) {
            if (drawableMask != null) {
                swapBitmapMask(makeBitmapMask(drawableMask));
            }
        }
    }

    /*
     * Code to draw the canvas on the layout.
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (bmp != null && paint != null) {
            paint.setXfermode(porterDuffXfermode);

            canvas.drawBitmap(bmp, 0.0f, 0.0f, paint);
            paint.setXfermode(null);
        }
    }

    private Bitmap makeBitmapMask( Drawable drawable) {
        if (drawable != null) {
            if (getMeasuredWidth() > 0 && getMeasuredHeight() > 0) {
                Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
                Canvas canvas = new Canvas(bmp);
                if(this.layoutPoints!=null && this.layoutPoints.length>0){
                    Paint paint = new Paint();
                    paint.setStrokeWidth(3);
                    paint.setStyle(Paint.Style.FILL);
                    paint.setColor(Color.TRANSPARENT);

                    DisplayMetrics metrics = new DisplayMetrics();
                    activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

                    for (int i=0;i<path_list.size();i++){
                        canvas.drawPath(path_list.get(i), paint);
                        canvas.drawPath(path_list.get(i), painta);
                    }
                }

                drawable.draw(canvas);
                return bmp;
            } else {
                return null;
            }
        }
        return null;
    }

    /*
     * Code to draw & mask on the FrameLayout.
     */
    private Drawable drawMask(){
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        bmp = Bitmap.createBitmap(metrics_object.getOriginalwidth(), metrics_object.getHeight(), conf);

        Canvas canvas = new Canvas(bmp);

        if(this.layoutPoints!=null && this.layoutPoints.length>0){

            Paint paint = new Paint();
            paint.setStrokeWidth(3);
            paint.setStyle(Paint.Style.FILL);
            //paint.setColor(Color.TRANSPARENT);

            DisplayMetrics metrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            for (int i=0;i<path_list.size();i++){
                canvas.drawPath(path_list.get(i), paint);
                canvas.drawPath(path_list.get(i), painta);
            }
        }

        BitmapDrawable drawable = new BitmapDrawable(getResources(), bmp);
        return drawable;
    }

    public void setPaintProperties(int strokewidth){
        painta = new Paint();
        painta.setStrokeWidth(strokewidth);
        painta.setStyle(Paint.Style.STROKE);
        //painta.setColor(Color.TRANSPARENT);
        painta.setPathEffect(new DashPathEffect(new float[]{20, 20}, 0));
    }

    /*
     * The POJO class for storing the x-y co-ordinate pairs.
     */
    class Pt {
        float x, y;
        Pt(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    /*
     * Code to Define the properties of a paint object.
     */
    private Paint definePaint(){
        Paint tempPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        tempPaint.setXfermode(porterDuffXfermode);
        return tempPaint;
    }

    private void registerMeasure() {
        final ViewTreeObserver treeObserver = LayoutMaskingView.this.getViewTreeObserver();
        if (treeObserver != null && treeObserver.isAlive()) {
            treeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    ViewTreeObserver aliveObserver = treeObserver;
                    if (!aliveObserver.isAlive()) {
                        aliveObserver = LayoutMaskingView.this.getViewTreeObserver();
                    }
                    if (aliveObserver != null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            aliveObserver.removeOnGlobalLayoutListener(this);
                        } else {
                            aliveObserver.removeGlobalOnLayoutListener(this);
                        }
                    }
                    swapBitmapMask(makeBitmapMask(drawableMask));
                }
            });
        }
    }

    @Override
    public void invalidateDrawable(Drawable dr) {
        if (dr != null) {
            initMask(dr);
            swapBitmapMask(makeBitmapMask(dr));
            invalidate();
        }
    }

    @Override
    public void scheduleDrawable(Drawable who, Runnable what, long when) {
        if (who != null && what != null) {
            handler.postAtTime(what, when);
        }
    }

    @Override
    public void unscheduleDrawable(Drawable who, Runnable what) {
        if (who != null && what != null) {
            handler.removeCallbacks(what);
        }
    }

    private void swapBitmapMask(Bitmap newMask) {
        /*if (newMask != null) {
           *//* if (finalMaskBitmap != null && !finalMaskBitmap.isRecycled()) {
                finalMaskBitmap.recycle();
            }*//*
            //Bitmap b = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
           // finalMaskBitmap = newMask;
        }*/

        Log.d("swapping","true"); // NO I18N
    }

    /*
    * Code to convert the int passed as argument to the corresponding PorterDuff Modes.
    */
    private PorterDuffXfermode getPorterDuffModeFromInteger(int index) {
        PorterDuff.Mode mode = null;
        switch (index) {
            case MODE_ADD:
                if (Build.VERSION.SDK_INT >= 11) {
                    mode = PorterDuff.Mode.ADD;
                }
            case MODE_CLEAR:
                mode = PorterDuff.Mode.CLEAR;
                break;
            case MODE_DARKEN:
                mode = PorterDuff.Mode.DARKEN;
                break;
            case MODE_DST:
                mode = PorterDuff.Mode.DST;
                break;
            case MODE_DST_ATOP:
                mode = PorterDuff.Mode.DST_ATOP;
                break;
            case MODE_DST_IN:
                mode = PorterDuff.Mode.DST_IN;
                break;
            case MODE_DST_OUT:
                mode = PorterDuff.Mode.DST_OUT;
                break;
            case MODE_DST_OVER:
                mode = PorterDuff.Mode.DST_OVER;
                break;
            case MODE_LIGHTEN:
                mode = PorterDuff.Mode.LIGHTEN;
                break;
            case MODE_MULTIPLY:
                mode = PorterDuff.Mode.MULTIPLY;
                break;
            case MODE_OVERLAY:
                if (Build.VERSION.SDK_INT >= 11) {
                    mode = PorterDuff.Mode.OVERLAY;
                }
            case MODE_SCREEN:
                mode = PorterDuff.Mode.SCREEN;
                break;
            case MODE_SRC:
                mode = PorterDuff.Mode.SRC;
                break;
            case MODE_SRC_ATOP:
                mode = PorterDuff.Mode.SRC_ATOP;
                break;
            case MODE_SRC_IN:
                mode = PorterDuff.Mode.SRC_IN;
                break;
            case MODE_SRC_OUT:
                mode = PorterDuff.Mode.SRC_OUT;
                break;
            case MODE_SRC_OVER:
                mode = PorterDuff.Mode.SRC_OVER;
                break;
            case MODE_XOR:
                mode = PorterDuff.Mode.XOR;
                break;
            default:
                mode = PorterDuff.Mode.DST_IN;
        }
        return new PorterDuffXfermode(mode);
    }
}
