package com.greenthumb.greenthumb.LayoutMetrics;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * Created by raakeshpremkumar on 4/22/17.
 */

public class LayoutMetricsPojo {

    private int width=0,height=0,originalwidth=0;
    private boolean width_greaterthanhalf=false;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public int getOriginalwidth() {
        return originalwidth;
    }

    public void setOriginalwidth(int originalwidth) {
        this.originalwidth = originalwidth;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    //temp function remove or modify after standardized.
    public void display_metrics_calculator_temp(Activity context){
        DisplayMetrics metrics=new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        setWidth(metrics.widthPixels);

        setHeight(metrics.heightPixels);

    }

    public void display_metrics_calculator(Activity context){
        DisplayMetrics metrics=new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        setWidth(calculateWidth(metrics, 1024));

        setHeight(calculateHeight(metrics,768));

        setOriginalwidth(metrics.widthPixels);
    }

    private int calculateHeight(DisplayMetrics metrics,int standardHeight){
        if(metrics.heightPixels%standardHeight==0) {
            return metrics.heightPixels;
        }
        else if(width_greaterthanhalf){
            int difference = standardHeight-(metrics.heightPixels%standardHeight);
            return (metrics.heightPixels+difference);
        }
        else if((metrics.heightPixels%standardHeight)>0 && (metrics.heightPixels%standardHeight)<=(standardHeight/2)){
            return metrics.heightPixels-(metrics.heightPixels % standardHeight);
        }
        else if((metrics.heightPixels%standardHeight)>0 && (metrics.heightPixels%standardHeight)>(standardHeight/2)){
            int difference = standardHeight-(metrics.heightPixels%standardHeight);
            return (metrics.heightPixels+difference);
        }
        else{
            return metrics.heightPixels;
        }

    }

    private int calculateWidth(DisplayMetrics metrics, int standardWidth){
        if(metrics.widthPixels%standardWidth==0) {
            return metrics.widthPixels;
        }
        else if((metrics.widthPixels%standardWidth)>0 && (metrics.widthPixels%standardWidth)<=(standardWidth/2)){
            return metrics.widthPixels-(metrics.widthPixels % standardWidth);
        }
        else if((metrics.widthPixels%standardWidth)>0 && (metrics.widthPixels%standardWidth)>(standardWidth/2)){
            width_greaterthanhalf=true;
            int difference = standardWidth-(metrics.widthPixels%standardWidth);
            return (metrics.widthPixels+difference);
        }
        else{
            return metrics.widthPixels;
        }
    }
}

