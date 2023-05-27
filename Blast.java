import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
public class Blast extends GameObject{
    //爆炸圖集
   static Image[] imgs = new Image[7];//7不知道為啥不行
    //爆炸圖集的靜態初始化
   int explodeCount = 0;
   static{
        for(int i=0;i<7;i++){
            imgs[i]=Toolkit.getDefaultToolkit().getImage("image/blast/blast"+(i+1)+".png");
        }
   }

   public Blast(String img,int x,int y,GamePanel gamePanel){
       super(img,x,y,gamePanel);
   }
    @Override
    public void paintSelft(Graphics g){
        if(explodeCount<7){
            if (!g.drawImage(imgs[explodeCount], x, y, null)){
                for (int i = 0; i < 7; i++){
                    g.drawImage(imgs[i], -1000, -1000, null);
                }
                g.drawImage(imgs[explodeCount], x, y, null);
            }
            explodeCount++;
        }
    }
    @Override
    public Rectangle getRect(){
          return null;
    }
}