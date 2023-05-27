import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Bullet extends GameObject{

    // 尺寸
    public int width = 10;
    public int height = 10;

    // 速度
    public int speed = 7;
    // 方向
    Direction direction;

    public Bullet(String img, int x, int y, GamePanel gamePanel, Direction direction) {
        super(img, x, y, gamePanel);
        this.direction = direction;
    }

    //子彈四個方向的移動方法
    public void leftWard(){
        x -= speed;
    }

    public void rightWard(){
        x += speed;
    }

    public void upWard(){
        y -= speed;
    }

    public void downWard(){
        y += speed;
    }

    //子彈的移動方法(總和)
    public void go(){
        switch (direction) {
            case LEFT:
                leftWard();
                break;
            case RIGHT:
                rightWard();
                break;
            case UP:
                upWard();
                break;
            case DOWN:
                downWard();
                break;
        }
        this.hitWall();
        this.moveToBorder();
        this.hitBase();
    }

    public void hitBot(){
        ArrayList<Bot> bots = this.gamePanel.botList;
        for (Bot bot : bots){
            if (this.getRect().intersects(bot.getRect())){
                this.gamePanel.blastList.add(new Blast("", bot.x-34, bot.y-14, this.gamePanel));
                this.gamePanel.botList.remove(bot);
                this.gamePanel.removeList.add(this);
                break;
            }
        }
    }

    //子彈於基地碰撞測試
    public void hitBase(){
        ArrayList<Base> baseList = this.gamePanel.baseList;
        for (Base base : baseList){
            if (this.getRect().intersects(base.getRect())){
                this.gamePanel.baseList.remove(base);
                this.gamePanel.removeList.add(this);
                break;
            }
        }
    }

    //子彈於圍牆碰撞測試
    public void hitWall(){
        ArrayList<Wall> walls = this.gamePanel.wallList;
        for (Wall wall: walls){
            if (this.getRect().intersects(wall.getRect())){
                this.gamePanel.wallList.remove(wall);
                this.gamePanel.removeList.add(this);
                break;
            }
        }
        walls = this.gamePanel.wallList1;
        for (Wall wall: walls){
            if (this.getRect().intersects(wall.getRect())){
                this.gamePanel.removeList.add(this);
                break;
            }
        }
    }

    //子彈於邊界碰撞測試 多餘的刪除避免內存炸裂
    public void moveToBorder(){
        if (x < 0 || x + width > this.gamePanel.getWidth()){
			this.gamePanel.removeList.add(this);
		}
		else if (y < 0 || y + height > this.gamePanel.getHeight()){
			this.gamePanel.removeList.add(this);
		}
    }

    @Override
    public void paintSelft(Graphics g) {
        g.drawImage(img, x, y, 10, 10, null);
        this.go();
        this.hitBot();
    }

    @Override
    public Rectangle getRect() {
        return new Rectangle(x, y, width, height);
    }
}
