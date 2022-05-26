package com.company;


import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.TreeSet;

public class PointSET {
    private TreeSet<Point2D> set;

    public PointSET(){
        this.set = new TreeSet<>();
    }

    public boolean isEmpty(){
        return this.set.isEmpty();
    }

    public int size(){
        return this.set.size();
    }

    public void insert(Point2D p){
        if(!contains(p)){this.set.add(p);}
    }
    public boolean contains(Point2D p){
        return this.set.contains(p);
    }
    // draw all points to standard draw
    public void draw(){
        StdDraw.setPenRadius(0.02);
        StdDraw.setPenColor(StdDraw.BLUE);
        for(Point2D point : set){
            StdDraw.point(point.x(), point.y());
        }
        StdDraw.show();
    }
     //all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect){
        Point2D pointMin = new Point2D(rect.xmin(), rect.ymin());
        Point2D pointMax = new Point2D(rect.xmax(), rect.ymax());
        return this.set.subSet(pointMin, true, pointMax, true);
    }
    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p){
        Point2D pointHigher = this.set.higher(p);
        Point2D pointLower = this.set.lower(p);
        if(p.distanceTo(pointHigher) > p.distanceTo(pointLower)){
            return pointLower;
        }
        return pointHigher;
    }

    public static void main(String[] args) {
        PointSET pointset = new PointSET();
        try {
            File myObj = new File("points.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] point = data.split(" ");
                pointset.insert(new Point2D(Double.parseDouble(point[0]), Double.parseDouble(point[1])));
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        System.out.println(pointset.nearest(new Point2D(0.456733, 0.766733)).toString());
    }
}
