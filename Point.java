public class Point{
  private double x;
  private double y;
  private double z;
  private double screenX;
  private double screenY;
  private double distance;
  final double PI = 3.1415962;

  Point(){
    this(0,0,0);
  }

  Point( double x, double y, double z){
    this(x,y,z,0,0);
  }

  Point(double x, double y, double z, double alpha, double rho){
    super();
    setX(x);
    setY(y);
    setZ(z);
    setScreenPoints(alpha,rho);
  }

  public void calcScreenPoints(){
    this.calcScreenPoints(ThreeDimensionalSpace.getUser()[0],
                          ThreeDimensionalSpace.getUser()[1],
                          ThreeDimensionalSpace.getUser()[2], 
                          ThreeDimensionalSpace.getCosPhi(),
			  ThreeDimensionalSpace.getSinPhi(),
			  ThreeDimensionalSpace.getTanGamma());
  }
 
  public void calcScreenPoints(double uX, double uY, double uZ,double cosPhi,double sinPhi, double tanGamma){
    double Ax = x - uX;
    double Ay = y - uY;
    double Az = z - uZ;
    double len = Math.sqrt(Math.pow(Ax, 2)+Math.pow(Ay, 2) + Math.pow(Az, 2));
    double AdotJ = Ax*cosPhi+Ay*sinPhi+Az*tanGamma;
    double AonJX = Ax - AdotJ*cosPhi;
    double AonJY = Ay - AdotJ*sinPhi;
    double AonJZ = Az - AdotJ*tanGamma;
    double rhoLen = Math.sqrt(Math.pow(AonJX,2) + Math.pow(AonJY,2) + Math.pow(AonJZ,2));
    double alpha = arcC((float)AdotJ,1,(float)len);
    double rho = arcC((float)(sinPhi*(AonJX) - cosPhi*(AonJY)),
                 AonJZ,
                 (float)rhoLen*Math.sqrt(Math.pow(cosPhi,2)+Math.pow(sinPhi,2))-.0000000000005);

    setScreenPoints(alpha, rho);
  }
 
  private void setScreenPoints(double alpha, double rho){
    double finalX = alpha*Math.cos(rho)*ThreeDimensionalSpace.screenX/
    ThreeDimensionalSpace.theta/2 +ThreeDimensionalSpace.screenX/2;
    double finalY = alpha*Math.sin(rho)*ThreeDimensionalSpace.screenY/
    ThreeDimensionalSpace.theta/2 +ThreeDimensionalSpace.screenY/2;
  
    /*if((int)finalX >= 0 && (int)finalX <= ThreeDimensionalSpace.screenX){
      if((int)finalY >= 0 && (int)finalY <= ThreeDimensionalSpace.screenY){
        this.setScreenX(finalX);
        this.setScreenY(finalY);
        return;
      }
    }
    offScreen = true;    
 
    if((int)finalY < 0){
      //Intersects with Y upperPlane
      offUpper = true;
    }else{
      offLower = true;
    }

    if((int)finalX < 0){
      offLeft = true;
    } else{
      offRight = true;
    }*/

    this.setScreenX(finalX);
    this.setScreenY(finalY);
  }

  public void setScreenX(double x){ 
    this.screenX = x;
  }

  public void setScreenY(double y){
    this.screenY = y;
  }

  public void setX(double x){
    this.x = x;
  } 

  public void setY(double y){
    this.y = y;
  }
 
  public void setZ(double z){
    this.z = z;
  } 

  public double getX(){
    return this.x;
  }

  public double getY(){
    return this.y;
  }

  public double getZ(){
    return this.z;
  }

  public double getDistance(){
    return this.distance;
  }

  public void move(double dx, double dy, double dz){
    x+=dx;
    y+=dy;
    z+=dz;
  }

  public void moveTo(double dx, double dy, double dz){
    x = dx;
    y = dy;
    z = dz;
  }  

  public void calculateDistance(int px, int py, int pz){
    distance = Math.sqrt(Math.pow(this.z - pz,2)+
               Math.pow(this.y - py,2)+
               Math.pow(this.z - pz,2));
  }

  public int getScreenX(){
    return (int)this.screenX;
  }

  public int getScreenY(){
    return (int)this.screenY;
  }

  public double arcC(double x, double y, double r){
    if(x/r > 1){
      r = x;
    } else if(x/r < -1) {
      r = -x;
    }
    if(y < 0) 
      return -Math.acos(x/r);
    else
      return Math.acos(x/r);
  }

  public void tag(Object o, String n){
    System.out.print(" | "+n+": "+o);
  }  
}
