import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class BattlePlayerTwo extends Tank{

    private boolean up = false;
    private boolean left = false;
    private boolean right = false;
    private boolean down = false;

    private int skillCDTime = 15000;
    private boolean skillRequest = true;
    private Timer skillTimer;

    public BattlePlayerTwo(String img, int x, int y, GamePanel gamePanel, String upImg, String leftImg, String rightImg,
            String downImg) {
        super(img, x, y, gamePanel, upImg, leftImg, rightImg, downImg);
        //TODO Auto-generated constructor stub
    }

    @Override
    public void attack(){
		if (attackRequest && alive){
			Point p = this.getHeadPoint();
			Bullet bullet = new BattleBulletTwo("image/battleBullet2.png", p.x, p.y, this.gamePanel, direction);
			this.gamePanel.battleBulletList2.add(bullet);
			// ±Ò°Êthread
			AttackCD attackCD = new AttackCD();
        	attackCD.start();
		}
	}

    public void skill(){
        if (skillRequest && alive){
            skillRequest = false;
            attackCoolDownTime = 100;
            skillTimer = new Timer();
            skillTimer.schedule(new TimerTask() {
                @Override
                public void run(){
                    attackCoolDownTime = 1000;

                    Timer skillCDTimer = new Timer();
                    skillCDTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            skillRequest = true;
                            skillCDTimer.cancel();
                        }
                    }, skillCDTime);

                    skillTimer.cancel();
                }

            }, 2500);
        }
        
    }
    
    public void keyPressed(KeyEvent e){
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_LEFT:
                left = true;
                break;
            case KeyEvent.VK_DOWN:
                down = true;
                break;
            case KeyEvent.VK_RIGHT:
                right = true;
                break;
            case KeyEvent.VK_UP:
                up = true;
                break;
            case KeyEvent.VK_K:
                attack();
                break;
            case KeyEvent.VK_L:
                skill();
            default:
                break;
        }
    }

    public void keyReleased(KeyEvent e){
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_LEFT:
                left = false;
                break;
            case KeyEvent.VK_DOWN:
                down = false;
                break;
            case KeyEvent.VK_RIGHT:
                right = false;
                break;
            case KeyEvent.VK_UP:
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
                this.gamePanel.battlePlayer2Point += 1;
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
