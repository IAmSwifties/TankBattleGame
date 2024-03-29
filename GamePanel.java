import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
public class GamePanel extends JFrame{
    //定義雙緩存圖片   避免每次重新刪掉重劃而導致的閃爍
    Image offScreenImage=null;
    //視窗大小
    int width=800;
    int height=610;
    //指針圖片
    Image select=Toolkit.getDefaultToolkit().getImage("image/tank2.png");
    Image infintyBullet1 = Toolkit.getDefaultToolkit().getImage("image/skill/infintyBullet.png");
    Image infintyBullet2 = Toolkit.getDefaultToolkit().getImage("image/skill/infintyBullet.png");
    Image gojo = Toolkit.getDefaultToolkit().getImage("image/five gojo.jpg");
    Image sokuna = Toolkit.getDefaultToolkit().getImage("image/five sokuna.jpg");

    //指針初始縱座標
    int y=165;
    //遊戲模式 0 遊戲未開始 1單人模式 2雙人模式 3遊戲暫停 4遊戲失敗 5遊戲勝利 6對戰模式 
    // 7玩家一輸 8玩家二輸
    int state=0;
    int a=1;//為了選擇狀態而用

    //重畫次數
    int count = 0;

    //場上敵人數量
    int enemyCount = 0;

    // 積分數量
    int pointCount = 0;
    int battlePlayer1Point = 0;
    int battlePlayer2Point = 0;
    
    //對戰模式生命數
    int battlePlayer1Lives = 3;
    int battlePlayer2Lives = 3;

    // 技能相關
    WPoint temp;

    int battlePlayer1SkillCDTime = 16000;
    int battlePlayer2SkillCDTime = 16000;





    // 子彈列表(方便繪製每個子彈)
    ArrayList<Bullet> bulletList = new ArrayList<Bullet>();
    ArrayList<Bullet> battleBulletList1 = new ArrayList<Bullet>();
    ArrayList<Bullet> battleBulletList2 = new ArrayList<Bullet>();
    // 敵人列表
    ArrayList<Bot> botList = new ArrayList<Bot>();
    // 要刪除的子彈列表
    ArrayList<Bullet> removeList = new ArrayList<Bullet>(); 
    // 玩家列表
    ArrayList<Tank> playerList = new ArrayList<Tank>();
    ArrayList<Tank> battleList1 = new ArrayList<Tank>();
    ArrayList<Tank> battleList2 = new ArrayList<Tank>();
    // 圍牆列表(可破壞)
    ArrayList<Wall> wallList = new ArrayList<Wall>();
    // 圍牆列表(不可破壞)
    ArrayList<Wall> wallList1 = new ArrayList<Wall>();
    //基地列表
    ArrayList<Base> baseList = new ArrayList<Base>();
    ArrayList<Base> baseList1 = new ArrayList<Base>();
    ArrayList<Base> baseList2 = new ArrayList<Base>();
    //爆炸列表
    ArrayList<Blast> blastList = new ArrayList<Blast>();
    // 積分列表
    ArrayList<WPoint> pointList = new ArrayList<WPoint>();

    // player1
    PlayerOne playerOne = new PlayerOne("image/player1/p1tankU.png", 125, 510, this,
        "image/player1/p1tankU.png", "image/player1/p1tankL.png", 
        "image/player1/p1tankR.png", "image/player1/p1tankD.png");
    // player2
    PlayerTwo playerTwo = new PlayerTwo("image/player2/p2tankU.png", 625, 510, this,
        "image/player2/p2tankU.png", "image/player2/p2tankL.png", 
        "image/player2/p2tankR.png", "image/player2/p2tankD.png");

    BattlePlayerOne battlePlayerOne = new BattlePlayerOne("image/battle1/tankR.png", 190, 465, this,
        "image/battle1/tankU.png", "image/battle1/tankL.png", 
        "image/battle1/tankR.png", "image/battle1/tankD.png");
    
    BattlePlayerTwo battlePlayerTwo = new BattlePlayerTwo("image/battle2/tankL.png", 1470, 465, this,
        "image/battle2/tankU.png", "image/battle2/tankL.png", 
        "image/battle2/tankR.png", "image/battle2/tankD.png");

