import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Random;

public class Bot extends Tank{
    int moveTime = 0;
    public Bot(String img, int x, int y, GamePanel gamePanel, String upImg, String leftImg, String rightImg,
            String downImg) {
        super(img, x, y, gamePanel, upImg, leftImg, rightImg, downImg);
        //TODO Auto-generated constructor stub
    }

    //敵方坦克獲得隨機方向
    public Direction getRandomDirection(){
        Random random = new Random();
        int rnum = random.nextInt(4);
        switch (rnum) {
            case 0:
                return Direction.LEFT;
            case 1:
                return Direction.RIGHT;
            case 2:
                return Direction.UP;
            case 3:
                return Direction.DOWN;
            default:
                return null;
        }
    }
    public void go(){
        attack();
        if (moveTime >= 20){
            direction = getRandomDirection();
            moveTime = 0;
        }
        else{
            moveTime += 1;
        }
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
    }
    //隨機發射
    public void attack(){
        Point p = getHeadPoint();
        Random random = new Random();
        int rnum = random.nextInt(350);
        if (rnum < 4){
            this.gamePanel.bulletList.add(new EnemyBullet("image/bulletRed.png", p.x, p.y, this.gamePanel, direction));
        }
    }

    @Override
    public void paintSelft(Graphics g) {
        g.drawImage(img, x, y, 60, 60, null);
        go();
    }

    @Override
    public Rectangle getRect() {
      return new Rectangle(x, y, width, height);
    }
    
}
