package Ultimate_Tic_Tac_Toe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class GameField extends JPanel {
    private Image imageCircle;
    private Image imageCross;
    private Image whiteGreen;
    private static GameField gameField;
    private Color purple = new Color(148, 0, 211);
    private static final int CELL_SIZE = 90;
    private static final int BIG_CELL_SIZE = 270;
    private static final int GAME_PANEL = 810;
    private static final int BIG_LINES_COUNT = 3;
    private static final int LINES_COUNT = 9;
    private static final int CROSS_SIZE = 270;
    private static final int CIRCLE_SIZE = 260;
    private static final int IMAGE_SIZE = 75;
    private static final int INDENT_12 = 12;
    private static final int INDENT_14 = 14;
    private static final int SIGN_TO_WIN = 3;
    final char NOT_SIGN = '*';
    static char[][] smallCell;
    private static char[][] bigCell;
    static int x, y;
    private int nextFieldX, nextFieldY; // Указывают откуда делать проверку на заполненность поля
    private int startPointNextTurnX, startPointNextTurnY; // Указывают поле для следующего хода
    private static int fieldX, fieldY; // Указывают поле для bigCell
    private static int startX, startY; // Указывают где делать проверку на победу
    private boolean gameOver = false;
    private String gameOverMessage;
    static boolean firstMove = true;
    static boolean playersMove = false;

    static synchronized GameField getInstance() {
        if (gameField == null)
            gameField = new GameField();
        return gameField;
    }

    GameField() {
        imageCircle = new ImageIcon(getClass().getResource("/Image/Circle.png")).getImage();
        imageCross = new ImageIcon(getClass().getResource("/Image/Cross.png")).getImage();
        whiteGreen = new ImageIcon(getClass().getResource("/Image/white-green.png")).getImage();

        if (smallCell == null)
            startGame();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                x = e.getX() / CELL_SIZE;
                y = e.getY() / CELL_SIZE;
                if (!gameOver) {
                    modeAgainstAI();
                }
            }
        });
        setVisible(true);
    }

    private void modeAgainstAI() {
        Player player = new Player('X');
        AI ai = new AI('O', player.sign);
        if (!gameOver) {
            if (player.shot(x, y, nextFieldX, nextFieldY)) {
                checkEndGame(player.sign);
                repaint();
                if (!gameOver) {
                    if (ai.shot(x, y, nextFieldX, nextFieldY)) {
                        checkEndGame(ai.sign);
                        repaint();
                    }
                }
            }
        }
    }

    void startGame() {
        smallCell = new char[LINES_COUNT][LINES_COUNT];
        bigCell = new char[BIG_LINES_COUNT][BIG_LINES_COUNT];
        repaint();
        firstMove = true;
        gameOver = false;
        gameOverMessage = null;
        playersMove = false;
        for (int i = 0; i < LINES_COUNT; i++) {
            for (int j = 0; j < LINES_COUNT; j++) {
                smallCell[i][j] = '*';
            }
        }
        for (int i = 0; i < BIG_LINES_COUNT; i++) {
            for (int j = 0; j < BIG_LINES_COUNT; j++) {
                bigCell[i][j] = '*';
            }
        }
        setVisible(true);
    }

    private void checkEndGame(char sign) {
        fromSmallCellInBigCell();
        if (firstMove)
            methodThatAssignsValuesToVariables();
        System.out.println("Проверка выигриша будет происходить в поле " + fieldX + " " + fieldY + " с клетки " + startX + " " + startY);
        if (bigCell[fieldX][fieldY] == NOT_SIGN) {
            System.out.println("Началась проверка выигриша с клеток " + startX + " " + startY);
            if (checkSmallWin(sign, startX, startY)) {
                System.out.println("ПОЛЕ СТАЛО ВЫИГРИШНЫМ!!!");
                bigCell[fieldX][fieldY] = sign;
            }
        }
        if (checkWin(sign)) {
            gameOver = true;
            switch (sign) {
                case 'X':
                    gameOverMessage = "YOU WIN!!!";
                    break;
                case 'O':
                    gameOverMessage = "YOU DEAD!!!";
                    break;
            }
        }
        methodThatAssignsValuesToVariables();
        System.out.println("Проверка заполнености поля идет с " + nextFieldX + " " + nextFieldY + " до " + (nextFieldX + 2) + " " + (nextFieldY + 2));
        if (isSmallFieldFull(nextFieldX, nextFieldY)) {
            firstMove = true;
            System.out.println("СЛЕДУЮЩИЙ ХОД БУДЕТ В ЛЮБУЮ КЛЕТКУ!!!");
        }
        if (isBigFieldFull()) {
            gameOver = true;
            gameOverMessage = "DRAW!!!";
        }
    }

    private boolean checkWin(char sign) {
        for (int i = 0; i < BIG_LINES_COUNT; i++) {
            // проверяем строки
            if (checkBigLine(i, 0, 0, 1, sign)) return true;
            // проверяем столбцы
            if (checkBigLine(0, i, 1, 0, sign)) return true;
        }
        // проверяем диагонали
        if (checkBigLine(0, 0, 1, 1, sign)) return true;
        if (checkBigLine(0, BIG_LINES_COUNT - 1, 1, -1, sign)) return true;
        return false;
    }

    private boolean checkBigLine(int start_x, int start_y, int dx, int dy, char sign) {
        for (int i = 0; i < BIG_LINES_COUNT; i++) {
            if (bigCell[start_x + i * dx][start_y + i * dy] != sign)
                return false;
        }
        return true;
    }

    boolean checkSmallWin(char sign, int startX, int startY) {
        for (int i = 0; i < SIGN_TO_WIN; i++) {
            // Строки
            if (checkSmallLine(startX, startY, i, '-', sign)) return true;
            // Столбцы
            if (checkSmallLine(startX, startY, i, '|', sign)) return true;
            // Диагонали
            if (checkSmallLine(startX, startY, i, '\\', sign)) return true;
            if (checkSmallLine(startX, startY, i, '/', sign)) return true;
        }
        return false;
    }

    private boolean checkSmallLine(int start_x, int start_y, int i, char whatWeCheck, char sign) {
        for (int y = 0; y < SIGN_TO_WIN; y++) {
            if (whatWeCheck == ('-')) {
                if (smallCell[start_x + y][start_y + i] != sign)
                    return false;
            }
            if (whatWeCheck == ('|')) {
                if (smallCell[start_x + i][start_y + y] != sign)
                    return false;
            }
            if (whatWeCheck == ('\\')) {
                if (smallCell[start_x + y][start_y + y] != sign)
                    return false;
            }
            if (whatWeCheck == ('/')) {
                if (smallCell[start_x + 2 - y][start_y + y] != sign)
                    return false;
            }
        }
        return true;
    }

    boolean isCellFree(int x, int y) {
        if (x < 0 || y < 0 || x > LINES_COUNT - 1 || y > LINES_COUNT - 1) {
            return false;
        }
        return smallCell[x][y] == NOT_SIGN;
    }

    private boolean isSmallFieldFull(int start_x, int start_y) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (smallCell[start_x + i][start_y + j] == NOT_SIGN)
                    return false;
            }
        }
        return true;
    }

    private boolean isBigFieldFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (bigCell[i][j] == NOT_SIGN)
                    return false;
            }
        }
        return true;
    }

    // Проверка на поле для следующего хода.
    private void methodThatAssignsValuesToVariables() {
        if (x == 0 || x == 3 || x == 6) {
            if (y == 0 || y == 3 || y == 6) {
                // Левый верхний угол.
                System.out.println("Левый верхний угол");
                nextFieldX = 0;
                nextFieldY = 0;
                startPointNextTurnX = 0;
                startPointNextTurnY = 0;
            } else if (y == 1 || y == 4 || y == 7) {
                // Левый центральный угол.
                System.out.println("Левый центральный угол");
                nextFieldX = 0;
                nextFieldY = 3;
                startPointNextTurnX = 0;
                startPointNextTurnY = 1;
            } else if (y == 2 || y == 5 || y == 8) {
                // Левый нижний угол.
                System.out.println("Левый нижний угол");
                nextFieldX = 0;
                nextFieldY = 6;
                startPointNextTurnX = 0;
                startPointNextTurnY = 2;
            }
        } else if (x == 1 || x == 4 || x == 7) {
            if (y == 0 || y == 3 || y == 6) {
                // Центральный верхний угол.
                System.out.println("Центральный верхний угол");
                nextFieldX = 3;
                nextFieldY = 0;
                startPointNextTurnX = 1;
                startPointNextTurnY = 0;
            } else if (y == 1 || y == 4 || y == 7) {
                // Центр.
                System.out.println("Центр");
                nextFieldX = 3;
                nextFieldY = 3;
                startPointNextTurnX = 1;
                startPointNextTurnY = 1;
            } else if (y == 2 || y == 5 || y == 8) {
                // Центральный нижний угол.
                System.out.println("Центральный нижний угол");
                nextFieldX = 3;
                nextFieldY = 6;
                startPointNextTurnX = 1;
                startPointNextTurnY = 2;
            }
        } else if (x == 2 || x == 5 || x == 8) {
            if (y == 0 || y == 3 || y == 6) {
                // Правый верхний угол.
                System.out.println("Правый верхний угол");
                nextFieldX = 6;
                nextFieldY = 0;
                startPointNextTurnX = 2;
                startPointNextTurnY = 0;
            } else if (y == 1 || y == 4 || y == 7) {
                // Правый центральный угол.
                System.out.println("Правый центральный угол");
                nextFieldX = 6;
                nextFieldY = 3;
                startPointNextTurnX = 2;
                startPointNextTurnY = 1;
            } else if (y == 2 || y == 5 || y == 8) {
                // Правый нижний угол.
                System.out.println("Правый нижний угол");
                nextFieldX = 6;
                nextFieldY = 6;
                startPointNextTurnX = 2;
                startPointNextTurnY = 2;
            }
        }
    }

    private void fromSmallCellInBigCell() {
        if (x >= 0 && x < 3 && y >= 0 && y < 3) {
            fieldX = 0;
            fieldY = 0;
            startX = 0;
            startY = 0;
        } else if (x >= 3 && x < 6 && y >= 0 && y < 3) {
            fieldX = 1;
            fieldY = 0;
            startX = 3;
            startY = 0;
        } else if (x >= 6 && x < 9 && y >= 0 && y < 3) {
            fieldX = 2;
            fieldY = 0;
            startX = 6;
            startY = 0;
        } else if (x >= 0 && x < 3 && y >= 3 && y < 6) {
            fieldX = 0;
            fieldY = 1;
            startX = 0;
            startY = 3;
        } else if (x >= 3 && x < 6 && y >= 3 && y < 6) {
            fieldX = 1;
            fieldY = 1;
            startX = 3;
            startY = 3;
        } else if (x >= 6 && x < 9 && y >= 3 && y < 6) {
            fieldX = 2;
            fieldY = 1;
            startX = 6;
            startY = 3;
        } else if (x >= 0 && x < 3 && y >= 6 && y < 9) {
            fieldX = 0;
            fieldY = 2;
            startX = 0;
            startY = 6;
        } else if (x >= 3 && x < 6 && y >= 6 && y < 9) {
            fieldX = 1;
            fieldY = 2;
            startX = 3;
            startY = 6;
        } else if (x >= 6 && x < 9 && y >= 6 && y < 9) {
            fieldX = 2;
            fieldY = 2;
            startX = 6;
            startY = 6;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (playersMove) {
            g.drawImage(whiteGreen, startPointNextTurnX * 270, startPointNextTurnY * 270, 270, 270, null);
        }
        // Рисуем линии полей маленького поля
        for (int i = 1; i < 9; i++) {
            g.setColor(Color.BLACK);
            g.drawLine(15, i * CELL_SIZE, BIG_CELL_SIZE - 15, i * CELL_SIZE);
            g.drawLine(BIG_CELL_SIZE + 15, i * CELL_SIZE, BIG_CELL_SIZE * 2 - 15, i * CELL_SIZE);
            g.drawLine(BIG_CELL_SIZE * 2 + 15, i * CELL_SIZE, BIG_CELL_SIZE * 3 - 15, i * CELL_SIZE);
            g.drawLine(i * CELL_SIZE, 15, i * CELL_SIZE, BIG_CELL_SIZE - 15);
            g.drawLine(i * CELL_SIZE, BIG_CELL_SIZE + 15, i * CELL_SIZE, BIG_CELL_SIZE * 2 - 15);
            g.drawLine(i * CELL_SIZE, BIG_CELL_SIZE * 2 + 15, i * CELL_SIZE, BIG_CELL_SIZE * 3 - 15);
        }
        // Рисуем линии полей большого поля
        for (int i = 1; i < 3; i++) {
            ((Graphics2D) g).setStroke(new BasicStroke(3));
            g.setColor(purple);
            g.drawLine(0, i * BIG_CELL_SIZE, GAME_PANEL, i * BIG_CELL_SIZE);
            g.drawLine(i * BIG_CELL_SIZE, 0, i * BIG_CELL_SIZE, GAME_PANEL);
        }
        for (int i = 0; i < LINES_COUNT; i++) {
            for (int j = 0; j < LINES_COUNT; j++) {
                if (smallCell[i][j] != NOT_SIGN) {
                    if (smallCell[i][j] == 'X') {
                        if (i < 3 && j < 3)
                            g.drawImage(imageCross, (((i + 1) * INDENT_12) + (i * IMAGE_SIZE)), (((j + 1) * INDENT_12) + (j * IMAGE_SIZE)), IMAGE_SIZE, IMAGE_SIZE, null);
                        if (i >= 3 && i < 6 && j < 3)
                            g.drawImage(imageCross, (((i + 1) * INDENT_14) + (i * IMAGE_SIZE)), (((j + 1) * INDENT_12) + (j * IMAGE_SIZE)), IMAGE_SIZE, IMAGE_SIZE, null);
                        if (i >= 6 && j < 3)
                            g.drawImage(imageCross, (((i + 1) * INDENT_14) + (i * IMAGE_SIZE)), (((j + 1) * INDENT_12) + (j * IMAGE_SIZE)), IMAGE_SIZE, IMAGE_SIZE, null);
                        if (i < 3 && j >= 3 && j < 6)
                            g.drawImage(imageCross, (((i + 1) * INDENT_12) + (i * IMAGE_SIZE)), (((j + 1) * INDENT_14) + (j * IMAGE_SIZE)), IMAGE_SIZE, IMAGE_SIZE, null);
                        if (i >= 3 && i < 6 && j >= 3 && j < 6)
                            g.drawImage(imageCross, (((i + 1) * INDENT_14) + (i * IMAGE_SIZE)), (((j + 1) * INDENT_14) + (j * IMAGE_SIZE)), IMAGE_SIZE, IMAGE_SIZE, null);
                        if (i >= 6 && j >= 3 && j < 6)
                            g.drawImage(imageCross, (((i + 1) * INDENT_14) + (i * IMAGE_SIZE)), (((j + 1) * INDENT_14) + (j * IMAGE_SIZE)), IMAGE_SIZE, IMAGE_SIZE, null);
                        if (i < 3 && j >= 6)
                            g.drawImage(imageCross, (((i + 1) * INDENT_12) + (i * IMAGE_SIZE)), (((j + 1) * INDENT_14) + (j * IMAGE_SIZE)), IMAGE_SIZE, IMAGE_SIZE, null);
                        if (i >= 3 && i < 6 && j >= 6)
                            g.drawImage(imageCross, (((i + 1) * INDENT_14) + (i * IMAGE_SIZE)), (((j + 1) * INDENT_14) + (j * IMAGE_SIZE)), IMAGE_SIZE, IMAGE_SIZE, null);
                        if (i >= 6 && j >= 6)
                            g.drawImage(imageCross, (((i + 1) * INDENT_14) + (i * IMAGE_SIZE)), (((j + 1) * INDENT_14) + (j * IMAGE_SIZE)), IMAGE_SIZE, IMAGE_SIZE, null);

                        // Рисует крестик.
                        /*g.setColor(Color.RED);
                        g.drawLine((i * CROSS_SIZE), (j * CROSS_SIZE), (i + 1) * CROSS_SIZE, (j + 1) * CROSS_SIZE);
                        g.drawLine((i + 1) * CROSS_SIZE, (j * CROSS_SIZE), (i * CROSS_SIZE), (j + 1) * CROSS_SIZE);*/
                    }
                    // Вставляет в клетки картинку круга.
                    if (smallCell[i][j] == 'O' || smallCell[i][j] == '0') {
                        if (i < 3 && j < 3)
                            g.drawImage(imageCircle, (((i + 1) * INDENT_12) + (i * IMAGE_SIZE)), (((j + 1) * INDENT_12) + (j * IMAGE_SIZE)), IMAGE_SIZE, IMAGE_SIZE, null);
                        if (i >= 3 && i < 6 && j < 3)
                            g.drawImage(imageCircle, (((i + 1) * INDENT_14) + (i * IMAGE_SIZE)), (((j + 1) * 13) + (j * IMAGE_SIZE)), IMAGE_SIZE, IMAGE_SIZE, null);
                        if (i >= 6 && j < 3)
                            g.drawImage(imageCircle, (((i + 1) * INDENT_14) + (i * IMAGE_SIZE)), (((j + 1) * 13) + (j * IMAGE_SIZE)), IMAGE_SIZE, IMAGE_SIZE, null);
                        if (i < 3 && j >= 3 && j < 6)
                            g.drawImage(imageCircle, (((i + 1) * INDENT_12) + (i * IMAGE_SIZE)), (((j + 1) * INDENT_14) + (j * IMAGE_SIZE)), IMAGE_SIZE, IMAGE_SIZE, null);
                        if (i >= 3 && i < 6 && j >= 3 && j < 6)
                            g.drawImage(imageCircle, (((i + 1) * INDENT_14) + (i * IMAGE_SIZE)), (((j + 1) * INDENT_14) + (j * IMAGE_SIZE)), IMAGE_SIZE, IMAGE_SIZE, null);
                        if (i >= 6 && j >= 3 && j < 6)
                            g.drawImage(imageCircle, (((i + 1) * INDENT_14) + (i * IMAGE_SIZE)), (((j + 1) * INDENT_14) + (j * IMAGE_SIZE)), IMAGE_SIZE, IMAGE_SIZE, null);
                        if (i < 3 && j >= 3 && j >= 6)
                            g.drawImage(imageCircle, (((i + 1) * INDENT_12) + (i * IMAGE_SIZE)), (((j + 1) * INDENT_14) + (j * IMAGE_SIZE)), IMAGE_SIZE, IMAGE_SIZE, null);
                        if (i >= 3 && i < 6 && j >= 6)
                            g.drawImage(imageCircle, (((i + 1) * INDENT_14) + (i * IMAGE_SIZE)), (((j + 1) * INDENT_14) + (j * IMAGE_SIZE)), IMAGE_SIZE, IMAGE_SIZE, null);
                        if (i >= 6 && j >= 6)
                            g.drawImage(imageCircle, (((i + 1) * INDENT_14) + (i * IMAGE_SIZE)), (((j + 1) * INDENT_14) + (j * IMAGE_SIZE)), IMAGE_SIZE, IMAGE_SIZE, null);

                        // Рисует круги.
                        /*g.setColor(Color.BLUE);
                        g.drawOval((((i + 1) * 10) + (i * IMAGE_SIZE)), (((j + 1) * 10) + (j * IMAGE_SIZE)), IMAGE_SIZE, IMAGE_SIZE);
                        g.drawOval((((i + 1) * 13) + (i * IMAGE_SIZE)), (((j + 1) * 10) + (j * IMAGE_SIZE)), IMAGE_SIZE, IMAGE_SIZE);
                        g.drawOval((((i + 1) * 12) + (i * IMAGE_SIZE)), (((j + 1) * 10) + (j * IMAGE_SIZE)), IMAGE_SIZE, IMAGE_SIZE);
                        g.drawOval((((i + 1) * 10) + (i * IMAGE_SIZE)), (((j + 1) * 13) + (j * IMAGE_SIZE)), IMAGE_SIZE, IMAGE_SIZE);
                        g.drawOval((((i + 1) * 13) + (i * IMAGE_SIZE)), (((j + 1) * 13) + (j * IMAGE_SIZE)), IMAGE_SIZE, IMAGE_SIZE);
                        g.drawOval((((i + 1) * 14) + (i * IMAGE_SIZE)), (((j + 1) * 13) + (j * IMAGE_SIZE)), IMAGE_SIZE, IMAGE_SIZE);
                        g.drawOval((((i + 1) * 10) + (i * IMAGE_SIZE)), (((j + 1) * 14) + (j * IMAGE_SIZE)), IMAGE_SIZE, IMAGE_SIZE);
                        g.drawOval((((i + 1) * 13) + (i * IMAGE_SIZE)), (((j + 1) * 14) + (j * IMAGE_SIZE)), IMAGE_SIZE, IMAGE_SIZE);
                        g.drawOval((((i + 1) * 14) + (i * IMAGE_SIZE)), (((j + 1) * 14) + (j * IMAGE_SIZE)), IMAGE_SIZE, IMAGE_SIZE);*/
                    }
                }
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (bigCell[i][j] != NOT_SIGN) {
                    if (bigCell[i][j] == 'X') {
                        g.setColor(Color.RED);
                        g.drawLine((i * CROSS_SIZE), (j * CROSS_SIZE), (i + 1) * CROSS_SIZE, (j + 1) * CROSS_SIZE);
                        g.drawLine((i + 1) * CROSS_SIZE, (j * CROSS_SIZE), (i * CROSS_SIZE), (j + 1) * CROSS_SIZE);
                    }
                    if (bigCell[i][j] == 'O' || bigCell[i][j] == '0') {
                        g.setColor(Color.BLUE);
                        g.drawOval((((i + 1) * 8) + (i * CIRCLE_SIZE)), (((j + 1) * 8) + (j * CIRCLE_SIZE)), CIRCLE_SIZE, CIRCLE_SIZE);
                    }
                }
            }
        }
        if (gameOver) {
            g.setColor(Color.black);
            g.setFont(new Font("Tahoma", Font.BOLD, 70));
            g.drawString(gameOverMessage, 220, 430);
        }
    }
}