    //Base
    Base base = new Base("image/base.png",365,560,this);

    boolean begin = false; // true遊戲開始 false還在主畫面
    boolean pause = false; // true遊戲暫停 false遊戲沒有暫停
    boolean end = false; // true結束了 false遊戲還沒結束

    //視窗啟動方法
    public void launch(){
        setTitle("坦克大戰");
        setSize(width,height);
        //視窗居中
        setLocationRelativeTo(null);
        //添加關閉視窗事件
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //使用者不可調整大小
        setResizable(false);
        setVisible(true);
        //添加鍵盤監視器
        this.addKeyListener(new GamePanel.KeyMonitor());
        // 添加圍牆
        addWalls();
        //添加基地
        baseList.add(base);
        //重畫
        while(true){
            //System.out.println(wallList.size());
            if (state == 6){
                if (battlePlayer1Lives == 0 || baseList1.isEmpty() || battlePlayer2Point == 10){
                    state = 7;
                    end = true;
                }
                else if (battlePlayer2Lives == 0 || baseList2.isEmpty()|| battlePlayer1Point == 10){
                    state = 8;
                    end = true;
                }

                if (count % 100 == 1 && pointCount < 3){
                    Random random = new Random();
                    int numX = random.nextInt(785) + 430;
                    int numY = random.nextInt(897) + 32;
                    temp = new WPoint("image/w_point.png", numX, numY, this);
                    if (!temp.hitWalls()){
                        pointList.add(temp);
                        pointCount += 1;
                    }
                }
            }
            //遊戲勝利判定
            if(botList.size() == 0 && enemyCount == 10){
                state=5;
                end = true;
            }
            //遊戲失敗判定
            if((playerList.size()==0 && (state==1 || state==2)) || baseList.size()==0){
                state=4;
                end = true;
            }
            // 每重畫100次就加1隻敵人 添加電腦坦克
            if (count % 100 == 1 && enemyCount < 10 && (state==1||state==2)){
                // 讓敵人隨機生成
                Random random = new Random();
                int rnum = random.nextInt(800); 
                botList.add(new Bot("image/enemy/enemy1U.png", rnum, 110, this,
                "image/enemy/enemy1U.png", "image/enemy/enemy1L.png", 
                "image/enemy/enemy1R.png", "image/enemy/enemy1D.png"));
                enemyCount += 1;
            }
            
            

            repaint();
            try{
                Thread.sleep(25);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

    }
    //wallList1為不可破壞，wallList打代表可破壞
    public void addWalls(){
        if (state != 6){
            for (int i = 0; i < 14; i++){
            wallList.add(new Wall("image/wall.png", i*60, 170, this));
            }
            wallList.add(new Wall("image/wall.png", 305, 560, this));
            wallList.add(new Wall("image/wall.png", 305, 500, this));
            wallList.add(new Wall("image/wall.png", 365, 500, this));
            wallList.add(new Wall("image/wall.png", 425, 500, this));
            wallList.add(new Wall("image/wall.png", 425, 560, this));
        }
        else{
            //the wall to protect base1
            wallList.add(new Wall("image/wall.png", 10, 405, this));
            wallList.add(new Wall("image/wall.png", 70, 405, this));
            wallList.add(new Wall("image/wall.png", 70, 465, this));
            wallList.add(new Wall("image/wall.png", 70, 525, this));
            wallList.add(new Wall("image/wall.png", 10, 525, this));
            //the wall to protect base2 
            wallList.add(new Wall("image/wall.png", 1630, 405, this));
            wallList.add(new Wall("image/wall.png", 1570, 405, this));
            wallList.add(new Wall("image/wall.png", 1570, 465, this));
            wallList.add(new Wall("image/wall.png", 1570, 525, this));
            wallList.add(new Wall("image/wall.png", 1630, 525, this));
            //the unbreakable wall below the base1
            for(int i = 0; i < 5; i++){
                wallList1.add(new Wall("image/upgrade wall.png", 10 + i * 60, 225, this));
            }
            //the unbreakable wall on the top of the base1
            for(int i = 0; i < 5; i++){
                wallList1.add(new Wall("image/upgrade wall.png", 10 + i * 60, 705, this));
            }
            //the unbreakable wall in front of the base1
            for(int i = 0; i < 5; i++){
                wallList1.add(new Wall("image/upgrade wall.png", 310, 345 + (i * 60), this));
            }
            //the unbreakable wall below the base2
            for(int i = 0; i < 5; i++){
                wallList1.add(new Wall("image/upgrade wall.png", 1630 - (i * 60), 225, this));
            }
            //the unbreakable wall on the top of the base2
            for(int i = 0; i < 5; i++){
                wallList1.add(new Wall("image/upgrade wall.png", 1630 - (i * 60), 705, this));
            }
            //the unbreakable wall in front of the base2
            for(int i = 0; i < 5; i++){
                wallList1.add(new Wall("image/upgrade wall.png", 1330, 345 + (i * 60), this));
            }
            //the breakable wall in front of the base1
            for(int i = 0; i < 7; i++){
                wallList.add(new Wall("image/wall.png", 310, 345 - (i * 60), this));
            }
            for(int i = 0; i < 6; i++){
                wallList.add(new Wall("image/wall.png", 310, 645 + (i * 60), this));
            }
            //the breakable wall in front of the base2
            for(int i = 0; i < 7; i++){
                wallList.add(new Wall("image/wall.png", 1330, 345 - (i * 60), this));
            }
            for(int i = 0; i < 6; i++){
                wallList.add(new Wall("image/wall.png", 1330, 645 + (i * 60), this));
            }
            //the unbreakable wall in the middle of the map
            for(int i = 0; i < 6; i++){
                wallList1.add(new Wall("image/upgrade wall.png", 830, 285 - (i * 60), this));
            }
            for(int i = 0; i < 5; i++){
                wallList1.add(new Wall("image/upgrade wall.png", 830, 705 + (i * 60), this));
            }

            //the unbreakable wall in the left and right side
            for(int i = 0; i < 7; i++){
                wallList1.add(new Wall("image/upgrade wall.png", 580, 285 + (i * 60), this));
            }
            for(int i = 0; i < 7; i++){
                wallList1.add(new Wall("image/upgrade wall.png", 1090, 285 + (i * 60), this));
            }
            //the breakable 
            for(int i = 0; i < 8; i++){
                wallList.add(new Wall("image/wall.png", 370 + (i * 60), 765, this));
            }
            for(int i = 0; i < 8; i++){
                wallList.add(new Wall("image/wall.png", 1270 - (i * 60), 765, this));
            }
            
             for(int i = 0; i < 1; i++){
                 wallList.add(new Wall("image/wall.png", 520 , 458, this));
             }
            for(int i = 0; i < 2; i++){
                wallList.add(new Wall("image/wall.png", 1130 + (i * 60), 458, this));
            }
            //line 5 6
            for(int i = 0; i < 10; i++){
                wallList.add(new Wall("image/wall.png", 460, 165+ (i * 60), this));
            }
            for(int i = 0; i < 10; i++){
                wallList.add(new Wall("image/wall.png", 1213, 165+ (i * 60), this));
            }
            //the breakable wall 
            for(int i = 0; i < 8; i++){
                wallList.add(new Wall("image/wall.png", 370 + (i * 60), 165, this));
            }
            for(int i = 0; i < 8; i++){
                wallList.add(new Wall("image/wall.png", 1270 - (i * 60), 165, this));
            }

            //line 7 8 9 10
            for(int i = 0; i < 4; i++){
                wallList.add(new Wall("image/wall.png", 710, 165 + (i * 60), this));
            }
            for(int i = 0; i < 4; i++){
                wallList.add(new Wall("image/wall.png",955, 165 + (i * 60), this));
            }
            for(int i = 0; i < 3; i++){
                wallList.add(new Wall("image/wall.png", 710, 592 + (i * 60), this));
            }
            for(int i = 0; i < 3; i++){
                wallList.add(new Wall("image/wall.png",955, 592 + (i * 60), this));
            }
            //line 14 11
            for(int i = 0; i < 2; i++){
                wallList.add(new Wall("image/wall.png",618 + (i * 60), 345 , this));
            }
            for(int i = 0; i < 2; i++){
                wallList.add(new Wall("image/wall.png",970 + (i * 60), 345 , this));
            }
            //line 12 13
            for(int i = 0; i < 2; i++){
                wallList.add(new Wall("image/wall.png",615 + (i * 60), 592 , this));
            }
            for(int i = 0; i < 2; i++){
                wallList.add(new Wall("image/wall.png",970 + (i * 60), 592 , this));
            }
            //the unbreakable wall in the top and bottom side
            for(int i = 0; i < 3; i++){
                wallList1.add(new Wall("image/upgrade wall.png",580 , 825 + (i * 60) , this));
            }
            for(int i = 0; i < 3; i++){
                wallList1.add(new Wall("image/upgrade wall.png",1090 , 825 + (i * 60) , this));
            }
            for(int i = 0; i < 3; i++){
                wallList1.add(new Wall("image/upgrade wall.png",580 , 5 + (i * 50) , this));
            }
            for(int i = 0; i < 3; i++){
                wallList1.add(new Wall("image/upgrade wall.png",1090 , 5 + (i * 50) , this));
            }

            // add base
            baseList1.clear();
            baseList2.clear();
            baseList1.add(new Base("image/base1.png",10,465,this));
            baseList2.add(new Base("image/base.png",1630,465,this));
        }
    }


    @Override
    public void paint(Graphics g){
        //System.out.println(battleBulletList1.size());
        setSize(width,height);
        //創建一個跟視窗一樣大小的圖片
        if(offScreenImage == null){
            offScreenImage = this.createImage(width,height);
        }
        //獲得該圖片的畫筆
        Graphics gImage = offScreenImage.getGraphics();

        //背景顏色
        gImage.setColor(Color.black);
        //實心矩形
        gImage.fillRect(0, 0, width, height);
        //改變文字顏色
        gImage.setColor(Color.blue);
        //字體
        gImage.setFont(new Font("仿宋",Font.BOLD,50));
        //state=0 未開始遊戲
        if(state==0){
            //添加文字
            gImage.drawString("選擇遊戲模式", 220, 100);
            gImage.drawString("單人模式", 220, 200);
            gImage.drawString("雙人模式", 220, 300);
            gImage.drawString("對戰模式", 220, 400);
            //繪製指針
            gImage.drawImage(select, 170, y, null);
        }else if(state==1||state==2){
            gImage.setFont(new Font("仿宋",Font.BOLD,30));
            gImage.setColor(Color.red);
            gImage.drawString("剩餘敵人: "+botList.size(), 0,65);
            // 重畫玩家
            for (Tank player: playerList){
                player.paintSelft(gImage);
            }
            // 重畫子彈
            for (Bullet bullet: bulletList){
                bullet.paintSelft(gImage);
            }
            bulletList.removeAll(removeList);
            // 重畫敵人
            for (Bot bot: botList){
                bot.paintSelft(gImage);
            }
            // 重畫圍牆
            for (Wall wall: wallList){
                wall.paintSelft(gImage);
            }
            for (Wall wall: wallList1){
                wall.paintSelft(gImage);
            }
            // 重畫基地
            for (Base base: baseList){
                base.paintSelft(gImage);
            }
            // 重畫爆炸
            for (Blast blast: blastList){
                blast.paintSelft(gImage);
            }
            //重畫次數 + 1
            count += 1;
        }else if(state==3){
            gImage.drawString("遊戲暫停", 220, 200);
            gImage.drawString("按P鍵繼續", 220, 300);
            gImage.drawString("按B鍵回主畫面", 220, 400);
        }
        else if(state==4){
            gImage.drawString("遊戲失敗", 220, 200);
            gImage.drawString("按B鍵回主畫面", 220, 300);
        }
        else if(state==5){
            gImage.drawString("遊戲勝利！", 220, 200);
            gImage.drawString("按B鍵回主畫面", 220, 300);
        }
        else if (state == 6){
            gImage.setColor(Color.red);
            gImage.setFont(new Font("仿宋",Font.BOLD,25));
            gImage.drawString("玩家一生命數: "+ battlePlayer1Lives, 7,53);
            gImage.drawString("積分: "+ battlePlayer1Point, 7, 88);
            gImage.drawString("玩家二生命數: "+ battlePlayer2Lives, 1500,53);
            gImage.drawString("積分: "+ battlePlayer2Point, 1605,88);
            gImage.drawImage(infintyBullet1, 200, 30, 40, 40, null);
            gImage.drawImage(infintyBullet2, 1455, 30, 40, 40, null);

            gImage.drawImage(gojo, 60, 780, 160, 200, null);
            gImage.drawImage(sokuna, 1480, 780, 160, 200, null);

            if (!battlePlayerOne.skillRequest){
                gImage.setColor(Color.green);
                gImage.drawString(Integer.toString(battlePlayer1SkillCDTime / 1000), 205,68);
                battlePlayer1SkillCDTime -= 25;
                if (battlePlayer1SkillCDTime < 0){
                    battlePlayer1SkillCDTime = 16000;
                    battlePlayerOne.skillRequest = true;
                }
            }
            if (!battlePlayerTwo.skillRequest){
                gImage.setColor(Color.green);
                gImage.drawString(Integer.toString(battlePlayer2SkillCDTime / 1000), 1462,68);
                battlePlayer2SkillCDTime -= 25;
                if (battlePlayer2SkillCDTime < 0){
                    battlePlayer2SkillCDTime = 16000;
                    battlePlayerTwo.skillRequest = true;
                }
            }

            // 重畫玩家
            for (Tank player: battleList1){
                player.paintSelft(gImage);
            }
            for (Tank player: battleList2){
                player.paintSelft(gImage);
            }
            // 重畫子彈
            for (Bullet bullet: battleBulletList1){
                bullet.paintSelft(gImage);
            }
            battleBulletList1.removeAll(removeList);
            for (Bullet bullet: battleBulletList2){
                bullet.paintSelft(gImage);
            }
            battleBulletList2.removeAll(removeList);
            // 重畫圍牆
            for (Wall wall: wallList){
                wall.paintSelft(gImage);
            }
            for (Wall wall: wallList1){
                wall.paintSelft(gImage);
            }
            // 重畫基地
            for (Base base: baseList1){
                base.paintSelft(gImage);
            }
            for (Base base: baseList2){
                base.paintSelft(gImage);
            }
            // 重畫爆炸
            for (Blast blast: blastList){
                blast.paintSelft(gImage);
            }
            // 重劃積分
            for (WPoint wPoint: pointList){
                wPoint.paintSelft(gImage);
            }
            count += 1;
        }
        else if (state == 7){
            gImage.drawString("玩家二勝利！", 220, 200);
            gImage.drawString("按B鍵回主畫面", 220, 300);
        }
        else if (state == 8){
            gImage.drawString("玩家一勝利！", 220, 200);
            gImage.drawString("按B鍵回主畫面", 220, 300);
        }
        //將offScreenImage一次性畫入視窗
        g.drawImage(offScreenImage, 0, 0, null);
    }

    //鍵盤監視器
    class KeyMonitor extends KeyAdapter{
        //按下鍵盤
        @Override
        public void keyPressed(KeyEvent e){
            //返回鍵值
            int key=e.getKeyCode();
            switch(key){
                case KeyEvent.VK_1:
                    if (!begin){
                        a=1;
                        y=165; 
                    }
                    break;
                case KeyEvent.VK_2:
                    if (!begin){
                        a=2;
                        y=265;
                    }
                    break;
                case KeyEvent.VK_3:
                    if (!begin){
                        a = 6;
                        y = 365;
                    }
                    break;
                case KeyEvent.VK_ENTER:
                    if (!begin){
                        state=a;
                        if (state == 1){
                            playerList.add(playerOne);
                            playerOne.alive=true;
                        }
                        // playerTwo
                        else if(state==2){
                            playerList.add(playerOne);
                            playerOne.alive=true;
                            playerList.add(playerTwo);
                            playerTwo.alive=true;
                        }
                        else if (state == 6){
                            battleList1.add(battlePlayerOne);
                            battlePlayerOne.alive = true;
                            battlePlayerOne.direction = Direction.RIGHT; //預設是UP，要改成RIGHT
                            battleList2.add(battlePlayerTwo);
                            battlePlayerTwo.alive = true;
                            battlePlayerTwo.direction = Direction.LEFT;
                            wallList.clear();
                            addWalls();
                            // 調整視窗大小
                            width = 1700;
                            height = 1000;
                            setSize(width,height);
                            setLocationRelativeTo(null);
                            offScreenImage = null;
                        }
                        begin = true;
                        
                    }
                    break;
                case KeyEvent.VK_P:
                    // 確認遊戲開始了沒，還沒開始不能按暫停
                    if (state != 0){
                        if(state!=3){
                            a=state;
                            state=3;
                            pause = true;
                        }
                        else{
                            state=a;
                            if(a==0){
                                a=1;
                            }
                            pause = false;
                        }
                    }
                    break;//不確定要不要
                case KeyEvent.VK_B:
                    //檢查遊戲是否暫停或結束，遊戲沒有暫停或結束的話B按下去不做任何反映
                    if (pause || end){
                        state = 0;
                        begin = false;
                        pause = false;
                        end = false;
                        // 將所有資料歸零(不能把基地清掉，這會被判定為遊戲失敗)
                        count = 0;
                        enemyCount = 0;
                        pointCount = 0;
                        battlePlayer1Point = 0;
                        battlePlayer2Point = 0;
                        bulletList.clear();
                        battleBulletList1.clear();
                        battleBulletList2.clear();
                        botList.clear();
                        removeList.clear();
                        playerList.clear();
                        wallList.clear();
                        wallList1.clear();
                        baseList.clear();
                        baseList1.clear();
                        baseList2.clear();
                        blastList.clear();
                        battleList1.clear();
                        battleList2.clear();
                        pointList.clear();
                        // 復原牆壁
                        addWalls();
                        // 復原基地
                        baseList.add(base);
                        // 復原玩家初始位置
                        playerOne.x = 125;
                        playerOne.y = 510;
                        playerOne.direction = Direction.UP;
                        playerOne.setImg(playerOne.upImg);
                        playerTwo.x = 625;
                        playerTwo.y = 510;
                        playerTwo.direction = Direction.UP;
                        playerTwo.setImg(playerTwo.upImg);


                        battlePlayer1Point = 0;
                        battlePlayer2Point = 0;
                        battlePlayer1Lives = 3;
                        battlePlayer2Lives = 3;
                        battlePlayerOne.alive = true;
                        battlePlayerTwo.alive = true;
                        battlePlayerOne.x = 190;
                        battlePlayerOne.y = 465;
                        battlePlayerOne.direction = Direction.RIGHT;
                        battlePlayerOne.setImg(battlePlayerOne.rightImg);
                        battlePlayerTwo.x = 1470;
                        battlePlayerTwo.y = 465;
                        battlePlayerTwo.direction = Direction.LEFT;
                        battlePlayerTwo.setImg(battlePlayerTwo.leftImg);
                        // 調整視窗大小
                        width = 800;
                        height = 610;
                        setSize(width,height);
                        setLocationRelativeTo(null);
                        offScreenImage = null;
                    }
                    break;
                default:
                //if (begin)
                    if (state == 1){
                        playerOne.keyPressed(e);
                    }
                    else if (state == 2){
                        playerOne.keyPressed(e);
                        playerTwo.keyPressed(e);
                    }
                    else if (state == 6){
                        battlePlayerOne.keyPressed(e);
                        battlePlayerTwo.keyPressed(e);
                    }
            }
        }

        //鬆開鍵盤
        public void keyReleased(KeyEvent e){
            
            if (state == 1){
                playerOne.keyReleased(e);
            }
            else if (state == 2){
                playerOne.keyReleased(e);
                playerTwo.keyReleased(e);
            }
            else if (state == 6){
                battlePlayerOne.keyReleased(e);
                battlePlayerTwo.keyReleased(e);
            }
            else {
                playerOne.keyReleased(e);
                playerTwo.keyReleased(e);
                battlePlayerOne.keyReleased(e);
                battlePlayerTwo.keyReleased(e);
            }
        }

    }



    public static void main(String[] args) {
        GamePanel gp=new GamePanel();
        gp.launch();
    }
}
//owo