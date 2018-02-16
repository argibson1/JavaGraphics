public class Plot{

  private int maxParen = 0;
  private int[][][] fcn = new int[5][0][0];
  private int[] length = new int[5];
  private int numberOfOperations = 6;
  private String currentNumber;
  private boolean needsParse = false;
  private double[] values = new double[100];
  private int[] varIndex = new int[100];
  private int[] valCode = new int[100];
  private int[] ops = new int[100];
  private int next = 0, next1 = 0; 
  private boolean nextValueIsNegative = false;
  private boolean setNegative = false;
  private int points = 0, yIterations = 0, xIterations = 0;
  private Point[] xyPlane;  
 
  public Plot(int[][] points, int[][] planes){}


  public Plot(String fcn, char var1, char var2, double xStart,
              double xEnd, double yStart, double yEnd, double spacing){
    // if an operation is in parenthesis then it immediately
    // has precidence over all other operations. Also the nested 
    // parenthesis are treated as top priority all should be

    // All fcns should be functions of two variables given by var1
    // and var 2

    // EX:  fcn = "x*10+(y/x)" var1 = 'x' var2 = 'y'
    // operation array which correlates to the string index of
    // operations
    
    int[][] operationOrder;
    int[] operationCode;
    // () 0: ^  1: *  2: /  3: %  4: -  5: +
    int paren = 0;
    maxParen = 1;
    needsParse = false;
    currentNumber = "";
    for( int i = 0; i < fcn.length(); i++){
      char current = fcn.charAt(i);
      switch(current){
        case '(':
          paren++;
          if(paren > maxParen - 1){
            maxParen++;
          }
          break;
        case ')':
          paren--;
          break;
        case '^':
          addOp( 0, paren, next1);
          break;
        case '*':
          addOp( 1, paren, next1);             
          break;
        case '%':
          addOp( 2, paren, next1);
          break;
        case '/':
          addOp( 3, paren, next1);
          break;
        // the subtraction is the same as making the value
        // negative and adding
        case '-':
          nextValueIsNegative = true;
          addOp( 5, paren, next1);
          break;
        case '+':
          addOp( 5, paren, next1);
          break;
        case ' ':
          break;
        case '0':
        case '1':
        case '2':
        case '3':
        case '4':
        case '5':
        case '6':
        case '7':
        case '8':
        case '9':
          // assumed this is always a number
          if(!needsParse){
            varIndex[next] = i;
            valCode[next] = 0;
            next++;
          }
          needsParse = true;
          currentNumber += current;                  
          break;

        default:
          
          if(current == var1){
            varIndex[next] = i;
            if(setNegative){
              valCode[next] = 2;
              setNegative = false;
            } else {
              valCode[next] = 1;
            }
            next++;
          } else if(current == var2){
            if(setNegative){
              valCode[next] = 4;
              setNegative = false;
            } else {
              valCode[next] = 3;
            }
            next++;     
          } else {
            //tag("\nINVALID CHAR @ index: " + i +
            //  "  |  " + "\"" + current + "\"" );
            System.exit(1);
          }     
        break;
      }
    }

    parseNumber();
    sortArray();

    // xyPlane[][0] -> x
    // xyPlane[][1] -> y
    // xyPlane[][2] -> z
    xyPlane =  generatePlot(xStart,xEnd,yStart,
                                       yEnd,spacing,spacing);
  
    int[][] surfaceCorners = getSurfaceCorners();
  }


  public void addOp(int code, int paren, int order ){
    int[][][] fcnOps;
    
    parseNumber();
    ops[next1++] = code;    
    int addIndex = fcn[paren].length;
    length[paren]++;
    fcnOps = new int[5][fcn[paren].length + 1][2];
    for(int i = 0; i < maxParen; i++){
      //tag("i :" + i + " | " );
      for(int j = 0; j < fcn[i].length; j++){
        if(fcn[i][j][0] == 0 && fcn[i][j][1] == 0){
          addIndex = j;
          break;
        } 
        fcnOps[i][j][0] = fcn[i][j][0];
        fcnOps[i][j][1] = fcn[i][j][1];
        
        //tag("{ " + fcnOps[i][j][0] + ", " +
        //fcnOps[i][j][1]);
        //tag(" }");
      } 
      //System.out.println();
    }

    //System.out.println(fcn[paren].length + " , " + order + " , " + code);
    fcnOps[paren][addIndex][0] = order;
    fcnOps[paren][addIndex][1] = code;
    fcn = fcnOps;
  }


  public void sortArray(){
    // need to store a length array which store values of length of index "fcn"
    for( int i = 0; i < maxParen; i++ ){
      int sortIndex = 0;
      for(int k = 0; k < numberOfOperations; k++){
        for( int j = sortIndex; j < length[i]; j++){
          if( fcn[i][j][1] == k){
            int[] temp;
            temp = fcn[i][j];
            fcn[i][j] = fcn[i][sortIndex];
            fcn[i][sortIndex] = temp;
            sortIndex++;
          }
        }
      }
    } 
    
    for(int i = 0; i < maxParen; i++){
      for( int j = 0; j < length[i]; j++){
        // tag("{ " + fcn[i][j][0] + " , " + fcn[i][j][1] + " }");
      }
      System.out.println();
    } 
  }


  public double operate(double var1, double var2){
    // values[]  array containing all of the constant values as doubles 
    // valueIndex[] the location of all values in String << dont think i need
    // valCode[] the 0: const, 1: var1, 2: -var1, 3: var2 , 4: -var2
    // values[] the actual stored values, and thes are in order where 0 
    // means its a var
    // fcn[][][] 1st array paren, 2nd order of operations <- shouldnt need
    // use this data
    // viewLine();
    // tag("\nInitial Values: ");
    // printValues();
    // tag("var1 =  " + var1 + " | var2 = " + var2 );
    
    int counter = 0;
    double[] tempValues = new double[next];
    int[] tempOps = new int[next1];
    int[] tempValCode = new int[next];
    
    for(int i = 0; i < next1; i++){
      tempOps[i] = ops[i]; 
    }
    
    for(int i = 0; i < next; i++){
      tempValues[i] = values[i];
      tempValCode[i] = valCode[i]; 
    }
   
    for(int i = maxParen - 1 ; i >= 0; i--){
      for(int j = 0; j < length[i]; j++){
        double prevVal = 0, postVal = 0;
        counter++;
        // viewLine();
        // tag("Iteration #" + counter + "\n"); 
        switch(tempValCode[fcn[i][j][0]]){
          case 0:
            prevVal = tempValues[fcn[i][j][0]];
            break;
          case 1:
            prevVal = var1;
            break;
          case 2:
            prevVal = -var1;
            break;
          case 3:
            prevVal = var2;
            break;
          case 4:
            prevVal = -var2;
            break;
        }
        switch(tempValCode[fcn[i][j][0]+1]){
          case 0:
            postVal = tempValues[fcn[i][j][0] + 1];
            break;
          case 1:
            postVal = var1;
            break;
          case 2:
            postVal = -var1;
            break;
          case 3:
            postVal = var2;
            break;
          case 4:
            postVal = -var2;
            break;
        }
        
        double result = 0;
        //operation type 
        switch(fcn[i][j][1]){
          
          // pow
          case 0:
            result = Math.pow(prevVal,postVal);           
            break;

          // *
          case 1:
            result = prevVal * postVal;
            break;

          // /
          case 2:
            result = prevVal / postVal;
            break;

          // %
          case 3:
            result = prevVal % postVal;
            break;

          // -
          case 4:
            result = prevVal - postVal;
            break;

          // +
          case 5:
            result = prevVal + postVal;
            break; 
        }

        // tag("operation: " + fcn[i][j][1]);
      
        tempOps[fcn[i][j][0]] = -1;
        tempValues[fcn[i][j][0]] = result;
        tempValues[fcn[i][j][0] + 1] = result;
        tempValCode[fcn[i][j][0]] = 0;
        tempValCode[fcn[i][j][0] + 1] = 0;
        
        // tag("\nprev: " + prevVal + " | postVal: " +
        //   postVal + " | result: " + result);

        // loops through values left setting all values already operated
        // to the result. Already operated will have null operation between data
        for(int x = fcn[i][j][0] - 1 ; x >= 0; x--){
          if( x == -1){ break; }
          if(tempOps[x] == -1){
            tempValues[x] = result;
          } else {
            break;
          }
        }

        // Iterates to the right to check if the current post value already
        // belongs to a completed operation chain 
        for(int y = fcn[i][j][0] + 1 ; y < next; y++){
          if(y >= next1){ break; }
          if(tempOps[y] == -1){
            tempValues[y + 1] = result;
          } else {
            break;
          }
        }
        
        // printValues(tempValues); 
        // printOperations();
      }
    }
   
    // viewLine();
     
    // tag("Final Values: ");
    // printValues(tempValues);
    
    // tag("Final Value: ");
    // tag(tempValues[0]);
    // tag();
    return tempValues[0];  
  }


  private void parseNumber(){
    
    if( needsParse ){
      //tag("\n" + currentNumber);
      values[ next - 1 ] = Double.parseDouble(currentNumber);
      
      if(setNegative){
        values[ next - 1 ] *= -1;
        setNegative = false;
      }
       
      currentNumber = "";
      needsParse = false;
    }

    if(nextValueIsNegative){
      setNegative = true;
      nextValueIsNegative = false;
    }
  }

  private Point[] generatePlot(double xMin, double xMax, double yMin,
    double yMax, double dy, double dx){ 
    
    yIterations = (int)((yMax - yMin)/dy)+1;
    xIterations = (int)((xMax - xMin)/dx)+1;

    if(xIterations > 100 || yIterations > 100){
      tag("\nCRASH SAFETY, TOO MUCH DATA");
      System.exit(1);
    }
    
    // tag("y: " + yIterations);  
    // tag("x: " + xIterations);  

    Point[] points = new Point[(yIterations+1)*(xIterations)*2];   

    int counter = 0;
    double xStart = xMin;
    double yStart = yMin;
    for( int i = 0; i < (int)(xIterations * 2); i++){
      if(i%2 == 1){
        yStart += dy/2;
        yIterations--;
      } else if( i != 0){
        yStart -= dy/2;
        yIterations++;
      }

      // tag();
      // tag("y: " + yIterations);  
      for( int j = 0; j < (int)yIterations ; j++){
        points[counter] = new Point();
        points[counter].setX(xStart + i * dx/2);
        points[counter].setY(yStart + j * dy);
        points[counter].setZ(operate(points[counter].getY(),
                             points[counter].getX())); 
      
        if(j < (int)yIterations - 1){
           tag(" | ");
        }
        counter++;
      }
      //viewLine();
    }

   /*tag();
    tag("xIterations -> " + xIterations);
    tag();
    tag("yIterations -> " + yIterations);
    tag();
    tag("yIterations -> " + yIterations);
    tag();
    tag("yIterations -> " + yIterations);
    */
    yIterations++;
    setPoints(counter);
    return points;
  }

  public int[][] getSurfaceCorners(){
    tag(points); 
    int[][] data = new int[(int)(Math.pow(yIterations ,2))*4][3];

    int i = 0;
    int counter = 0;
    int rollOverIndex = (yIterations + (yIterations - 1)); 
    while(i < points - yIterations){
      if(i % rollOverIndex == 0 || i == 0){
        i += yIterations;
        tag();
      }
      data[counter][0] = i;
     
      switch(counter % 4){
        case 0: 
          data[counter][1] = i - yIterations;
          data[counter][2] = i - yIterations + 1;
        break;
        case 1:        
          data[counter][1] = i - yIterations + 1;
          data[counter][2] = i + yIterations; 
        break;
        case 2: 
          data[counter][1] = i + yIterations; 
          data[counter][2] = i + yIterations - 1; 
        break;
        case 3: 
          data[counter][1] = i + yIterations - 1; 
          data[counter][2] = i - yIterations;
          tag(i + " , ");
          i++; 
        break;
      }

      counter++;
    }
    return data; 
  }

  private void setPoints(int p){
    this.points = p;
  }

  public int getPoints(){
    return this.points;
  }

  // prints all the curent stored values
  private void printValues(){
   
    tag("\nvalues: { ");
    for(int i = 0; i < next; i++){
      tag( values[i]);
      if( i < next - 1){
        tag( " , ");
      }
    }
    tag(" }\n");
  }


  private void printValues(double[] values){
   
    tag("\nvalues: { ");
    for(int i = 0; i < next; i++){
      tag( values[i]);
      if( i < next - 1){
        tag( " , ");
      }
    }
    tag(" }\n");
  }

   
  // prints the ops array
  private void printOperations(){
    
    tag("ops: { ");
    for(int i = 0; i < next1; i++){
      tag( ops[i] );
      if( i < next1 - 1){
        tag( " , ");
      }
    }
    tag(" }"); 
  }


  private void tag(Object out){
    System.out.print(out);
  }

  
  private void tag(){
    tag("\n");
  }

  
  private void viewLine(){
    tag("\n----------------------------\n");
  }

  public Point[] get3DPoints(){
    return xyPlane;
  } 
}
