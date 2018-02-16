import java.awt.*;
import java.awt.List;
import java.awt.color.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.*;
import java.lang.*;
import java.lang.Integer.*;

public class ThreeDimensionalSpace extends JPanel implements ActionListener, KeyListener, MouseMotionListener, MouseListener{
  
  public static int screenX = 700;
  public static int screenY = 700;
  
  public Plot p1;
  public Cube[] cube;

  public static final double PI = 3.141592;
  
  public static double theta = PI/6;
  public static Point origin; 
  double screenC = screenX/theta;
  
  public Point[] points;
  public int[][] faces;
  
  public int pointCount;
  int cubeCount = 1;
  int cubeSide = 10;

  private static double tanGamma, cosGamma, sinGamma,
                 cosPhi, sinPhi; 
  
  Cube[] cubes;
  int[][] cubeX, cubeY;
  public int[][][] cubeSides;
  public Point[][] cubePoints;

  public int[] xCoor;
  public int[] yCoor;  

  public boolean PLOT;

  public Point[] p;
 
  public ThreeDimensionalSpace(){
    super();
    p = new Point[6];
    p[0] = new Point(0,0,10);
    p[1] = new Point(0,0,-10);
    p[2] = new Point(0,10,0); 
    p[3] = new Point(0,-10,0);
    p[4] = new Point(10,0,0);
    p[5] = new Point(-10,0,0);
    origin = new Point(0,0,0);
    PLOT = false;
    cubes = new Cube[cubeCount];
    cubePoints = new Point[cubeCount][8];
    cubeSides = new int[cubeCount][6][4];
    cubeX = new int[cubeCount][8];
    cubeY = new int[cubeCount][8];
    int x = 0;
    int y = 0;
    for(int i = 0; i < cubeCount; i++){
      cubes[i] = new Cube(x,y,0,cubeSide);
      cubeSides[i] = cubes[i].getSides();
      cubePoints[i] = cubes[i].getPoints();
      x+=cubeSide;
      y+=cubeSide;
    }
    redoTrig();
  }
 
  public ThreeDimensionalSpace(String func, char var1, char var2){
    this();
  
    PLOT = true;
    p1 = new Plot(func,var1,var2,-10, 10,-10,10, 2);

    points = p1.get3DPoints();
    faces = p1.getSurfaceCorners();
    pointCount = p1.getPoints();
    xCoor = new int[pointCount];
    yCoor = new int[pointCount];
  }

  public void revaluate(){
   
    origin.calcScreenPoints(); 
    for(int i = 0; i < 6; i++){
      p[i].calcScreenPoints();
    }

    for(int i = 0; i < cubeCount; i++){
      for(int j = 0; j < 8; j++){
        cubePoints[i][j].calcScreenPoints();
        cubeX[i][j] = cubePoints[i][j].getScreenX();
        cubeY[i][j] = cubePoints[i][j].getScreenY();
      }   
    }
 
    for(int i = 0; i < pointCount; i++){
      points[i].calcScreenPoints();
      xCoor[i] = points[i].getScreenX();
      yCoor[i] = points[i].getScreenY();
    }
   
    tag("\nphi: " + phi);
    tag("\ngamma: " + gamma);
    tag("X: " + uX + " | Y: " + uY + " | Z: " + uZ );
  }
  
  // Method adds the  in the list to arrays
  // useful in the future if I want to allow the 
  // user to add new coordinates or shapes
  
  public static double uX = 0;
  public static double uY = 0;
  public static double uZ = 0;
  
