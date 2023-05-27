import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.ArrayList;

public class Tank extends GameObject{
    // 尺寸
    public int width = 50;
    public int height = 50;

    // 速度
    private int speed = 3;

    // 方向
    public Direction direction = Direction.UP;

	//生命
	public boolean invincible = false;
	public boolean alive = false;
	public int lives = 3;

    // 四個方向的圖片
    public String upImg;
    public String leftImg;
    public String rightImg;
    public String downImg;

	// 子彈發射請求(true代表可以attack)
	public boolean attackRequest = true;
	// 子彈發射冷卻時間(ms)
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
			// 啟動thread
			AttackCD attackCD = new AttackCD();
        	attackCD.start();
		}
	}

	class AttackCD extends Thread{
		private volatile boolean running = true;
		public void run(){
			while (running){
				// 將請求改成false(代表不能attack)
				attackRequest = false;
				// 冷卻1秒
				try {
					Thread.sleep(attackCoolDownTime);
				} catch (Exception e) {
					e.printStackTrace();
				}
				// 把request改回來
				attackRequest = true;
				// 終止thread
				stopThread();
			}
		}
		public void stopThread() {
			running = false;
		}
	}
	
	//為了發射子彈須尋找坦克頭部位置
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
	//與圍牆碰撞檢測
	public boolean hitWall(int x, int y){
		ArrayList<Wall> walls = this.gamePanel.wallList;
		Rectangle next = new Rectangle(x, y, width, height);
		for (Wall wall: walls){
			if (next.intersects(wall.getRect())){//碰撞檢測
				return true;
			}
		}
		return false;
	}
	//判斷是否到達邊界
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