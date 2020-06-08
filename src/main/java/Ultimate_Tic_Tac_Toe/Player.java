package Ultimate_Tic_Tac_Toe;

class Player extends Gamer {
    private GameField gameField;

    Player(char sign) {
        this.sign = sign;
    }

    @Override
    boolean shot(int x, int y, int startX, int startY) {
        gameField = GameField.getInstance();
        if (gameField.isCellFree(x, y)) {
            if (x >= startX && x < startX + 3 && y >= startY && y < startY + 3 || GameField.firstMove) {
                System.out.println("Player X - " + x + " Player Y - " + y);
                //System.out.println("Player start point X - " + startX + " Player start point Y - " + startY);
                GameField.smallCell[x][y] = sign;
                GameField.firstMove = false;
                GameField.playersMove = false;
                return true;
            }
        }
        return false;
    }
}