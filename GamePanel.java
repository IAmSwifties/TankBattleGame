import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
public class GamePanel extends JFrame{
    //�w�q���w�s�Ϥ�   �קK�C�����s�R�������ӾɭP���{�{
    Image offScreenImage=null;
    //�����j�p
    int width=800;
    int height=610;
    //���w�Ϥ�
    Image select=Toolkit.getDefaultToolkit().getImage("image/tank2.png");
    Image infintyBullet = Toolkit.getDefaultToolkit().getImage("image/skill/infintyBullet.png");
    //���w��l�a�y��
    int y=165;
    //�C���Ҧ� 0 �C�����}�l 1��H�Ҧ� 2���H�Ҧ� 3�C���Ȱ� 4�C������ 5�C���ӧQ 6��ԼҦ� 
    // 7���a�@�� 8���a�G��
    int state=0;
    int a=1;//���F��ܪ��A�ӥ�

    //���e����
    int count = 0;

    //���W�ĤH�ƶq
    int enemyCount = 0;

    // �n���ƶq
    int pointCount = 0;
    int battlePlayer1Point = 0;
    int battlePlayer2Point = 0;
    
    //��ԼҦ��ͩR��
    int battlePlayer1Lives = 3;
    int battlePlayer2Lives = 3;


    WPoint temp;

    // �l�u�C��(��Kø�s�C�Ӥl�u)
    ArrayList<Bullet> bulletList = new ArrayList<Bullet>();
    ArrayList<Bullet> battleBulletList1 = new ArrayList<Bullet>();
    ArrayList<Bullet> battleBulletList2 = new ArrayList<Bullet>();
    // �ĤH�C��
    ArrayList<Bot> botList = new ArrayList<Bot>();
    // �n�R�����l�u�C��
    ArrayList<Bullet> removeList = new ArrayList<Bullet>(); 
    // ���a�C��
    ArrayList<Tank> playerList = new ArrayList<Tank>();
    ArrayList<Tank> battleList1 = new ArrayList<Tank>();
    ArrayList<Tank> battleList2 = new ArrayList<Tank>();
    // ����C��(�i�}�a)
    ArrayList<Wall> wallList = new ArrayList<Wall>();
    // ����C��(���i�}�a)
    ArrayList<Wall> wallList1 = new ArrayList<Wall>();
    //��a�C��
    ArrayList<Base> baseList = new ArrayList<Base>();
    ArrayList<Base> baseList1 = new ArrayList<Base>();
    ArrayList<Base> baseList2 = new ArrayList<Base>();
    //�z���C��
    ArrayList<Blast> blastList = new ArrayList<Blast>();
    // �n���C��
    ArrayList<WPoint> pointList = new ArrayList<WPoint>();

    // player1
    PlayerOne playerOne = new PlayerOne("image/player1/p1tankU.png", 125, 510, this,
        "image/player1/p1tankU.png", "image/player1/p1tankL.png", 
        "image/player1/p1tankR.png", "image/player1/p1tankD.png");
    // player2
    PlayerTwo playerTwo = new PlayerTwo("image/player2/p2tankU.png", 625, 510, this,
        "image/player2/p2tankU.png", "image/player2/p2tankL.png", 
        "image/player2/p2tankR.png", "image/player2/p2tankD.png");

    BattlePlayerOne battlePlayerOne = new BattlePlayerOne("image/battle1/tankD.png", 465, 200, this,
        "image/battle1/tankU.png", "image/battle1/tankL.png", 
        "image/battle1/tankR.png", "image/battle1/tankD.png");
    
    BattlePlayerTwo battlePlayerTwo = new BattlePlayerTwo("image/battle2/tankU.png", 465, 790, this,
        "image/battle2/tankU.png", "image/battle2/tankL.png", 
        "image/battle2/tankR.png", "image/battle2/tankD.png");

    //Base
    Base base = new Base("image/base.png",365,560,this);

    boolean begin = false; // true�C���}�l false�٦b�D�e��
    boolean pause = false; // true�C���Ȱ� false�C���S���Ȱ�
    boolean end = false; // true�����F false�C���٨S����

