import java.awt.*;
import java.awt.event.*;


public class Base extends GameObject{
   int length =50;

   
   
   public Base(String img,int x,int y,GamePanel gamePanel){
       super(img,x,y,gamePanel);
   }
    @Override
    public void paintSelft(Graphics g){
       g.drawImage(img, x, y, 60, 60, null);
    }
    @Override
    public Rectangle getRect(){
          return new Rectangle(x, y, 60, 60);
    }
}