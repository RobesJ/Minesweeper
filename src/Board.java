import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Board extends JPanel implements MouseListener {
    private final Field[][] boardArray;
    private final String difficulty;
    private final int row;
    private final int col;
    private final int numbOfMines;
    private int numbOfNotMines;
    private boolean gameOver;
    private final JLabel gameStatus;
    private final JLabel checkedMinesLabel;
    private int checkedMines;
    private static ImageIcon flagIcon, exploIcon;

    static {
        try {
            BufferedImage img = ImageIO.read(new File("/home/robes/Plocha/MinesweeperV2/out/production/MinesweeperV2/business.png"));
            Image scaledImg = img.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            flagIcon = new ImageIcon(scaledImg);
            BufferedImage img2 = ImageIO.read(new File("/home/robes/Plocha/MinesweeperV2/src/explo.jpeg"));
            Image scaledImg2 = img2.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            exploIcon = new ImageIcon(scaledImg2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Board(int row, int col, String difficulty, JLabel checkedMinesLabel, JLabel gameStatus) {
        this.row = row;
        this.col = col;
        this.difficulty = difficulty;

        double multiplier = 0.2;
        if(difficulty.equals("EASY")){
            multiplier = 0.1;
        }
        else if(difficulty.equals("HARD")){
            multiplier = 0.3;
        }
        this.numbOfMines = (int) Math.floor((row * col) * multiplier);
        this.checkedMines = numbOfMines;
        this.checkedMinesLabel = checkedMinesLabel;
        checkedMinesLabel.setIcon(flagIcon);
        checkedMinesLabel.setIconTextGap(5);
        checkedMinesLabel.setText(Integer.toString(checkedMines));
        checkedMinesLabel.setForeground(Color.WHITE);
        checkedMinesLabel.setBackground(Color.BLACK);

        this.gameStatus = gameStatus;
        gameStatus.setVisible(false);

        this.numbOfNotMines = row * col - numbOfMines;
        this.gameOver = false;
        int fieldSize = 30;

        this.setLayout(new GridLayout(row, col));
        this.setSize(row * fieldSize, col * fieldSize);

        boardArray = new Field[row][col];
        initBoard(this);
        generateMinePos();
    }

    private void initBoard(JPanel window) {
        for (int i = 0; i < boardArray.length; i++) {
            for (int j = 0; j < boardArray[0].length; j++) {
                Field button = new Field(false, false, i, j);
                window.add(button);
                button.setUI(new CustomButtonUI());
                button.addMouseListener(this);
                button.setBackground(Color.WHITE.darker());
                button.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                boardArray[i][j] = button;
                boardArray[i][j].setType(Type.BLANK);
            }
        }
    }

    private void revealField(int row, int col) {
        if (boardArray[row][col].isFlagged() || gameOver) {
              return;
        }
        if (boardArray[row][col].getType() == Type.MINE) {
            boardArray[row][col].setIcon(exploIcon);
            gameOver = true;
            gameStatus.setText("GAME OVER");
            gameStatus.setForeground(Color.RED);
            gameStatus.setVisible(true);

        } else if (boardArray[row][col].getType() == Type.BLANK) {
            Queue<Field> queue = new LinkedList<>();
            queue.add(boardArray[row][col]);

            while (!queue.isEmpty()) {
                Field current = queue.poll();
                int currentRow = current.getMineRow();
                int currentCol = current.getMineCol();

                if (current.notRevealed() && !current.isFlagged()) {
                    current.setRevealed();
                    numbOfNotMines--;
                    current.setEnabled(false);

                    for (int r = currentRow - 1; r <= currentRow + 1; r++) {
                        for (int c = currentCol - 1; c <= currentCol + 1; c++) {
                            if (r >= 0 && r < this.row && c >= 0 && c < this.col) {
                                Field neighbor = boardArray[r][c];
                                if (neighbor.notRevealed() && !neighbor.isFlagged()) {
                                    if (neighbor.getType() == Type.NUMBER) {
                                        neighbor.setRevealed();
                                        numbOfNotMines--;
                                        neighbor.setEnabled(false);
                                    } else if (neighbor.getType() == Type.BLANK) {
                                        queue.add(neighbor);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            boardArray[row][col].setRevealed();
            boardArray[row][col].setEnabled(false);
            numbOfNotMines--;
        }
        if (checkWin() == 0) {
            gameOver = true;
        }
    }

    public void flagField(int row, int col) {
        if (boardArray[row][col].notRevealed() && !gameOver) {
            if (boardArray[row][col].isFlagged()) {
                boardArray[row][col].setFlagged(false);
                checkedMines++;
                checkedMinesLabel.setText(Integer.toString(checkedMines));
            } else {
                boardArray[row][col].setFlagged(true);
                checkedMines--;
                checkedMinesLabel.setText(Integer.toString(checkedMines));
            }
        }
    }

    private void generateMinePos() {
        int count = 0;
        Random rand = new Random();

        while (count <= numbOfMines - 1) {
            int randRow = rand.nextInt(row);
            int randCol = rand.nextInt(col);
            if (boardArray[randRow][randCol].getType() != Type.MINE) {
                count++;
                boardArray[randRow][randCol].setType(Type.MINE);
                //check surroundings of MINE cell, create or add to NUMBER cell
                for (int x = randRow - 1; x <= randRow + 1; x++) {
                    for (int y = randCol - 1; y <= randCol + 1; y++) {
                        if (x != this.row && x != -1 && y != -1 && y != this.col) {
                            if (boardArray[x][y].getType() == Type.BLANK) {
                                boardArray[x][y].setType(Type.NUMBER);
                            } else if (boardArray[x][y].getType() == Type.NUMBER) {
                                boardArray[x][y].incrementNumb();
                            }
                        }
                    }
                }
            }
        }
    }

    public int checkWin() {
        return (numbOfNotMines == 0) ? 0 : 1;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Field field = (Field) e.getSource();
        if (e.getButton() == MouseEvent.BUTTON1) {
            this.revealField(field.getMineRow(), field.getMineCol());
            if (checkWin() == 0) {
                gameStatus.setText("YOU WON");
                gameStatus.setForeground(Color.GREEN);
                gameStatus.setVisible(true);
            }
        }
        if (e.getButton() == MouseEvent.BUTTON3) {
            this.flagField(field.getMineRow(), field.getMineCol());
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    public int getRow(){
        return row;
    }

    public int getCol(){
        return col;
    }

    public String getDifficulty(){
        return difficulty;
    }

    public  static class CustomButtonUI extends BasicButtonUI {
        @Override
        public void paint(Graphics g, JComponent c) {
            AbstractButton button = (AbstractButton) c;
            if (!button.isEnabled()) {
                // Set background color when disabled
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, button.getWidth(), button.getHeight());

                // Draw the text and icon in the original foreground color
                g.setColor(button.getForeground());
                if (button.getIcon() != null) {
                    button.getIcon().paintIcon(button, g, (button.getWidth() - button.getIcon().getIconWidth()) / 2,
                            (button.getHeight() - button.getIcon().getIconHeight()) / 2);
                }
                FontMetrics fm = g.getFontMetrics();
                int textX = (button.getWidth() - fm.stringWidth(button.getText())) / 2;
                int textY = (button.getHeight() + fm.getAscent()) / 2 - 2;
                g.drawString(button.getText(), textX, textY);
            } else {
                super.paint(g, c);
            }
        }
    }
}