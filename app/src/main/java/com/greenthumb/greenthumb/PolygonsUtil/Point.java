package com.greenthumb.greenthumb.PolygonsUtil;
/* $Id$ */
/**
 * Created by raakesh-3083 on 28/01/16.
 */
public class Point {

    public float x,y;

    public Point(float x,float y){
        this.x=x;
        this.y=y;
    }

    /*
     * Code to override the toString Method to get the Points in the format I need.
     */
    @Override
    public String toString(){
        return String.format("(%.2f,%.2f)", x, y); // No I18N
    }
}
