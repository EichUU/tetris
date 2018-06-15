package tetris;
import java.util.Random;

public class Shape {

    protected enum Tetrominoe { NoShape, ZShape, SShape, LineShape, 
               TShape, SquareShape, LShape, MirroredLShape }
    //enum 은 무엇을 뜻하는가?
               
    private Tetrominoe pieceShape; //이 변수는? enum 의 한 타입을 정의해 주기 위한 초석?
    private int coords[][]; //이 배열은?
    private int coordsTable[][][]; 


    public Shape() {

        coords = new int[4][2];
        setShape(Tetrominoe.NoShape); //이것이 무엇인가
    }

    public void setShape(Tetrominoe shape) {

         coordsTable = new int[][][] { //8개의 도형은 4가지의 좌표를 가진다, 그리고 각 회전값을 가진다
            { { 0, 0 },   { 0, 0 },   { 0, 0 },   { 0, 0 } },  // NoShape
            { { 0, -1 },  { 0, 0 },   { -1, 0 },  { -1, 1 } }, // SShape
            { { 0, -1 },  { 0, 0 },   { 1, 0 },   { 1, 1 } },  // ZShape
            { { 0, -1 },  { 0, 0 },   { 0, 1 },   { 0, 2 } },  // LineShape
            { { -1, 0 },  { 0, 0 },   { 1, 0 },   { 0, 1 } },  // TShape 
            { { 0, 0 },   { 1, 0 },   { 0, 1 },   { 1, 1 } },  // SquareShape
            { { -1, -1 }, { 0, -1 },  { 0, 0 },   { 0, 1 } },  // MirroredLShape
            { { 1, -1 },  { 0, -1 },  { 0, 0 },   { 0, 1 } }   // LShape
        };

        for (int i = 0; i < 4 ; i++) {
            
            for (int j = 0; j < 2; ++j) {
                
                coords[i][j] = coordsTable[shape.ordinal()][i][j]; 
                						//ordinal -> 나열된 순서를 숫자로 0부터  반환
              //배열에 배열을 대입하는 작업을 어떻게 이해해야하나?
            }
        } //이 2중 for문을 어떻게 이해해야하나
        
        pieceShape = shape; //2중for문으로 돌아간 배열모양의 도형이 pieceShape에 대입, 
        					//그것으로 enum안의 문자가 실제적인 도형이 된다 
        
    }

    private void setX(int index, int x) { 
    	coords[index][0] = x; //index는 무엇인가?
    }
    private void setY(int index, int y) {
    	coords[index][1] = y; 
    }
    public int x(int index) { 
    	return coords[index][0]; 
    }
    public int y(int index) {
    	return coords[index][1]; 
    }
    public Tetrominoe getShape()  {
    	return pieceShape; 
    }
 
    public void setRandomShape() { //도형이 렌덤으로 나오게 하는..?
        
        Random r = new Random();
        int x = Math.abs(r.nextInt()) % 7 + 1;
        Tetrominoe[] values = Tetrominoe.values(); 
        setShape(values[x]);
    }

    public int minX() { //머지?
        
      int m = coords[0][0];
      
      for (int i=0; i < 4; i++) {
          
          m = Math.min(m, coords[i][0]);
      }
      
      return m;
    }


    public int minY() { //머지?
        
      int m = coords[0][1];
      
      for (int i=0; i < 4; i++) {
          
          m = Math.min(m, coords[i][1]);
      }
      
      return m;
    }

    public Shape rotateLeft() { //왼쪽으로 회전시킨다	
        
        if (pieceShape == Tetrominoe.SquareShape) //SquareShape는 회전안한다 그대로 리턴
            return this;

        Shape result = new Shape();
        result.pieceShape = pieceShape;

        for (int i = 0; i < 4; ++i) {
            
            result.setX(i, y(i));
            result.setY(i, -x(i));
        }
        
        return result;
    }    
}
