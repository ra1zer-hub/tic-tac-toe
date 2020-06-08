package Ultimate_Tic_Tac_Toe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class MainForm extends JFrame {

    MainForm() {
        setSize(818, 875);
        String pad = String.format("%135s%n", "Улучшенные крестики нолики");
        setTitle(pad);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        GameField gameField = new GameField();
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        add(gameField, BorderLayout.CENTER);
        gameField.setBackground(Color.white);
        add(buttonPanel, BorderLayout.SOUTH);
        JButton newGame = new JButton("Начать новую игру");
        JButton rulesOfTheGame = new JButton("Правила");
        newGame.addActionListener(e -> gameField.startGame());
        rulesOfTheGame.addActionListener(e -> {
            new Details();
        });
        buttonPanel.add(newGame);
        buttonPanel.add(rulesOfTheGame);
        buttonPanel.setPreferredSize(new Dimension(1, 40));
        getContentPane().setBackground(Color.YELLOW);
        setVisible(true);
    }
}