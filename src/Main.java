import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main implements ActionListener{
    public static JFrame frame, menuFrame;
    private JPanel cards;
    private CardLayout cardLayout;
    private Menu menu;
    public static Board board;
    private JButton restartButton;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->new Main().createGUI());
    }
    public void createGUI(){
        frame = new JFrame("Minesweeper");
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setLocation(650, 250);
        frame.setSize(500,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        menu = new Menu(this);
        JPanel gamePanel = new JPanel(new BorderLayout());

        restartButton = new JButton("Restart");
        restartButton.setFocusable(false);
        restartButton.addActionListener(this);
        restartButton.setSize(50,30);

        gamePanel.add(restartButton, BorderLayout.NORTH);

        cards.add(menu, "MenuPanel");
        cards.add(gamePanel, "GamePanel");

        frame.add(cards);
        frame.setVisible(true);
    }
    public void startGame(String boardSize, String difficulty){
        String [] dim = boardSize.split("x");
        int rows = Integer.parseInt(dim[0]);
        int cols = Integer.parseInt(dim[1]);

        if(board != null){
            cards.remove(board);
        }

        board = new Board(rows, cols, difficulty);

        JPanel gamePanel = (JPanel) cards.getComponent(1);
        gamePanel.add(board, BorderLayout.CENTER);
        gamePanel.revalidate();
        gamePanel.repaint();

        cardLayout.show(cards, "GamePanel");
    }

   @Override
    public void actionPerformed(ActionEvent e) {
        startGame(board.getRow() + "x" + board.getCol(),"Easy");
     /*   SwingUtilities.invokeLater(()->restartButton.addActionListener(
            if(board != null){
            cards.remove(board);
             }

       board = new Board(board.getRow() , cols, difficulty);

       JPanel gamePanel = (JPanel) cards.getComponent(1);
       gamePanel.add(board, BorderLayout.CENTER);
       gamePanel.revalidate();
       gamePanel.repaint();

       cardLayout.show(cards, "GamePanel"););*/
    }
}

/*SwingUtilities.invokeLater(() -> {

            restartBut.addActionListener(new Main());
        });*/