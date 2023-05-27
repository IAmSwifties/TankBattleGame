import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class BattleBulletOne extends Bullet{

    public int reliveCD = 3000;
    private Timer reliveTimer;
    private Tank deadPlayer;

    public BattleBulletOne(String img, int x, int y, GamePanel gamePanel, Direction direction) {
        super(img, x, y, gamePanel, direction);
        //TODO Auto-generated constructor stub
    }

    // hitBattlePlayerTwo
    public void hitPlayer(){
        ArrayList<Tank> players = this.gamePanel.battleList2;
        for (Tank player : players){;
            if (this.getRect().intersects(player.getRect())){
                this.gamePanel.removeList.add(this);
                if (!player.invincible){
                    this.gamePanel.blastList.add(new Blast("", player.x-34, player.y-14, this.gamePanel));
                    this.gamePanel.battleList2.remove(player);
                    player.alive = false;
                
                    this.gamePanel.battlePlayer2Lives -= 1;
                    if (this.gamePanel.battlePlayer2Lives != 0){
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
        deadPlayer.y = 790;
        deadPlayer.direction = Direction.UP;
        deadPlayer.setImg(deadPlayer.upImg);
        reliveTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                gamePanel.battleList2.add(deadPlayer);
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
        ArrayList<Base> baseList = this.gamePanel.baseList2;
        for (Base base : baseList){
            if (this.getRect().intersects(base.getRect())){
                this.gamePanel.baseList2.remove(base);
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
