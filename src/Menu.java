import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JPanel{
    private JComboBox<String> sizeCB;
    private JComboBox<String> diffCB;
    private JButton startButton;
    private Main main;


    public Menu(Main main) {
        this.main = main;
        //this.setBackground(Color.MAGENTA);
        JPanel panelTop = new JPanel();
        panelTop.setLayout(new BoxLayout(panelTop,BoxLayout.Y_AXIS));
        //panelTop.setBackground(Color.MAGENTA);

        JPanel panelMidd = new JPanel();
        panelMidd.setLayout(new BoxLayout(panelMidd,BoxLayout.Y_AXIS));
        //panelMidd.setBackground(Color.MAGENTA);

        GridBagLayout grid = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        this.setLayout(grid);

        JLabel sizeLabel = new JLabel("CHOOSE BOARD SIZE");
        sizeLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        sizeCB = new JComboBox<>(new String[]{"10 x 10","15 x 15","20 x 20"});
        panelTop.add(sizeLabel);
        panelTop.add(sizeCB);

        JLabel diffLabel = new JLabel("CHOOSE DIFFICULTY");
        diffLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        diffCB = new JComboBox<>(new String[]{"EASY", "MEDIUM", "HARD"});
        panelMidd.add(diffLabel);
        panelMidd.add(diffCB);

        startButton = new JButton("START GAME");
        startButton.setSize(100,50);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                main.startGame(sizeCB.getSelectedItem().toString(),diffCB.getSelectedItem().toString());
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets.bottom =20;
        add(panelTop, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets.bottom = 30;
        add(panelMidd, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(startButton,gbc);
    }

    public JButton getStartButton(){
        return startButton;
    }
}

