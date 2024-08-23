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
    private JLabel checkedMines;
    JLabel gameStatus;
    private JLayeredPane layeredPane;
    private JPanel gameStatusPanel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->new Main().createGUI());
    }

    public void createGUI(){
        frame = new JFrame();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setLocation(620, 220);
        frame.setSize(new Dimension(630,727));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        //super panel
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new BoxLayout(gamePanel,BoxLayout.Y_AXIS));

        //panel that contains buttons and label at the top of super panel
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));
        controlPanel.setPreferredSize(new Dimension(frame.getWidth(), 60));
        controlPanel.setBackground(Color.BLACK);

        //go back to menu button
        JButton goToMenuButt = getMainMenuButton();

        checkedMines = new JLabel();
        checkedMines.setOpaque(true);

        //restart game button
        JButton restartButton = getRestartButton();

        controlPanel.add(Box.createRigidArea(new Dimension(20, 60)));
        controlPanel.add(goToMenuButt);
        controlPanel.add(Box.createHorizontalGlue());
        controlPanel.add(checkedMines);
        controlPanel.add(Box.createHorizontalGlue());
        controlPanel.add(restartButton);
        controlPanel.add(Box.createRigidArea(new Dimension(20, 60)));

        gamePanel.add(controlPanel);

        //panel that contains board under the control panel
        JPanel boardPanel = new JPanel(new GridBagLayout());
        boardPanel.setBackground(Color.BLACK);

        // Create a JLayeredPane and add both the board and the gameStatus label to it
        layeredPane = new JLayeredPane();
        layeredPane.setLayout(new OverlayLayout(layeredPane));
        boardPanel.add(layeredPane);
        gamePanel.add(boardPanel);

        menu = new Menu(this);
        cards.add(menu, "MenuPanel");
        cards.add(gamePanel, "GamePanel");
        frame.add(cards);
        frame.setVisible(true);
    }

    private JButton getMainMenuButton() {
        JButton goToMenuButt = new JButton("MAIN MENU");
        goToMenuButt.setFocusable(false);
        goToMenuButt.setBackground(Color.WHITE);
        goToMenuButt.setForeground(Color.BLACK);
        goToMenuButt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            layeredPane.remove(board);
            layeredPane.remove(gameStatusPanel);
            checkedMines.setText("");
            cardLayout.show(cards, "MenuPanel");
            }
        });
        return goToMenuButt;
    }

    private JButton getRestartButton() {
        JButton restartButton = new JButton("RESTART");
        restartButton.setBackground(Color.WHITE);
        restartButton.setForeground(Color.BLACK);
        restartButton.setFocusable(false);
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                layeredPane.remove(board);
                layeredPane.remove(gameStatusPanel);
                checkedMines.setText("");
                startGame(board.getRow() + " x " + board.getCol(), board.getDifficulty());
            }
        });
        return restartButton;
    }

    public void startGame(String boardSize, String difficulty){

        if(boardSize.isEmpty() || difficulty.isEmpty()){
            JOptionPane.showMessageDialog(menu, "Missing board size or difficulty");
            return;
        }

        String[] dim = boardSize.split(" x ");
        int rows = Integer.parseInt(dim[0]);
        int cols = Integer.parseInt(dim[1]);

        gameStatusPanel = new JPanel(new GridBagLayout());
        gameStatusPanel.setOpaque(false);
        gameStatus = new JLabel("");
        gameStatus.setFont(new Font("Arial", Font.BOLD, 40));
        gameStatus.setVisible(false);
        gameStatusPanel.add(gameStatus);

        if(board != null){
            cards.remove(board);
        }
        board = new Board(rows, cols, difficulty, checkedMines, gameStatus);

        layeredPane.add(board, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(gameStatusPanel, JLayeredPane.PALETTE_LAYER);
        layeredPane.revalidate();
        layeredPane.repaint();
        cardLayout.show(cards, "GamePanel");
    }

   @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == menu.getStartButton()) {
            startGame(board.getRow() + " x " + board.getCol(), board.getDifficulty());
        }
    }
}