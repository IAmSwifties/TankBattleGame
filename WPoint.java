import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

public class WPoint extends GameObject{
    int width = 40;
    int height = 40;

    public WPoint(String img, int x, int y, GamePanel gamePanel) {
        super(img, x, y, gamePanel);
        
    }

    //¤l¼u©ó³òÀð¸I¼²´ú¸Õ
    public boolean hitWalls(){
        ArrayList<Wall> walls = this.gamePanel.wallList;
        for (Wall wall: walls){
            if (this.getRect().intersects(wall.getRect())){
                return true;
            }
        }
        walls = this.gamePanel.wallList1;
        for (Wall wall: walls){
            if (this.getRect().intersects(wall.getRect())){
                return true;
            }
        }
        return false;
    }

    @Override
    public void paintSelft(Graphics g) {
        g.drawImage(img, x, y, width, height, null);
    }

    @Override
    public Rectangle getRect() {
        return new Rectangle(x, y, width, height);
    }
    
}
