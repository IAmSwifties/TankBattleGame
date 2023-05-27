import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class BattlePlayerOne extends Tank{

    private boolean up = false;
    private boolean left = false;
    private boolean right = false;
    private boolean down = false;

    public BattlePlayerOne(String img, int x, int y, GamePanel gamePanel, String upImg, String leftImg, String rightImg,
            String downImg) {
        super(img, x, y, gamePanel, upImg, leftImg, rightImg, downImg);
        //TODO Auto-generated constructor stub
    }

    @Override
    public void attack(){
		if (attackRequest && alive){
			Point p = this.getHeadPoint();
			Bullet bullet = new BattleBulletOne("image/battleBullet1.png", p.x, p.y, this.gamePanel, direction);
			this.gamePanel.battleBulletList1.add(bullet);
			// ±Ò°Êthread
			AttackCD attackCD = new AttackCD();
        	attackCD.start();
		}
	}

    public void keyPressed(KeyEvent e){
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_A:
                left = true;
                break;
            case KeyEvent.VK_S:
                down = true;
                break;
            case KeyEvent.VK_D:
                right = true;
                break;
            case KeyEvent.VK_W:
                up = true;
                break;
            case KeyEvent.VK_SPACE:
                attack();
                break;
            default:
                break;
        }
    }

    public void keyReleased(KeyEvent e){
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_A:
                left = false;
                break;
            case KeyEvent.VK_S:
                down = false;
                break;
            case KeyEvent.VK_D:
                right = false;
                break;
            case KeyEvent.VK_W:
                up = false;
                break;
            default:
                break;
        }
    }
    


    public void move(){
        if (left){
            leftWard();
        }
        else if (right){
            rightWard();
        }
        else if (up){
            upWard();
        }
        else if (down){
            downWard();
        }
        hitPoint();
    }

    public void hitPoint(){
        ArrayList<WPoint> wPoints = this.gamePanel.pointList;
        for (WPoint wPoint: wPoints){
            if (this.getRect().intersects(wPoint.getRect())){
                this.gamePanel.pointList.remove(wPoint);
                this.gamePanel.pointCount -= 1;
                this.gamePanel.battlePlayer1Point += 1;
                break;
            }
        }
    }

    @Override
    public void paintSelft(Graphics g) {
        g.drawImage(img, x, y, width, height, null);
        move();
    }

    @Override
    public Rectangle getRect() {
      return new Rectangle(x, y, width, height);
    }
}
