import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main implements ActionListener{
    public static JFrame frame;
    private JPanel cards;
    private CardLayout cardLayout;
    private Menu menu;
    public static Board board;
    private JLabel gameStatus;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->new Main().createGUI());
    }

    public void createGUI(){
        frame = new JFrame();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setLocation(650, 250);
        frame.setSize(new Dimension(350,300));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        menu = new Menu(this);
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new BoxLayout(gamePanel,BoxLayout.Y_AXIS));
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));

        // create a button for going back to MAIN MANE from gamePanel
        JButton goToMenuButt = new JButton("MAIN MENU");
        goToMenuButt.setFocusable(false);
        goToMenuButt.addActionListener(this);
        goToMenuButt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            gamePanel.remove(board);
            cardLayout.show(cards, "MenuPanel");
            }
        });
        controlPanel.add(goToMenuButt);
        controlPanel.add(Box.createHorizontalGlue());

        gameStatus = new JLabel();
        gameStatus.setOpaque(true);
        controlPanel.add(gameStatus);
        controlPanel.add(Box.createHorizontalGlue());

        //create a button for restarting a game
        JButton restartButton = new JButton("RESTART");
        restartButton.setFocusable(false);
        restartButton.addActionListener(this);
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gamePanel.remove(board);
                startGame(board.getRow() + " x " + board.getCol(), board.getDifficulty());
            }
        });
        controlPanel.add(restartButton);

        gamePanel.add(controlPanel);
        cards.add(menu, "MenuPanel");
        cards.add(gamePanel, "GamePanel");
        frame.add(cards);
        frame.setVisible(true);
    }

    public void startGame(String boardSize, String difficulty){
        String [] dim = boardSize.split(" x ");
        int rows = Integer.parseInt(dim[0]);
        int cols = Integer.parseInt(dim[1]);

        if(board != null){
            cards.remove(board);
        }
        board = new Board(rows, cols, difficulty);

        JPanel gamePanel = (JPanel) cards.getComponent(1);
        gamePanel.add(board);
        gamePanel.revalidate();
        gamePanel.repaint();
        cardLayout.show(cards, "GamePanel");
        frame.pack();
    }

   @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == menu.getStartButton()) {
            startGame(board.getRow() + " x " + board.getCol(), board.getDifficulty());

        }
    }
}

/*"Minesweeper"

if(board.checkWin() == 0){
        System.out.println("main you won");
                gameStatus.setText("YOU WON");
            } else if (board.getGameOver()) {
        gameStatus.setText("GAME OVER");
                System.out.println("main you lost");
            }

 */