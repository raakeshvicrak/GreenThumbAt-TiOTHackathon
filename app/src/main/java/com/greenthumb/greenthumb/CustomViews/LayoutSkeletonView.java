package com.greenthumb.greenthumb.CustomViews;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.greenthumb.greenthumb.LayoutMetrics.LayoutMetricsPojo;

import java.util.ArrayList;

/**
 * Created by raakeshpremkumar on 4/22/17.
 */

public class LayoutSkeletonView extends View {

    private String[] layoutPoints;
    private int layoutheight=0,layoutwidth=0;
    private Activity activity;
    private Paint painta;
    private String[] differentLayoutPaths;
    private ArrayList<String[]> layoutPoints_List=new ArrayList<String[]>();
    private ArrayList<Path> path_list=new ArrayList<Path>();
    private boolean isMainLayout;

    public LayoutSkeletonView(Context context) {
        super(context);
        init(context);
    }

    public LayoutSkeletonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LayoutSkeletonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        this.activity=(Activity)context;
        setPaintProperties(3);
    }

    public void setLayoutPoints(String layoutPoints, LayoutMetricsPojo metrics_object, boolean isMainLayout, int layout_width_individual, int layout_height_individual){
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
                            temp[0]=String.valueOf(Float.parseFloat(temp[0])*width_ratio);
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
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

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

    public void setPaintProperties(int strokewidth){
        painta = new Paint();
        painta.setStrokeWidth(strokewidth);
        painta.setStyle(Paint.Style.STROKE);
        painta.setColor(Color.BLACK);
        painta.setPathEffect(new DashPathEffect(new float[]{20, 20}, 0));
        invalidate();
    }
}

