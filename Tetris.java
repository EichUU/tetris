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
    
    private void initUI() {  //init �޼���� �ʱ�ȭ

        statusbar = new JLabel(" 0"); //�� ���� Board Ŭ�������� ó���ȴ�
        setLayout(bl);
        add("North", statusbar);
        
        //add("EAST",p1);  //���� ����� �������� �˰�ʹ�
        //p1.add(nextBlock);
        
        //add(statusbar, BorderLayout.NORTH); 
        //add(new TextArea(), BorderLayout.CENTER) api���� ���� Ŭ������ ������ ����

        Board board = new Board(this);
        add(board);
        board.start(); //���� Ŭ������ ���⼭ �ҷ��� �����ϴ°ǰ�?

        setTitle("Tetris");
        setSize(200, 400);
        setVisible(true);
       
        //JFrame �޺��޼���
        setDefaultCloseOperation(EXIT_ON_CLOSE); 
          //JFrame ���� ���� ���󰡴� �޼���
          // close�� ������ �߻��ϴ� �̺�Ʈ? , ������â ����� ���μ������� �ݴ´�
        setLocationRelativeTo(null); 
          //������ â�� ȭ���� ����� ���� ����
       
    }

    public JLabel getStatusBar() {

        return statusbar;
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> { 
        	//������..?
        	//�������� ��� �̺�Ʈ�� ó���� �Ŀ� �߻��Ѵ�
        	//runable �������̽��� �������ش�..

            Tetris tetris = new Tetris();
           
        });
    }
}
