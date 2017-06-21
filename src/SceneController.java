/**
 * Created by astruc2 on 6/20/2017.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SceneController {
    private JFrame mFrame;
    private JPanel mPane;
    private JLabel riceBoard[][];
    private ImageIcon ricePic;
    private int score=0;
    private boolean movRight=true;
    private boolean dropping=false;
    private boolean dead;
    private int activeRice[][];
    long timeDelay = 500;

    public SceneController(){
        activeRice = new int[2][2];
        mFrame = new JFrame();
        mFrame.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                dropping = true;
            }
        });
        mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mFrame.setSize(1200,900);
        mFrame.setTitle("Score: "+score);
        mPane = new JPanel();
        mPane.setLayout(new GridLayout(6,5));
        mFrame.add(mPane);

        ricePic = new ImageIcon("res/rice.png");
        riceBoard = new JLabel[6][5];
        for(int x = 0;x<6;x++){
            for(int y = 0;y<5;y++){
                riceBoard[x][y]=new JLabel();
                riceBoard[x][y].setSize(233,142);
                riceBoard[x][y].setIcon(ricePic);
                mPane.add(riceBoard[x][y],x,y);
                setRiceFalse(riceBoard[x][y]);
            }
        }
        setRiceTrue(riceBoard[5][0]);
        setRiceInPlay(5,0);
        setRiceTrue(riceBoard[0][2]);
        setRiceTarget(0,2);
        mFrame.setVisible(true);
    }

    public void sim() throws InterruptedException {
        if(!dropping)movRice();
        else {
            dropRice();
            int tempY = (int)(5*Math.random());
            setRiceTrue(riceBoard[5][tempY]);
            setRiceFalse(riceBoard[activeRice[0][0]][activeRice[0][1]]);
            setRiceInPlay(5, tempY);
        }
        Thread.sleep(timeDelay);
    }

    public void dropRice() throws InterruptedException {
        for(int z = 4;z>0;z--) {
            int x = activeRice[0][0];
            int y = activeRice[0][1];
            setRiceTrue(riceBoard[z][y]);
            setRiceFalse(riceBoard[x][y]);
            setRiceInPlay(z, y);
            Thread.sleep(500);
        }
        if(activeRice[0][1]==activeRice[1][1]){
            score++;
            mFrame.setTitle("Score: "+score);
            timeDelay-=17;
        }else { //DEAD
            timeDelay = 500;
            score=0;
            mFrame.setTitle("Score: "+score);
        }
        dropping = false;
    }

    public void movRice(){
        if(movRight)movRight();
        else movLeft();
    }

    public void movRight(){
        int x=activeRice[0][0];
        int y=activeRice[0][1];

        if(y<4){
            setRiceTrue(riceBoard[x][y+1]);
            setRiceFalse(riceBoard[x][y]);
            setRiceInPlay(x,y+1);
            if(y+1 == 4)movRight = false;
        }else movRight = false;
    }
    public void movLeft(){
        int x=activeRice[0][0];
        int y=activeRice[0][1];
        if(y>0){
            setRiceTrue(riceBoard[x][y-1]);
            setRiceFalse(riceBoard[x][y]);
            setRiceInPlay(x,y-1);
            if(y-1 == 0)movRight = true;
        }else movRight = true;
    }

    public void setRiceTrue(JLabel riceTile){
        riceTile.setVisible(true);
        riceTile.setEnabled(true);
    }
    public void setRiceFalse(JLabel riceTile){
        riceTile.setVisible(false);
        riceTile.setEnabled(false);
    }
    public void setRiceInPlay(int x, int y){    //[0][*] is player rice
        activeRice[0][0] = x;
        activeRice[0][1] = y;
    }
    public void setRiceTarget(int x, int y){
        activeRice[1][0] = x;
        activeRice[1][1] = y;
    }
}