    //�����Ұʤ�k
    public void launch(){
        setTitle("�Z�J�j��");
        setSize(width,height);
        //�����~��
        setLocationRelativeTo(null);
        //�K�[���������ƥ�
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //�ϥΪ̤��i�վ�j�p
        setResizable(false);
        setVisible(true);
        //�K�[��L�ʵ���
        this.addKeyListener(new GamePanel.KeyMonitor());
        // �K�[����
        addWalls();
        //�K�[��a
        baseList.add(base);
        //���e
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
            //�C���ӧQ�P�w
            if(botList.size() == 0 && enemyCount == 10){
                state=5;
                end = true;
            }
            //�C�����ѧP�w
            if((playerList.size()==0 && (state==1 || state==2)) || baseList.size()==0){
                state=4;
                end = true;
            }
            // �C���e100���N�[1���ĤH �K�[�q���Z�J
            if (count % 100 == 1 && enemyCount < 10 && (state==1||state==2)){
                // ���ĤH�H���ͦ�
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
            /* 
            wallList.add(new Wall("image/wall.png", 405, 32, this));
            wallList.add(new Wall("image/wall.png", 405, 92, this));
            wallList.add(new Wall("image/wall.png", 465, 92, this));
            wallList.add(new Wall("image/wall.png", 525, 92, this));
            wallList.add(new Wall("image/wall.png", 525, 32, this));
            for (int i = 0; i < 17; i++){
                wallList.add(new Wall("image/wall.png", i * 60, 480, this));
            }
            wallList.add(new Wall("image/wall.png", 405, 930, this));
            wallList.add(new Wall("image/wall.png", 405, 870, this));
            wallList.add(new Wall("image/wall.png", 465, 870, this));
            wallList.add(new Wall("image/wall.png", 525, 870, this));
            wallList.add(new Wall("image/wall.png", 525, 930, this));*/

            wallList.add(new Wall("image/upgrade wall.png", 430, 32, this));
            wallList.add(new Wall("image/upgrade wall.png", 1215, 32, this));

            wallList.add(new Wall("image/upgrade wall.png", 430, 930, this));
            wallList.add(new Wall("image/upgrade wall.png", 1215, 930, this));

            wallList1.add(new Wall("image/upgrade wall.png", 600, 930, this));


            // addbase
            baseList1.clear();
            baseList2.clear();
            baseList1.add(new Base("image/base1.png",465,32,this));
            baseList2.add(new Base("image/base.png",465,930,this));
        }
    }


