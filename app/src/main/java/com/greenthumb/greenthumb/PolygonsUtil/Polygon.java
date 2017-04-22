/* $Id$ */
package com.greenthumb.greenthumb.PolygonsUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by raakesh-3083 on 28/01/16.
 */
public class Polygon  {

    private final BoundingBox boundingbox;
    private final List<Line> sides;

    public Polygon(BoundingBox boundingbox, List<Line> sides) {
        this.boundingbox = boundingbox;
        this.sides = sides;
    }

    public static Builder builder(){
        return new Builder();
    }

    /*
     * Class to build the Polygon from the given points.
     */
    public static class Builder{
        private List<Line> sides=new ArrayList<Line>();
        private List<Point> vertexes=new ArrayList<Point>();
        private BoundingBox boundingbox=null;

        private boolean firstPoint=true;
        private boolean isClosed=false;

        public Builder addVertex(Point point){
            if(isClosed){
                vertexes= new ArrayList<Point>();
                isClosed=false;
            }

            updateBoundingBox(point);
            vertexes.add(point);

            /*
             * Code to form each side of a polygon as we get the points.
             */
            if(vertexes.size()>1){
                Line line= new Line(vertexes.get(vertexes.size()-2),point);
                sides.add(line);
            }

            return this;
        }

        /*
         * Code to call when all the vertexes are added and polygon is to formed.
         */
        public Builder close(){
            validate();

            /*
             * Code to form the last line when all vertexes have been given.
             */
            Line line= new Line(vertexes.get(vertexes.size()-1),vertexes.get(0));
            sides.add(line);

            isClosed=true;

            return this;
        }

        /*
         * Code to build the polygon.
         */
        public Polygon build(){
            validate();

            if(!isClosed){
                Line line= new Line(vertexes.get(vertexes.size()-1),vertexes.get(0));
                sides.add(line);
            }

            Polygon polygon=new Polygon(boundingbox, sides);
            return polygon;
        }

        /*
         * Code to update the Bounding box (i,e;) the polygon.
         */
        private void updateBoundingBox(Point point){
            if(firstPoint){
                boundingbox= new BoundingBox();
                boundingbox.xMax= point.x;
                boundingbox.xMin= point.x;
                boundingbox.yMax= point.y;
                boundingbox.yMin= point.y;

                firstPoint=false;
            }
            else{
                if(point.x > boundingbox.xMax){
                    boundingbox.xMax= point.x;
                }
                else if(point.x<boundingbox.xMin){
                    boundingbox.xMin= point.x;
                }
                else if(point.y> boundingbox.yMax){
                    boundingbox.yMax= point.y;
                }
                else if(point.y< boundingbox.yMin){
                    boundingbox.yMin= point.y;
                }
            }
        }

        /*
         * Code to validate if the given vertexes form a polygon.
         */
        private void validate(){
            if(vertexes.size()<3){
                throw new RuntimeException("Polygon must have atleast 3 points"); //NO i18N
            }
        }
    }

    /*
     * Code to check if the given point(point of touch by user) lies inside the polygon.
     */
    public boolean contains(Point point){
        if(inBoundingbox(point)){
            Line checkingLine=createCheckingLine(point);
            int nooftimesIntersected=0;

            for(Line side:sides){
                if(intersect(side,checkingLine)){
                    nooftimesIntersected++;
                }
            }

            /*
             * If the number of interactions is odd then the point lies inside the polygon else it lies outside.
             */
            if(nooftimesIntersected%2==1){
                return true;
            }
        }
        return false;
    }

    public boolean intersect(Line side,Line checkingLine){
        Point intersectionPoint=null;

        if(!side.isvertical() && !checkingLine.isvertical()){

            /*
             * If slope of both the lines are same then it means the lines are parallel,
             * which means no intersection point.
             */
            if(side.getSlope() == checkingLine.getSlope()){
                return false;
            }

            /*
             * Code to find the x- coordinate value for given two lines y1 = m1*x+b1 and y2 = m2*x+b2
             * Hence, x = (b2-b1)/(m2-m1)
             */
            float x_coordinate_value=((side.getB()-checkingLine.getB())/(checkingLine.getSlope()-side.getSlope()));
            float y_coordinate_value=side.getSlope()*x_coordinate_value+side.getB();
            intersectionPoint= new Point(x_coordinate_value,y_coordinate_value);
        }

        else if(checkingLine.isvertical() && !side.isvertical()){
            float x_coordinate_value=checkingLine.getStart().x;
            float y_coordinate_value=side.getSlope()*x_coordinate_value+side.getB();
            intersectionPoint= new Point(x_coordinate_value,y_coordinate_value);
        }

        else if(!checkingLine.isvertical() && side.isvertical()){
            float x_coordinate_value= side.getStart().x;
            float y_coordinate_value= checkingLine.getSlope()*x_coordinate_value+checkingLine.getB();
            intersectionPoint= new Point(x_coordinate_value,y_coordinate_value);
        }

        else{
            return false;
        }

        if(side.isInside(intersectionPoint) && checkingLine.isInside(intersectionPoint)){
            return true;
        }

        return false;
    }

    /*
     * Code to create the intersecting line with assuming one point outside the polygon.
     */
    private Line createCheckingLine(Point point){
        /*
         * Code to find out the difference between the minimum and maximum co-ordinates which will be
         * subtracted to minimum of X to get the outside point.
         */
        float outsidepointXcoordinatedifference= (boundingbox.xMax- boundingbox.xMin)/100f;
        Point outsidePoint= new Point(boundingbox.xMin-outsidepointXcoordinatedifference,boundingbox.yMin);

        /*
         * code to find the intersecting line between the point of selection and one point assumed outside the polygon.
         */
        Line intersectingline=new Line(outsidePoint, point);
        return intersectingline;
    }

    private boolean inBoundingbox(Point point){
        if(point.x< boundingbox.xMin || point.x> boundingbox.xMax || point.y< boundingbox.yMin || point.y> boundingbox.yMax){
            return false;
        }
        return true;
    }

    private static class BoundingBox{
        public float xMax= Float.NEGATIVE_INFINITY;
        public float xMin= Float.NEGATIVE_INFINITY;
        public float yMax= Float.NEGATIVE_INFINITY;
        public float yMin= Float.NEGATIVE_INFINITY;
    }

}
