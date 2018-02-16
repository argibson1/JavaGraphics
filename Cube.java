public class Cube{

  private Point center = new Point(0,0,0); 
  private Point[] points = new Point[8]; 
  private int side = 0;

  public Cube(int x, int y, int z, int side){
    center = new Point(x,y,z);
    this.side = side;
    initPoints();
  }

  public void initPoints(){
    double x = center.getX();
    double y = center.getY();
    double z = center.getZ();
    
    for(int i = 0; i < 8; i++){
      if(i == 0){
        x+=side/2;
        y+=side/2;
        z+=side/2;
        points[0] = new Point(x,y,z);
      
        System.out.println("{ " + x + " , " + y + " , " + z + " }");
        continue;
      }else if(i%2 == 0){
        x+=side;
      }else if(i%2 == 1){
        x-=side;
      } 
     
      if(i%4 == 0){
        y+=side;
        z-=side;
      } else if(i%2 == 0){
        y-=side;
      }

      System.out.println("{ " + x + " , " + y + " , " + z + " }");
      
      points[i] = new Point(x,y,z);
    } 
  }

  public Point[] getPoints(){
    return points;
  }

  public int[][] getSides(){
    int[][] sides = new int[6][4];
    
    // all same z value
    sides[0][0] = 0;
    sides[0][1] = 1;
    sides[0][2] = 3;
    sides[0][3] = 2;

    sides[1][0] = 4;
    sides[1][1] = 5;
    sides[1][2] = 7;
    sides[1][3] = 6;

    sides[2][0] = 1;
    sides[2][0] = 3;
    sides[2][0] = 7;
    sides[2][0] = 5;

    sides[3][0] = 0;
    sides[3][1] = 2;
    sides[3][2] = 6;
    sides[3][3] = 4;
  
    sides[4][0] = 0;
    sides[4][1] = 1;
    sides[4][2] = 5;
    sides[4][3] = 4;
  
    sides[5][0] = 2;
    sides[5][1] = 3;
    sides[5][2] = 7;
    sides[5][3] = 6;

    return sides;
  }
}
