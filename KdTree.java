package com.company;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class KdTree {
    private Node root;
    class Node
    {
        Point2D point2D;
        Node left, right;

        public Node(Point2D point2D)
        {
            this.point2D = point2D;
            left = right = null;
        }

        public String toString(){
            return point2D.toString();
        }
    }

    public boolean contains(Point2D point2D){
        Node node = new Node(point2D);
        if(root == null){ return false;}
        if(root.point2D == node.point2D){return true;}
        if(node.point2D.x() > root.point2D.x()){
            return contains(node, root.right, true);
        }
        return contains(node, root.left, true);
    }

    public boolean contains(Node node, Node current, boolean even){
        if(current == null){return false;}
        if(current.point2D == node.point2D){return true;}

        if(even){
            if(node.point2D.y() > current.point2D.y()){
                return contains(node, current.right, false);
            }
            return contains(node, current.left, false);
        }
        if(node.point2D.x() > current.point2D.x()){
            return contains(node, current.right, true);
        }
        return contains(node, current.left, true);

    }

    public void insert(Point2D point2D){
        Node node = new Node(point2D);

        if(root == null){ root = node; return;}

        if(node.point2D.x() > root.point2D.x()){
            if(root.right != null){
                insertCompareY(node, root.right);return;
            }
            root.right = node; return;
        }
        if(root.left != null){
            insertCompareY(node, root.left);return;
        }
        root.left = node;
    }

    private void insertCompareX(Node node, Node current){
        if(node.point2D.x() > current.point2D.x()){
            if(current.right != null){
                insertCompareY(node, current.right); return;
            }
            current.right = node; return;
        }
        if(current.left != null){
            insertCompareY(node, current.left); return;
        }
        current.left = node;
    }

    private void insertCompareY(Node node, Node current){
        if(node.point2D.y() > current.point2D.y()){
            if(current.right != null){
                insertCompareX(node, current.right); return;
            }
            current.right = node; return;
        }
        if(current.left != null){
            insertCompareX(node, current.left); return;
        }
        current.left = node;
    }

    public Point2D nearest(Point2D p){
        if(root == null){
            return null;
        }

        Point2D firstChamp = nearest(p, root.point2D, root.right, true, p);
        return nearest(p, firstChamp, root.left, true, new Point2D(root.point2D.x(), p.y()));
    }

    private Point2D nearest(Point2D p, Point2D champ, Node current, boolean even, Point2D closestPossiblePoint){
        if(current == null){
            return champ;
        }
        if(p.distanceTo(champ) < p.distanceTo(closestPossiblePoint)){
            return champ;
        }
        double distanceCurrent = p.distanceTo(current.point2D);
        if(distanceCurrent < p.distanceTo(champ)){
            champ = current.point2D;
        }

        if(even){
            if(current.point2D.y() > p.y()){
                champ = nearest(p, champ, current.right, false, new Point2D(closestPossiblePoint.x(), current.point2D.y()));
                return nearest(p, champ, current.left, false, closestPossiblePoint);

            }
            champ = nearest(p, champ, current.left, false, new Point2D(closestPossiblePoint.x(), current.point2D.y()));
            return nearest(p, champ, current.right, false, closestPossiblePoint);

        }
        if(current.point2D.x() > p.x()){
            champ = nearest(p, champ, current.right, true, new Point2D(current.point2D.x(), closestPossiblePoint.y()));
            return nearest(p, champ, current.left, true, closestPossiblePoint);

        }
        champ = nearest(p, champ, current.left, true, new Point2D(current.point2D.x(), closestPossiblePoint.y()));
        return nearest(p, champ, current.right, true, closestPossiblePoint);
    }

    public Iterable<Point2D> range(RectHV rect){
        ArrayList<Point2D> points = new ArrayList<>();
        if(root == null){
            return points;
        }
        RectHV rectRight = new RectHV(root.point2D.x(), 0, 1, 1);
        RectHV rectLeft = new RectHV(0, 0, root.point2D.x(), 1);
        rectRight.draw();
        rectLeft.draw();
        points = range(rect, rectRight, root.right, true, points);
        return range(rect, rectLeft, root.left, true, points);
    }

    private ArrayList<Point2D> range(RectHV rectSearch, RectHV rectIn, Node current, boolean even, ArrayList<Point2D> points){
       if(current == null){
           return points;
       }
       if(!rectSearch.intersects(rectIn)){
           return points;
       }
       if(rectSearch.contains(current.point2D)){
           points.add(current.point2D);
       }
       if(even){
           RectHV rectUp = new RectHV(rectIn.xmin(), current.point2D.y(), rectIn.xmax(), rectIn.ymax());
           rectUp.draw();
           RectHV rectDown = new RectHV(rectIn.xmin(), rectIn.ymin(), rectIn.xmax(), current.point2D.y());
            rectDown.draw();
           points = range(rectSearch, rectUp, current.right, false, points);
           return range(rectSearch, rectDown, current.left, false, points);
       }
       else{
           RectHV rectRight = new RectHV(current.point2D.x(), rectIn.ymin(), rectIn.xmax(), rectIn.ymax());
           rectRight.draw();
           RectHV rectLeft = new RectHV(rectIn.xmin(), rectIn.ymin(), current.point2D.x(), rectIn.ymax());
           rectLeft.draw();
           points = range(rectSearch, rectRight, current.right, true, points);
           return range(rectSearch, rectLeft, current.left, true, points);
       }
    }


    public static void main(String[] args) {
        KdTree kdTree = new KdTree();
        try {
            File myObj = new File("points.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] point = data.split(" ");
                kdTree.insert(new Point2D(Double.parseDouble(point[0]), Double.parseDouble(point[1])));
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
//        System.out.println(kdTree.nearest(new Point2D(0.68393, 0.73929)).toString());
        for(Point2D point : kdTree.range(new RectHV(0.6, 0.6, 0.7, 0.7))){
            System.out.println(point.toString());
        }
    }
}
