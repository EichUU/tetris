package tetris;
import java.util.Random;

public class Shape {

    protected enum Tetrominoe { NoShape, ZShape, SShape, LineShape, 
               TShape, SquareShape, LShape, MirroredLShape };
    //enum 은 무엇을 뜻하는가?
               
    private Tetrominoe pieceShape; //이 변수는?
    private int coords[][]; //이 배열은?
    private int[][][] coordsTable; 


    public Shape() {

        coords = new int[4][2];
        setShape(Tetrominoe.NoShape); //이것이 무엇인가
    }

    public void setShape(Tetrominoe shape) {

         coordsTable = new int[][][] { //도형을 나타내는 배열이다
            { { 0, 0 },   { 0, 0 },   { 0, 0 },   { 0, 0 } },  // NoShape
            { { 0, -1 },  { 0, 0 },   { -1, 0 },  { -1, 1 } }, // SShape?
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
                
            }
        } //이 2중 for문을 어떻게 이해해야하나
        
        pieceShape = shape;
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

    public int minX() {
        
      int m = coords[0][0];
      
      for (int i=0; i < 4; i++) {
          
          m = Math.min(m, coords[i][0]);
      }
      
      return m;
    }


    public int minY() {
        
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

    public Shape rotateRight() { //오른쪽으로 회전시킨다
        
        if (pieceShape == Tetrominoe.SquareShape)
            return this;

        Shape result = new Shape();
        result.pieceShape = pieceShape;

        for (int i = 0; i < 4; ++i) {

            result.setX(i, -y(i));
            result.setY(i, x(i));
        }
        
        return result;
    }
}
