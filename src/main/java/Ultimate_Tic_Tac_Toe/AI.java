package Ultimate_Tic_Tac_Toe;

import java.util.Random;

class AI extends Gamer {
    private GameField gameField;
    private char playerSign;

    AI(char sign, char playerSign) {
        this.sign = sign;
        this.playerSign = playerSign;
    }

    @Override
    boolean shot(int x, int y, int startX, int startY) {
        gameField = GameField.getInstance();
        x = -1;
        y = -1;
        boolean ai_win = false;
        boolean user_win = false;
        // Находим выигрышный ход
        System.out.println(startX + " " + startY);
        for (int i = startX; i < startX + 3; i++) {
            for (int j = startY; j < startY + 3; j++) {
                if (gameField.isCellFree(i, j)) {
                    GameField.smallCell[i][j] = playerSign;
                    if (gameField.checkSmallWin(playerSign, startX, startY)) {
                        x = i;
                        y = j;
                        ai_win = true;
                    }
                    GameField.smallCell[i][j] = gameField.NOT_SIGN;
                }
            }
        }
        // Блокировка хода пользователя, если он побеждает на следующем ходу
            if (!ai_win) {
                for (int i = startX; i < startX + 3; i++) {
                    for (int j = startY; j < startY + 3; j++) {
                        if (gameField.isCellFree(i, j)) {
                            GameField.smallCell[i][j] = sign;
                            if (gameField.checkSmallWin(sign, startX, startY)) {
                                x = i;
                                y = j;
                                user_win = true;
                            }
                            GameField.smallCell[i][j] = gameField.NOT_SIGN;
                        }
                    }
                }
            }
        if (!ai_win && !user_win) {
            do {
                Random rnd = new Random();
                if (GameField.firstMove) {
                    x = rnd.nextInt(9);
                    y = rnd.nextInt(9);
                } else {
                    x = startX + rnd.nextInt(3);
                    y = startY + rnd.nextInt(3);
                }
            }
            while (!gameField.isCellFree(x, y));
        }
        System.out.println("AI X - " + x + " AI Y - " + y);
        GameField.smallCell[x][y] = sign;
        GameField.x = x;
        GameField.y = y;
        GameField.playersMove = true;
        return true;
    }
}