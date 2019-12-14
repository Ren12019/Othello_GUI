import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Event;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;
import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.io.IOException;
//import java.net.*;
import java.net.MalformedURLException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;


public class GUI_osero extends Frame{
    private Label label = null;
    Button button = new Button("Reset");
    int c=0;
    int i,j;
    int status;
    int kuro,siro,next;
    int n,turn,enemy;
    int haba=40;
    double mode[][] = {{0,0,0,0,0,0,0,0},
                       {0,0,0,0,0,0,0,0},
                       {0,0,0,0,0,0,0,0},
                       {0,0,0,1,2,0,0,0},
                       {0,0,0,2,1,0,0,0},
                       {0,0,0,0,0,0,0,0},
                       {0,0,0,0,0,0,0,0},
                       {0,0,0,0,0,0,0,0}};
    
    public GUI_osero(){	
        super("Othello");
        setSize(600,500);
        Panel panel = new Panel();
        panel.setLayout(new BorderLayout());
        add(panel, BorderLayout.SOUTH);
        panel.add(button, BorderLayout.EAST);
        MyListener myListener = new MyListener();
        addMouseListener(myListener);
        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        }); 
        Random rnd = new Random();
        turn = rnd.nextInt(2) + 1;//先攻後攻の決定
        status=0;
    }
    
    public boolean action(Event e,Object o){
        if(o.equals("Reset")){
            for(i=0;i<8;i++){
                for(j=0;j<8;j++){
                    if((j==3&&i==3)||(j==4&&i==4)){
                        mode[j][i]=1;
                    }else if((j==3&&i==4)||(j==4&&i==3)){
                        mode[j][i]=2;
                    }else{
                        mode[j][i]=0;
                    }
                }
            }
            Random rnd2 = new Random();
            turn = rnd2.nextInt(2) + 1;
        }status=0;
        c=0;
        repaint();
        return true;
    }
    
    public void nextpattern(){
        int dx,dy,x2,y2;
        int out=0;
        if(turn==1){
            enemy=2;
        }else if(turn==2){
            enemy=1;
        }for(dy=-1;dy<=1;dy++){
            for(dx=-1;dx<=1;dx++){
                if(dy==0&&dx==0){
                    continue;
                }x2=j+dx;y2=i+dy;
                if(x2<0||8<=x2||y2<0||8<=y2){
                    continue;
                }if(mode[x2][y2]!=enemy){
                    continue;
                }do{
                    x2+=dx;y2+=dy;
                    if(x2<0||8<=x2||y2<0||8<=y2){
                        out=-1;break;
                    }
                }while(mode[x2][y2]==enemy);
                if(out<0){
                    out=0;continue;
                }if(mode[x2][y2]==0){
                    mode[x2][y2]=3;
                }
            }
        }
    }
    
    public void senryaku(){
        int dx,dy,x2,y2;
        int canx[]={0,0,0,0,0,0,0,0},cany[]={0,0,0,0,0,0,0,0};
        if(turn==1){
            enemy=2;
        }else if(turn==2){
            enemy=1;
        }else{
            System.out.println("TurnOver:error!\n");
        }for(dy=-1;dy<=1;dy++){
            for(dx=-1;dx<=1;dx++){
                if(dy==0&&dx==0){
                    continue;
                }n=0;
                x2=j+dx;y2=i+dy;
                if(x2<0||8<=x2||y2<0||8<=y2){
                    continue;
                }else if(mode[x2][y2]!=enemy){
                    continue;
                }canx[n]=x2;cany[n]=y2;n++;
                for(;;){
                    x2+=dx;y2+=dy;
                    if(x2<0||8<=x2||y2<0||8<=y2){
                        break;
                    }else if(mode[x2][y2]==enemy){
                        canx[n]=x2;cany[n]=y2;n++;
                    }else if(mode[x2][y2]==turn){
                        for(n--;n>=0;n--){
                            mode[canx[n]][cany[n]]=turn;
                        }break;
                    }else{
                        break;
                    }
                }
            }
        }
    }
    
    public void paint(Graphics g){
        for(i=0;i<8;i++){
            for(j=0;j<8;j++){
                if(mode[j][i]==turn){
                    nextpattern();
                }
            }
        }
        Color mozicolor = Color.black;
        Font font = new Font("IPAゴシック",Font.PLAIN,20);
        kuro=0;siro=0;next=0;
        for(i=0;i<8;i++){
            for(j=0;j<8;j++){
                g.setColor(Color.green);
                g.fillRect(50+haba*j,50+haba*i,haba,haba);
                g.setColor(Color.black);
                g.drawRect(50+haba*j,50+haba*i,haba,haba);
                if(mode[j][i]==1){
                    g.setColor(Color.black);
                    kuro+=1;
                }else if(mode[j][i]==2){
                    g.setColor(Color.white);
                    siro+=1;
                }else if(mode[j][i]==3){
                    g.setColor(Color.yellow);
                    g.fillRect(51+haba*j,51+haba*i,haba-1,haba-1);
                    next+=1;
                }else{
                    g.setColor(Color.green);
                }
                g.fillOval(51+haba*j,51+haba*i,haba-1,haba-1);
            }
        }
        g.setColor(mozicolor);
        g.setFont(font);
        if(turn==1){
            g.drawString(">>",380,70);
        }else{
            g.drawString(">>",380,90);
        }
        g.drawString("Black:"+kuro+"",400,70);
        g.drawString("White:"+siro+"",400,90);
        g.drawString("Place to put:"+next+"",400,110);
        if(status==0){
            if(kuro+siro>63 || siro==0 || kuro==0){
                syuuryou();
            }if(next==0 && kuro+siro<=63 && siro!=0 && kuro !=0){
                c+=1;
                turn=enemy;
                repaint();
                if(c==10){
                    syuuryou();
                }
            }if(turn==1 && kuro+siro<=63 && siro!=0 && kuro !=0){
                AI();
            }
        }else{
            if(kuro>siro){
                g.drawString("YOU LOSE",400,150);
            }else if(kuro<siro){
                g.drawString("YOU WIN!!",400,150);
            }else{
                g.drawString("DROW",400,150);
            }
        }
    }
    
    public void syuuryou(){
        System.out.println("Finish");
        status=1;
        repaint();
    }
    
    public void clear(){
        for(i=0;i<8;i++){
            for(j=0;j<8;j++){
                if(mode[j][i]==3){
                    mode[j][i]=0;
                }
            }
        }
    }
    
    public void AI(){
        int k=0;
        int comx[]={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},comy[]={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        Graphics g = getGraphics();
        if(turn==1){
            enemy=2;
        }else if(turn==2){
            enemy=1;
        }k=0;
        for(i=0;i<8;i++){
            for(j=0;j<8;j++){
                if(mode[j][i]==3){
                    comx[k]=j;comy[k]=i;
                    k++;
                }
            }
        }
        Random rnd = new Random();
        int com = rnd.nextInt(k);
        j=comx[com];i=comy[com];
        mode[j][i]=turn;
        senryaku();
        clear();
        turn=enemy;
        repaint();
        g.dispose();
    }
    
    public static void main(String[] args) throws Exception{
        Frame w = new GUI_osero();
        w.setVisible(true);
        Clip clip = createClip(new File("bgm0.wav"));
    }
    
    class MyListener extends MouseAdapter{
        int x,y;
        public void mouseClicked(MouseEvent m){
            Graphics g = getGraphics();
            x=m.getX();y=m.getY();
            if(50<x && x<=90){
                j=0;
            }else if(90<x && x<=130){
                j=1;
            }else if(130<x && x<=170){
                j=2;
            }else if(170<x && x<=210){
                j=3;
            }else if(210<x && x<=250){
                j=4;
            }else if(250<x && x<=290){
                j=5;
            }else if(290<x && x<=330){
                j=6;
            }else if(330<x && x<=370){
                j=7;
            }else{
                j=10;
            }if(50<y && y<=90){
                i=0;
            }else if(90<y && y<=130){
                i=1;
            }else if(130<y && y<=170){
                i=2;
            }else if(170<y && y<=210){
                i=3;
            }else if(210<y && y<=250){
                i=4;
            }else if(250<y && y<=290){
                i=5;
            }else if(290<y && y<=330){
                i=6;
            }else if(330<y && y<=370){
                i=7;
            }else{
                i=10;
            }if(j==10||i==10){
            }else{
                if(mode[j][i]!=3){
                }else{
                    mode[j][i]=turn;
                    senryaku();
                    clear();
                    turn=enemy;
                }
                repaint();
                g.dispose();
            }
        }
    }
    
    public static Clip createClip(File path) {
		//指定されたURLのオーディオ入力ストリームを取得
		try (AudioInputStream ais = AudioSystem.getAudioInputStream(path)){
			
			//ファイルの形式取得
			AudioFormat af = ais.getFormat();
			
			//単一のオーディオ形式を含む指定した情報からデータラインの情報オブジェクトを構築
			DataLine.Info dataLine = new DataLine.Info(Clip.class,af);
			
			//指定された Line.Info オブジェクトの記述に一致するラインを取得
			Clip c = (Clip)AudioSystem.getLine(dataLine);
			
			//再生準備完了
			c.open(ais);
			
			return c;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		return null;
	}

}