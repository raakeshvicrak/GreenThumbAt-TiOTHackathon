package com.greenthumb.greenthumb.PolygonsUtil;

/**
 * Created by raakesh-3083 on 28/01/16.
 */
/* $Id$ */
public class Line {

    private Point start,end;
    private float slope = Float.NaN, b = Float.NaN;
    private boolean isvertical=false;

    public Line(Point start, Point end) {

        this.start = start;
        this.end = end;

        /*
         * code to calculate if the given line formed from the given set of points is vertical or not.
         */

        if(end.x - start.x !=0){
            /*
             * code to find out the slope of the line and
             * the constant 'b' in b=mx+b. hence b=b-mx;
             */

            slope = ((end.y-start.y)/(end.x-start.x));
            b = start.y - (slope * start.x);
        }
        else{
            isvertical=true;
        }

    }

    /*
     * Code to check if the given point lies inside a given line.
     */

    public boolean isInside(Point point){
        float maxX = (start.x>end.x)?start.x:end.x;
        float minX = (start.x<end.x)?start.x:end.x;
        float maxy = (start.y>end.y)?start.y:end.y;
        float miny = (start.y<end.y)?start.y:end.y;

        if((point.x>= minX && point.x<= maxX) && (point.y>= miny && point.y<= maxy)){
            return true;
        }
        return false;
    }

    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }

    public float getSlope() {
        return slope;
    }

    public float getB() {
        return b;
    }

    public boolean isvertical() {
        return isvertical;
    }

    public void setIsvertical(boolean isvertical) {
        this.isvertical = isvertical;
    }

    @Override
    public String toString(){
        return String.format("%s-%s",start.toString(),end.toString()); //NO i18n
    }
}
