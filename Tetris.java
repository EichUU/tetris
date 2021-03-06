package tetris;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Panel;

import javax.swing.JFrame;
import javax.swing.JLabel;

/*
Java Tetris game clone

Author: Jan Bodnar
Website: http://zetcode.com
 */
public class Tetris extends JFrame { 

    private JLabel statusbar;
    private BorderLayout bl = new BorderLayout();
    private Panel p1 = new Panel();
    
    public Tetris() {

        initUI();
    }
    
    private void initUI() {  //init 메서드로 초기화

        statusbar = new JLabel(" 0"); //이 라벨은 Board 클래스에서 처리된다
        setLayout(bl);
        add("North", statusbar);
        
        //add("EAST",p1);  //다음 블록이 무엇인지 알고싶다
        //p1.add(nextBlock);
        
        //add(statusbar, BorderLayout.NORTH); 
        //add(new TextArea(), BorderLayout.CENTER) api에서 나온 클래스의 간편한 사용법

        Board board = new Board(this);
        add(board);
        board.start(); //보드 클래스를 여기서 불러내 실행하는건가?

        setTitle("Tetris");
        setSize(200, 400);
        setVisible(true);
       
        //JFrame 콤보메서드
        setDefaultCloseOperation(EXIT_ON_CLOSE); 
          //JFrame 사용시 거의 따라가는 메서드
          // close를 누르면 발생하는 이벤트? , 윈도우창 종료시 프로세스까지 닫는다
        setLocationRelativeTo(null); 
          //윈도우 창을 화면의 가운데에 띄우는 역할
       
    }

    public JLabel getStatusBar() {

        return statusbar;
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> { 
        	//사용법이..?
        	//보류중인 모든 이벤트가 처리된 후에 발생한다
        	//runable 인터페이스를 실행해준다..

            Tetris tetris = new Tetris();
           
        });
    }
}
