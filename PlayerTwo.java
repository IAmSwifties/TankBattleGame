import java.awt.*;
import java.awt.event.KeyEvent;

public class PlayerTwo extends Tank{

    private boolean up = false;
    private boolean left = false;
    private boolean right = false;
    private boolean down = false;

    public PlayerTwo(String img, int x, int y, GamePanel gamePanel, String upImg, String leftImg, String rightImg,
            String downImg) {
        super(img, x, y, gamePanel, upImg, leftImg, rightImg, downImg);
        
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