    @Override
    public void paint(Graphics g){
        //System.out.println(battleBulletList1.size());
        setSize(width,height);
        //�Ыؤ@�Ӹ�����@�ˤj�p���Ϥ�
        if(offScreenImage == null){
            offScreenImage = this.createImage(width,height);
        }
        //��o�ӹϤ����e��
        Graphics gImage = offScreenImage.getGraphics();

        //�I���C��
        gImage.setColor(Color.black);
        //��߯x��
        gImage.fillRect(0, 0, width, height);
        //���ܤ�r�C��
        gImage.setColor(Color.blue);
        //�r��
        gImage.setFont(new Font("�駺",Font.BOLD,50));
        //state=0 ���}�l�C��
        if(state==0){
            
            //�K�[��r
            gImage.drawString("��ܹC���Ҧ�", 220, 100);
            gImage.drawString("��H�Ҧ�", 220, 200);
            gImage.drawString("���H�Ҧ�", 220, 300);
            gImage.drawString("��ԼҦ�", 220, 400);
            //ø�s���w
            gImage.drawImage(select, 170, y, null);
        }else if(state==1||state==2){
            gImage.setFont(new Font("�駺",Font.BOLD,30));
            gImage.setColor(Color.red);
            gImage.drawString("�Ѿl�ĤH: "+botList.size(), 0,65);
            // ���e���a
            for (Tank player: playerList){
                player.paintSelft(gImage);
            }
            // ���e�l�u
            for (Bullet bullet: bulletList){
                bullet.paintSelft(gImage);
            }
            bulletList.removeAll(removeList);
            // ���e�ĤH
            for (Bot bot: botList){
                bot.paintSelft(gImage);
            }
            // ���e����
            for (Wall wall: wallList){
                wall.paintSelft(gImage);
            }
            // ���e��a
            for (Base base: baseList){
                base.paintSelft(gImage);
            }
            // ���e�z��
            for (Blast blast: blastList){
                blast.paintSelft(gImage);
            }
            //���e���� + 1
            count += 1;
        }else if(state==3){
            gImage.drawString("�C���Ȱ�", 220, 200);
            gImage.drawString("��P���~��", 220, 300);
            gImage.drawString("��B��^�D�e��", 220, 400);
        }
        else if(state==4){
            gImage.drawString("�C������", 220, 200);
            gImage.drawString("��B��^�D�e��", 220, 300);
        }
        else if(state==5){
            gImage.drawString("�C���ӧQ�I", 220, 200);
            gImage.drawString("��B��^�D�e��", 220, 300);
        }
        else if (state == 6){
            gImage.setFont(new Font("�駺",Font.BOLD,25));
            gImage.drawString("���a�@�ͩR��: "+ battlePlayer1Lives, 7,53);
            gImage.drawString("�n��: "+ battlePlayer1Point, 7, 88);
            gImage.drawString("���a�G�ͩR��: "+ battlePlayer2Lives, 7,950);
            gImage.drawString("�n��: "+ battlePlayer2Point, 7,985);
            gImage.drawImage(infintyBullet, 800, 30, 40, 40, null);

            // ���e���a
            for (Tank player: battleList1){
                player.paintSelft(gImage);
            }
            for (Tank player: battleList2){
                player.paintSelft(gImage);
            }
            // ���e�l�u
            for (Bullet bullet: battleBulletList1){
                bullet.paintSelft(gImage);
            }
            battleBulletList1.removeAll(removeList);
            for (Bullet bullet: battleBulletList2){
                bullet.paintSelft(gImage);
            }
            battleBulletList2.removeAll(removeList);
            // ���e����
            for (Wall wall: wallList){
                wall.paintSelft(gImage);
            }
            // ���e��a
            for (Base base: baseList1){
                base.paintSelft(gImage);
            }
            for (Base base: baseList2){
                base.paintSelft(gImage);
            }
            // ���e�z��
            for (Blast blast: blastList){
                blast.paintSelft(gImage);
            }
            // �����n��
            for (WPoint wPoint: pointList){
                wPoint.paintSelft(gImage);
            }
            count += 1;
        }
        else if (state == 7){
            gImage.drawString("���a�G�ӧQ�I", 220, 200);
            gImage.drawString("��B��^�D�e��", 220, 300);
        }
        else if (state == 8){
            gImage.drawString("���a�@�ӧQ�I", 220, 200);
            gImage.drawString("��B��^�D�e��", 220, 300);
        }
        //�NoffScreenImage�@���ʵe�J����
        g.drawImage(offScreenImage, 0, 0, null);
    }

    //��L�ʵ���
    class KeyMonitor extends KeyAdapter{
        //���U��L
        @Override
        public void keyPressed(KeyEvent e){
            //��^���
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
                            battlePlayerOne.direction = Direction.DOWN; //�w�]�OUP�A�n�令DOWN
                            battleList2.add(battlePlayerTwo);
                            battlePlayerTwo.alive = true;
                            wallList.clear();
                            addWalls();
                            // �վ�����j�p
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
                    // �T�{�C���}�l�F�S�A�٨S�}�l������Ȱ�
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
                    break;//���T�w�n���n
                case KeyEvent.VK_B:
                    //�ˬd�C���O�_�Ȱ��ε����A�C���S���Ȱ��ε�������B���U�h��������ϬM
                    if (pause || end){
                        state = 0;
                        begin = false;
                        pause = false;
                        end = false;
                        // �N�Ҧ�����k�s(������a�M���A�o�|�Q�P�w���C������)
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
                        // �_�����
                        addWalls();
                        // �_���a
                        baseList.add(base);
                        // �_�쪱�a��l��m
                        playerOne.x = 125;
                        playerOne.y = 510;
                        playerOne.direction = Direction.UP;
                        playerOne.setImg(playerOne.upImg);
                        playerTwo.x = 625;
                        playerTwo.y = 510;
                        playerTwo.direction = Direction.UP;
                        playerTwo.setImg(playerTwo.upImg);


                        battlePlayer1Lives = 3;
                        battlePlayer2Lives = 3;
                        battlePlayerOne.x = 465;
                        battlePlayerOne.y = 200;
                        battlePlayerOne.direction = Direction.DOWN;
                        battlePlayerOne.setImg(battlePlayerOne.downImg);
                        battlePlayerTwo.x = 465;
                        battlePlayerTwo.y = 790;
                        battlePlayerTwo.direction = Direction.UP;
                        battlePlayerTwo.setImg(battlePlayerTwo.upImg);
                        // �վ�����j�p
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

        //�P�}��L
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
