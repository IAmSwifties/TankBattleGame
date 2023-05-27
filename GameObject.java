//遊戲的父類別初始設定
import java.awt.*;
public abstract class GameObject{
    //圖片(共同元素)
    public Image img;
    //座標(共同元素)
    public int x,y;
    //介面(共同元素)
    public GamePanel gamePanel;

    public GameObject(String img,int x,int y,GamePanel gamePanel){
        this.img=Toolkit.getDefaultToolkit().getImage(img);//string改image
        this.x=x;
        this.y=y;
        this.gamePanel=gamePanel;
    }
    
    //共同方法 繪製
    public abstract void paintSelft(Graphics g);

    //碰撞測試 坦克與子彈的矩形碰撞測試
    public abstract Rectangle getRect();
}