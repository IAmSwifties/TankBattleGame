import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class BattleBulletTwo extends Bullet{

    public int reliveCD = 3000;
    private Timer reliveTimer;
    private Tank deadPlayer;

    public BattleBulletTwo(String img, int x, int y, GamePanel gamePanel, Direction direction) {
        super(img, x, y, gamePanel, direction);
        //TODO Auto-generated constructor stub
    }

    public void hitPlayer(){
        ArrayList<Tank> players = this.gamePanel.battleList1;
        for (Tank player : players){;
            if (this.getRect().intersects(player.getRect())){
                this.gamePanel.removeList.add(this);
                if (!player.invincible){
                    this.gamePanel.blastList.add(new Blast("", player.x-34, player.y-14, this.gamePanel));
                    this.gamePanel.battleList1.remove(player);
                    player.alive = false;

                    this.gamePanel.battlePlayer1Lives -= 1;
                    if (this.gamePanel.battlePlayer1Lives != 0){
                        // 復活
                        relive(player);
                    }
                }
                break;
            }
        }
    }

    public void relive(Tank player){
        deadPlayer = player;
        reliveTimer = new Timer();
        deadPlayer.x = 465;
        deadPlayer.y = 200;
        deadPlayer.direction = Direction.DOWN;
        deadPlayer.setImg(deadPlayer.downImg);
        reliveTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                gamePanel.battleList1.add(deadPlayer);
                deadPlayer.invincible = true;
                deadPlayer.alive = true;
                // 一秒後取消無敵狀態
                Timer invincibleTimer = new Timer();
                invincibleTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        deadPlayer.invincible = false;
                        invincibleTimer.cancel();
                    }
                }, 1000);
                reliveTimer.cancel();
            }
        }, reliveCD);
    }

    //子彈於基地碰撞測試
    @Override
    public void hitBase(){
        ArrayList<Base> baseList = this.gamePanel.baseList1;
        for (Base base : baseList){
            if (this.getRect().intersects(base.getRect())){
                this.gamePanel.baseList1.remove(base);
                this.gamePanel.removeList.add(this);
                break;
            }
        }
    }

    public void paintSelft(Graphics g) {
        g.drawImage(img, x, y, 10, 10, null);
        this.go();
        this.hitPlayer();
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, width, height);
    }
}
