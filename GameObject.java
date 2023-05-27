//�C���������O��l�]�w
import java.awt.*;
public abstract class GameObject{
    //�Ϥ�(�@�P����)
    public Image img;
    //�y��(�@�P����)
    public int x,y;
    //����(�@�P����)
    public GamePanel gamePanel;

    public GameObject(String img,int x,int y,GamePanel gamePanel){
        this.img=Toolkit.getDefaultToolkit().getImage(img);//string��image
        this.x=x;
        this.y=y;
        this.gamePanel=gamePanel;
    }
    
    //�@�P��k ø�s
    public abstract void paintSelft(Graphics g);

    //�I������ �Z�J�P�l�u���x�θI������
    public abstract Rectangle getRect();
}