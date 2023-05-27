import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Bullet extends GameObject{

    // �ؤo
    public int width = 10;
    public int height = 10;

    // �t��
    public int speed = 7;
    // ��V
    Direction direction;

    public Bullet(String img, int x, int y, GamePanel gamePanel, Direction direction) {
        super(img, x, y, gamePanel);
        this.direction = direction;
    }

    //�l�u�|�Ӥ�V�����ʤ�k
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

    //�l�u�����ʤ�k(�`�M)
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

    //�l�u���a�I������
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

    //�l�u�����I������
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

    //�l�u����ɸI������ �h�l���R���קK���s����
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
