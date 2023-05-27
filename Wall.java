import java.awt.Graphics;
import java.awt.Rectangle;

public class Wall extends GameObject{

    //คุคo
    int length = 50;

    public Wall(String img, int x, int y, GamePanel gamePanel) {
        super(img, x, y, gamePanel);
        //TODO Auto-generated constructor stub
    }

    @Override
    public void paintSelft(Graphics g) {
        g.drawImage(img, x, y, 60, 60, gamePanel);
    }

    @Override
    public Rectangle getRect() {
        return new Rectangle(x, y, 60, 60);
    }
    
}
