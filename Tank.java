import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.ArrayList;

public class Tank extends GameObject{
    // �ؤo
    public int width = 50;
    public int height = 50;

    // �t��
    private int speed = 3;

    // ��V
    public Direction direction = Direction.UP;

	//�ͩR
	public boolean invincible = false;
	public boolean alive = false;
	public int lives = 3;

    // �|�Ӥ�V���Ϥ�
    public String upImg;
    public String leftImg;
    public String rightImg;
    public String downImg;

	// �l�u�o�g�ШD(true�N��i�Hattack)
	public boolean attackRequest = true;
	// �l�u�o�g�N�o�ɶ�(ms)
	public int attackCoolDownTime = 1000;


    public Tank(String img, int x, int y, GamePanel gamePanel, String upImg, String leftImg, String rightImg, 
			String downImg) {
        super(img, x, y, gamePanel);
        this.upImg = upImg;
        this.leftImg = leftImg;
        this.rightImg = rightImg;
        this.downImg = downImg;
    }

	public void leftWard(){
		setImg(leftImg);
		direction = Direction.LEFT;
		if (!hitWall(x - speed, y) && !moveToBorder(x - speed, y)){
			x -= speed;
		}
	}

	public void upWard(){
		setImg(upImg);
		direction = Direction.UP;
		if (!hitWall(x, y - speed) && !moveToBorder(x, y - speed)){
			y -= speed;
		}
	}

	public void rightWard(){
		setImg(rightImg);
		direction = Direction.RIGHT;
		if (!hitWall(x + speed, y) && !moveToBorder(x + speed, y)){
			x += speed;
		}
	}

	public void downWard(){
		setImg(downImg);
		direction = Direction.DOWN;
		if (!hitWall(x, y + speed) && !moveToBorder(x, y + speed)){
			y += speed;
		}
	}

	public void setImg(String img){
		this.img = Toolkit.getDefaultToolkit().getImage(img);
	}


	public void attack(){
		if (attackRequest && alive){
			Point p = this.getHeadPoint();
			Bullet bullet = new Bullet("image/bulletGreen.png", p.x, p.y, this.gamePanel, direction);
			this.gamePanel.bulletList.add(bullet);
			// �Ұ�thread
			AttackCD attackCD = new AttackCD();
        	attackCD.start();
		}
	}

	class AttackCD extends Thread{
		private volatile boolean running = true;
		public void run(){
			while (running){
				// �N�ШD�令false(�N����attack)
				attackRequest = false;
				// �N�o1��
				try {
					Thread.sleep(attackCoolDownTime);
				} catch (Exception e) {
					e.printStackTrace();
				}
				// ��request��^��
				attackRequest = true;
				// �פ�thread
				stopThread();
			}
		}
		public void stopThread() {
			running = false;
		}
	}
	
	//���F�o�g�l�u���M��Z�J�Y����m
	public Point getHeadPoint(){
		switch (direction) {
			case LEFT:
				return new Point(x, y + (height / 2) - 5);
			case RIGHT:
				return new Point(x + width, y + (height / 2) - 5);
			case UP:
				return new Point(x + (width / 2) - 5, y);
			case DOWN:
				return new Point(x + (width / 2) - 5, y + height);
		}
		return null;
	}
	//�P����I���˴�
	public boolean hitWall(int x, int y){
		ArrayList<Wall> walls = this.gamePanel.wallList;
		Rectangle next = new Rectangle(x, y, width, height);
		for (Wall wall: walls){
			if (next.intersects(wall.getRect())){//�I���˴�
				return true;
			}
		}
		return false;
	}
	//�P�_�O�_��F���
	public boolean moveToBorder(int x, int y){
		if (x < 0){
			return true;
		}
		else if (x + width > this.gamePanel.getWidth()){
			return true;
		}
		else if (y < 0){
			return true;
		}
		else if (y + height > this.gamePanel.getHeight()){
			return true;
		}
		return false;
	}

    @Override
    public void paintSelft(Graphics g) {
		g.drawImage(img, x, y, width, height, null);
		
    }

    @Override
    public Rectangle getRect() {
      return null;
    }
  
}