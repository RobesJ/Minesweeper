import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
    private JLabel label;

    public Board(int row, int col, String difficulty) {
        UIManager.put("Button.disabledText", UIManager.get("Button.foreground"));
        this.row = row;
        this.col = col;
        this.difficulty = difficulty;
        double multiplier = 0.2;
        if(difficulty.equals("Easy")){
            multiplier = 0.1;
        }
        else if(difficulty.equals("Hard")){
            multiplier = 0.3;
        }
        this.numbOfMines = (int) Math.floor((row * col) * multiplier);
        System.out.println(numbOfMines);
        this.numbOfNotMines = row * col - numbOfMines;
        this.gameOver = false;
        int fieldSize = 30;

        this.setLayout(new GridLayout(row, col));
        this.setSize(row * fieldSize, col * fieldSize);

        boardArray = new Field[row][col];
        initBoard(this);
        generateMinePos();
    }

    public void initBoard(JPanel window) {
        for (int i = 0; i < boardArray.length; i++) {
            for (int j = 0; j < boardArray[0].length; j++) {
                Field button = new Field(false, false, i, j);
                window.add(button);
                button.addMouseListener(this);
                boardArray[i][j] = button;
                boardArray[i][j].setType(Type.BLANK);
            }
        }
    }

    public int revealField(int row, int col) {
        if (boardArray[row][col].isFlagged() || gameOver) return 0;

        if (boardArray[row][col].getType() == Type.MINE) {
            gameOver = true;
            disableAllButtons();
            label = new JLabel("Game over");
            label.setFont(new Font("Arial", Font.BOLD, 30));
            label.setForeground(Color.RED);
            this.setLayout(new BorderLayout());
            this.add(label, BorderLayout.CENTER);
            label.setVisible(true);

            return -1;
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
            disableAllButtons();
        }
        return 0;
    }

    private void disableAllButtons() {
        for (Field[] boardRow : boardArray) {
            for (Field cell : boardRow) {
                cell.setEnabled(false);
            }
        }
    }

    public void flagField(int row, int col) {
        if (boardArray[row][col].notRevealed() && !gameOver) {
            if (boardArray[row][col].isFlagged()) {
                boardArray[row][col].setFlagged(false);
            } else {
                boardArray[row][col].setFlagged(true);
            }
        }
    }

    public void generateMinePos() {
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
            if (this.revealField(field.getMineRow(), field.getMineCol()) == -1) {
                System.out.println("Game over");
            } else if (checkWin() == 0) {
                label = new JLabel("You won");
                this.add(label);
                System.out.println("You won");
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
}