  // the angle in the XY plane
  public double phi = 0;
  // the angle of tilt upwards
  public double gamma = 0;
  
  
  public double arcS(double x, double y, double r){
    if(x>0 && y < 0){ 
      return 3*PI / 2 + Math.asin(y/r);
    }
    if(x>0 && y > 0){
      return PI -  Math.asin(y/r);
    } else{
      return  Math.asin(y/r);
    } 
  }
  public double arcT(double x, double y, double r){
    
    if(x < 0) 
      return +Math.atan(y/x);
    else
      return Math.atan(y/x);
  }
  
  
  public void paintComponent(Graphics g){
    super.paintComponent(g);
    
    revaluate();
    
    g.setColor(Color.RED);
   
    for(int i = 0; i < 5; i+=2){
      g.drawLine(p[i].getScreenX(),p[i].getScreenY(),
                 p[i+1].getScreenX(),p[i+1].getScreenY());
    }

    g.setColor(Color.BLUE);

    g.drawOval(origin.getScreenX(),origin.getScreenY(),4,4);

    if(!PLOT){
    for( int i = 0; i < cubes.length; i++){
      for( int s = 0; s < 6; s++){
     	g.drawLine(cubeX[i][cubeSides[i][s][0]],
                   cubeY[i][cubeSides[i][s][0]], 
                   cubeX[i][cubeSides[i][s][1]], 
                   cubeY[i][cubeSides[i][s][1]]); 
        g.drawLine(cubeX[i][cubeSides[i][s][1]],
                   cubeY[i][cubeSides[i][s][1]], 
                   cubeX[i][cubeSides[i][s][2]], 
                   cubeY[i][cubeSides[i][s][2]]);
        g.drawLine(cubeX[i][cubeSides[i][s][2]],
                   cubeY[i][cubeSides[i][s][2]], 
                   cubeX[i][cubeSides[i][s][3]], 
                   cubeY[i][cubeSides[i][s][3]]); 
       	g.drawLine(cubeX[i][cubeSides[i][s][3]],
                   cubeY[i][cubeSides[i][s][3]], 
                   cubeX[i][cubeSides[i][s][0]], 
                   cubeY[i][cubeSides[i][s][0]]); 
      }
    }}
   
    if(PLOT){ 
      for(int i = 0; i < faces.length; i++){
        int[] xPoints = {xCoor[faces[i][0]],xCoor[faces[i][1]],
                           xCoor[faces[i][2]]};
        int[] yPoints = {yCoor[faces[i][0]],yCoor[faces[i][1]],
                           yCoor[faces[i][2]]};
        g.setColor(Color.BLACK);
        //g.fillPolygon(xPoints,xPoints,3);
        g.drawLine(xCoor[faces[i][0]],yCoor[faces[i][0]],
                   xCoor[faces[i][1]],yCoor[faces[i][1]]);
        g.drawLine(xCoor[faces[i][0]],yCoor[faces[i][0]],
                   xCoor[faces[i][2]],yCoor[faces[i][2]]);
        g.drawLine(xCoor[faces[i][2]],yCoor[faces[i][2]],
                   xCoor[faces[i][1]],yCoor[faces[i][1]]);
        g.setColor(Color.RED);
      }
    } 
  }

  public void keyPressed(KeyEvent e) {
    if(e.getKeyCode() == e.VK_SPACE){
      uX +=1*cosPhi*cosGamma;
      uY +=1*sinPhi*cosGamma;
      uZ +=1*sinGamma;
      repaint();
    }
    if(e.getKeyCode() == e.VK_UP){
      if(gamma-0.1>-PI/2){
        gamma-=.1;
        redoTrig();
        repaint();
      }
    }
    if(e.getKeyCode() == e.VK_DOWN){
      //uX -= 2*cosPhi;
      //uY -= 2*sinPhi;
      
      if(gamma+0.1<PI/2){
        gamma+=0.1;
        redoTrig();
        repaint();
      }
    }
    if(e.getKeyCode() == e.VK_RIGHT){
      phi -= .1;
      redoTrig();
      if( phi < 0){
        phi = 2*PI;
        phi -= .1;
      }  
      repaint();
    }
    if(e.getKeyCode() == e.VK_LEFT){
      phi += .1;
      redoTrig();
      if( phi >= 2*PI){
        phi = 0;
        phi += .1;
      }
      repaint();
    }
  }

  public void redoTrig(){
    cosPhi = Math.cos(phi);
    sinPhi = Math.sin(phi);
    cosGamma = Math.cos(gamma);
    sinGamma = Math.sin(gamma);
    tanGamma = Math.tan(gamma);
    double length = Math.sqrt(Math.pow(cosPhi,2)+Math.pow(sinPhi,2)+Math.pow(tanGamma,2));
    cosPhi /= length;  
    sinPhi /= length;  
    tanGamma /= length;  
  }  

  public void keyReleased(KeyEvent e) {}
  public void keyTyped(KeyEvent e) {}
  public void actionPerformed(ActionEvent e) {}
  public void mouseClicked(MouseEvent e) {}
  public void mouseEntered(MouseEvent e) {}
  public void mouseExited(MouseEvent e) {}
  public void mousePressed(MouseEvent e) {}
  public void mouseReleased(MouseEvent e) {}
  public void mouseDragged(MouseEvent e) {}
  public void mouseMoved(MouseEvent e) {}

  private void tag(Object out){ System.out.print(out); }
  private void tag(){ tag("\n"); }
  private void viewLine(){ tag("\n----------------------------\n"); }
  
  public static double getSinPhi(){ return sinPhi; }
  public static double getCosPhi(){ return cosPhi; }
  public static double getTanGamma(){ return tanGamma; }
  public static double[] getUser(){ 
    double[] result = {uX,uY,uZ};
    return result;
  }
}
