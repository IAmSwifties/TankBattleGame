# Java坦克大戰

## 使用方法
1. 確認已安裝 Java JDK 8 或以上版本。

2. 下載並解壓縮專案資料夾 TankBattleGame-main。

3. 使用以下任一方式執行遊戲：

### 方法一：使用 IDE (Eclipse / IntelliJ IDEA / VS Code)

1. 將專案匯入 IDE。

2. 找到並開啟 GamePanel.java。

3. 執行 main 方法即可啟動遊戲。

### 方法二：使用命令列 (Command Line)

1. 進入專案的 src 目錄，例如：
```cd TankBattleGame-main/src```

2. 編譯程式：
```javac *.java```

3. 執行主程式：
```java GamePanel```

## Game Startup Instructions
1. Make sure Java JDK 8 or later is installed on your system.

2. Download and extract the project folder TankBattleGame-main.

3. Run the game using one of the following methods:

### Method 1: Using an IDE (Eclipse / IntelliJ IDEA / VS Code)

1. Import the project into your IDE.

2. Locate and open GamePanel.java.

3. Run the main method to start the game.

### Method 2: Using the Command Line

1. Navigate to the src directory of the project, e.g.:
```cd TankBattleGame-main/src```


2. Compile the program:
```javac *.java```


3. Run the main class:
```java GamePanel```

## 遊戲規則：
1. 123鍵選擇模式(分別是單人模式、雙人模式、對戰模式)
2. Enter鍵進入遊戲
3. P鍵暫停
---
### 單人模式：
玩家單機與電腦對決，透過WASD鍵移動，F鍵攻擊。

擊敗所有電腦以得勝利。

---
### 雙人合作模式：

二位玩家合作與電腦對決

玩家一利用WASD及F鍵移動攻擊

玩家二利用上下左右即K鍵移動攻擊

擊敗所有電腦以獲得勝利。

---
### 對戰模式：

二位玩家單挑，玩家一利用WASD及F鍵移動攻擊，玩家二利用上下左右即K鍵移動攻擊。

擊敗對方(此模式雙方各有三條命)、摧毀對方的水晶塔(一條命)和獲得10個特殊能量點以獲得勝利。 

此外，此模式中雙方各有一種技能"無限子彈"，即子彈發射無冷卻時間，該技能冷卻時間15秒，請妥善運用。

最後，地圖製作時有特意為大家留下小驚喜，祝大家遊玩愉快

## Game Rules :

Use keys 1, 2, 3 to select a mode (Single-Player, Co-op, Versus).

Press Enter to start the game.

Press P to pause the game.

---
### Single-Player Mode:

The player battles against computer-controlled tanks. Use W/A/S/D to move and F to attack.

Defeat all enemy tanks to achieve victory.

--- 
### Co-op Mode (Two Players vs. AI):

Two players cooperate to fight against computer-controlled tanks.

Player 1: W/A/S/D to move, F to attack.

Player 2: Arrow keys to move, K to attack.

Victory is achieved by defeating all enemy tanks.

---
### Versus Mode (Player vs. Player):

Two players compete against each other.

Player 1: W/A/S/D to move, F to attack.

Player 2: Arrow keys to move, K to attack.

Victory is achieved by: 
1. Defeating the opponent (each player has three lives),

2. Destroying the opponent’s crystal tower (counts as one life)

3. Collecting 10 special energy points.

In addition, each player has a special skill called “Unlimited Bullets”, which removes the shooting cooldown.

(Skill activation: Player 1 – G key; Player 2 – L key.)

The skill has a 15-second cooldown time, so use it wisely.

A little surprise has been hidden in the map design—**enjoy the game!**
