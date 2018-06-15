package tetris;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import tetris.Shape.Tetrominoe;

public class Board extends JPanel 
        implements ActionListener {

    private final int BOARD_WIDTH = 10;
    private final int BOARD_HEIGHT = 22; 
    private final int DELAY = 400; //게임 속도가 아님, actionPerformed()를 실행하기위한 시간설정

    private Timer timer;
    private boolean isFallingFinished = false; //블록이 맨 밑 바닥까지 내려왔는가?
    private boolean isStarted = false; //시작과 정지를 위한 변수선언
    private boolean isPaused = false; 
    private int numLinesRemoved = 0; //몇 줄을 지웠는지 확인하기 위한 변수, 점차 쌓인다
    
    private int curX = 0; //떨어지는 테트리스 블럭의 실제 위치를 정의
    private int curY = 0;
    private JLabel statusbar;
    private Shape curPiece; //Shape 클래스를 import 해 온 것을 curpiece 변수로 선언
    private Tetrominoe[] board; //Shape 클래스에서 도형의 모양(?)을 정의한 Tetrominoe 배열???

    public Board(Tetris parent) {

        initBoard(parent);
    }
    
    private void initBoard(Tetris parent) { //초기화
        
       setFocusable(true); //키보드event 포커스를 받을 수 있도록 설정
       curPiece = new Shape();
       
       timer = new Timer(DELAY, this); 
       timer.start(); 	//매 DELAY(400ms)마다 actionPerformed()를 호출한다

       statusbar =  parent.getStatusBar();
       board = new Tetrominoe[BOARD_WIDTH * BOARD_HEIGHT]; //보드는 Tetrominoe[10*22]의 크기를 가지나?
       addKeyListener(new TAdapter());
       clearBoard();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) { 
        
        if (isFallingFinished) {	//블록 떨어지는 것이 끝났는지 확인한다
            
            isFallingFinished = false; //이 부분이 좀.. 어렵다
            newPiece(); //FallingFinished 이면 newPiece() 실행
        } else {
            
            oneLineDown(); // FallingFinished 가 아니면 떨어지는 블럭은 oneLineDown() 실행
        }
    }

    private int squareWidth() {  //이 변수들은 무엇인가?
    	return (int) getSize().getWidth() / BOARD_WIDTH; 
    }
    private int squareHeight() { 
    	return (int) getSize().getHeight() / BOARD_HEIGHT; 
    }
    
    private Tetrominoe shapeAt(int x, int y) { 
    	return board[(y * BOARD_WIDTH) + x]; 
    }
    
    public void start()  { //시작메서드
        
        if (isPaused)
            return;

        isStarted = true;
        isFallingFinished = false;
        numLinesRemoved = 0;
        clearBoard();

        newPiece();
        timer.start();
    }

    private void pause()  { //일시정지 메서드
        
        if (!isStarted)
            return;

        isPaused = !isPaused;
        
        if (isPaused) {
            
            timer.stop();
            statusbar.setText("paused");
        } else {
            
            timer.start();
            statusbar.setText(String.valueOf(numLinesRemoved));
        }
        
        repaint();
    }
    
    private void doDrawing(Graphics g) { //보드 안에 모든 객체 그리기
        
        Dimension size = getSize(); //그래픽 보드에 frame의 사이즈를 가져온다
        
        int boardTop = (int) size.getHeight() - BOARD_HEIGHT * squareHeight();

        for (int i = 0; i < BOARD_HEIGHT; ++i) {
            
            for (int j = 0; j < BOARD_WIDTH; ++j) {
                
                Tetrominoe shape = shapeAt(j, BOARD_HEIGHT - i - 1);
                
                if (shape != Tetrominoe.NoShape)
                    drawSquare(g, 0 + j * squareWidth(),
                               boardTop + i * squareHeight(), shape);
            } //보드바닥에 떨어뜨린 모든 조각의 모양을 shapeAt()를 통해 보드배열에 담는다
        }

        if (curPiece.getShape() != Tetrominoe.NoShape) {
            
            for (int i = 0; i < 4; ++i) {
                
                int x = curX + curPiece.x(i);
                int y = curY - curPiece.y(i);
                drawSquare(g, 0 + x * squareWidth(),
                           boardTop + (BOARD_HEIGHT - y - 1) * squareHeight(),
                           curPiece.getShape());
            }
        } //실제로 떨어지고 있는 그 한 부분을 그린다
    }

    @Override
    public void paintComponent(Graphics g) { 

        super.paintComponent(g);
        doDrawing(g);
    }

    private void dropDown() { //블럭 하나를 바닥에 붙인다(그럼 한번에 떨어지는 것 처럼 보임)
        
        int newY = curY;
        
        while (newY > 0) {
            
            if (!tryMove(curPiece, curX, newY - 1))
                break;
            --newY;
        }
        
        pieceDropped();
    }

    private void oneLineDown()  {  //한 줄 아래로 이동
        
        if (!tryMove(curPiece, curX, curY - 1)) //curY가 -1 되면 pieceDropped() 실행
            pieceDropped();
    }


    private void clearBoard() { //보드를 비어있게 NoShape로 채운다
        
        for (int i = 0; i < BOARD_HEIGHT * BOARD_WIDTH; ++i)
            board[i] = Tetrominoe.NoShape;
    } //NoShape는 충돌방지를 감지해준다..?

    private void pieceDropped() { // 블럭이 떨어지면 
        
        for (int i = 0; i < 4; ++i) {
            
            int x = curX + curPiece.x(i);
            int y = curY - curPiece.y(i);
            board[(y * BOARD_WIDTH) + x] = curPiece.getShape();
        } // 떨어지고 있는 그 상태를 보드배열에 담고

        removeFullLines(); // 메서드를 실행해서 지울 수 있는지 확인

        if (!isFallingFinished)
            newPiece(); // 바닥까지 다 떨어졌다면 새 조각 생성
    }

    private void newPiece()  { //새 조각을 만드는 메서드, 더이상 만들 수 없으면 게임오버
        
        curPiece.setRandomShape(); //랜덤으로 모양을 만들어서
        curX = BOARD_WIDTH / 2 + 1;
        curY = BOARD_HEIGHT - 1 + curPiece.minY();

        if (!tryMove(curPiece, curX, curY)) { 
        	//curX와 curY가 초기 자리에 위치할 수 없다면 게임은 끝난다
            curPiece.setShape(Tetrominoe.NoShape);
            timer.stop(); //타이머는 종료되고
            isStarted = false;
            statusbar.setText("game over"); //상태표시줄에 game over 표시
        }
    }

    private boolean tryMove(Shape newPiece, int newX, int newY) {
        
        for (int i = 0; i < 4; ++i) {
            
            int x = newX + newPiece.x(i);
            int y = newY - newPiece.y(i);
            
            if (x < 0 || x >= BOARD_WIDTH || y < 0 || y >= BOARD_HEIGHT)
                return false;
            //보드의 좌우 벽과 아래위 경계선에 도달할 경우 false반환, 바깥으로 벗어날 수 없다
            
            if (shapeAt(x, y) != Tetrominoe.NoShape)
                return false;
            //이미 떨어진 인접한 블럭에 도달했을 때 false반환, 움직일 수 없다  
        }

        curPiece = newPiece;
        curX = newX;
        curY = newY;

        repaint();

        return true;
    }

    private void removeFullLines() {  //조각이 바닥에 떨어질 때 몇 줄 지워야 할지 
        
        int numFullLines = 0;

        for (int i = BOARD_HEIGHT - 1; i >= 0; --i) {
            boolean lineIsFull = true;  

            for (int j = 0; j < BOARD_WIDTH; ++j) { //밑에서 부터 한 줄씩 바닥을 확인 
                if (shapeAt(j, i) == Tetrominoe.NoShape) {
                    lineIsFull = false;
                    break;
                }
            }

            if (lineIsFull) {
                ++numFullLines;
                for (int k = i; k < BOARD_HEIGHT - 1; ++k) {
                    for (int j = 0; j < BOARD_WIDTH; ++j)
                         board[(k * BOARD_WIDTH) + j] = shapeAt(j, k + 1);
                }//삭제 된 위의 모든 행을 삭제 된 줄 만큼 아래로 내린다
            }
        }

        if (numFullLines > 0) {
            
            numLinesRemoved += numFullLines; //지운 줄이 누적해서 statusbar에 표시된다
            statusbar.setText(String.valueOf(numLinesRemoved));
            isFallingFinished = true;
            curPiece.setShape(Tetrominoe.NoShape);
            repaint();
        }
     }

    private void drawSquare(Graphics g, int x, int y, Tetrominoe shape)  {
        //도형 색 추가하기
        Color colors[] = { new Color(0, 0, 0), new Color(204, 102, 102), 
            new Color(102, 204, 102), new Color(102, 102, 204), 
            new Color(204, 204, 102), new Color(204, 102, 204), 
            new Color(102, 204, 204), new Color(218, 170, 0)
        };

        Color color = colors[shape.ordinal()];

        g.setColor(color);
        g.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2);

        g.setColor(color.brighter());
        g.drawLine(x, y + squareHeight() - 1, x, y);
        g.drawLine(x, y, x + squareWidth() - 1, y);

        g.setColor(color.darker());
        g.drawLine(x + 1, y + squareHeight() - 1,
                         x + squareWidth() - 1, y + squareHeight() - 1);
        g.drawLine(x + squareWidth() - 1, y + squareHeight() - 1,
                         x + squareWidth() - 1, y + 1);

    }

    class TAdapter extends KeyAdapter {
        
         @Override
         public void keyPressed(KeyEvent e) {

             if (!isStarted || curPiece.getShape() == Tetrominoe.NoShape) {  
                 return;
             }

             int keycode = e.getKeyCode();

             if (keycode == 'P') { //p를 누르면 pause 메서드 실행
                 pause();
                 return;
             }

             if (isPaused)
                 return;

             switch (keycode) {
                 
             case KeyEvent.VK_LEFT:
                 tryMove(curPiece, curX - 1, curY);
                 break;
                 
             case KeyEvent.VK_RIGHT:
                 tryMove(curPiece, curX + 1, curY);
                 break;
                              
             case KeyEvent.VK_UP:
                 tryMove(curPiece.rotateLeft(), curX, curY);
                 break;
                 
             case KeyEvent.VK_SPACE:
                 dropDown();
                 break;
                 
             case KeyEvent.VK_DOWN:
                 oneLineDown();
                 break;
             }
         }
     }
}
