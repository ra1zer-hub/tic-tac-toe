package Ultimate_Tic_Tac_Toe;

abstract class Gamer {
    char sign;

    abstract boolean shot(int x, int y, int startX, int startY);
}