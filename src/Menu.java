import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

public class Menu extends JPanel{
    private String size;
    private String difficulty;
    private final JButton startButton;
    Main main;
    private final Set<JButton> buttonSet;

    public Menu(Main main) {
        this.difficulty = "";
        this.size = "";
        this.main = main;
        this.setBackground(Color.BLACK);
        buttonSet = new HashSet<>();

        JPanel panelTop = new JPanel();
        panelTop.setLayout(new BoxLayout(panelTop,BoxLayout.X_AXIS));
        panelTop.setPreferredSize(new Dimension(300,50));
        panelTop.setBackground(Color.BLACK);

        JPanel panelMidd = new JPanel();
        panelMidd.setLayout(new BoxLayout(panelMidd,BoxLayout.X_AXIS));
        panelMidd.setPreferredSize(new Dimension(300,50));
        panelMidd.setBackground(Color.BLACK);

        GridBagLayout grid = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        this.setLayout(grid);

        JLabel sizeLabel = new JLabel("CHOOSE BOARD SIZE");
        sizeLabel.setForeground(Color.WHITE);
        sizeLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        JButton smallBoard = new MyButton("10 x 10",this, "size");
        buttonSet.add(smallBoard);

        JButton mediumBoard = new MyButton("15 x 15",this, "size");
        buttonSet.add(mediumBoard);

        JButton bigBoard = new MyButton("20 x 20",this, "size");
        buttonSet.add(bigBoard);

        panelTop.add(smallBoard);
        panelTop.add(Box.createHorizontalGlue());
        panelTop.add(mediumBoard);
        panelTop.add(Box.createHorizontalGlue());
        panelTop.add(bigBoard);

        JLabel diffLabel = new JLabel("CHOOSE DIFFICULTY");
        diffLabel.setForeground(Color.WHITE);
        diffLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        JButton easyButt = new MyButton("EASY",this, "diff");
        buttonSet.add(easyButt);

        JButton mediumButt = new MyButton("MEDIUM",this, "diff");
        buttonSet.add(mediumButt);

        JButton hardButt = new MyButton("HARD", this, "diff");
        buttonSet.add(hardButt);

        panelMidd.add(easyButt);
        panelMidd.add(Box.createHorizontalGlue());
        panelMidd.add(mediumButt);
        panelMidd.add(Box.createHorizontalGlue());
        panelMidd.add(hardButt);

        startButton = new JButton("START GAME");
        startButton.setForeground(Color.BLACK);
        startButton.setBackground(Color.WHITE);
        startButton.setFocusable(false);
        startButton.setSize(140,50);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                main.startGame(size, difficulty);
                for(JButton button : buttonSet){
                    button.setBackground(Color.WHITE);
                }
                size = "";
                difficulty ="";
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets.bottom =5;
        add(sizeLabel,gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets.bottom =20;
        add(panelTop, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets.bottom =5;
        add(diffLabel,gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.insets.bottom = 80;
        add(panelMidd, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(startButton,gbc);
    }

    public JButton getStartButton(){
        return startButton;
    }
    public void setSizeParam(String size) {
        this.size = size;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getSizeParam() {
        return size;
    }

    public String getDifficulty() {
        return difficulty;
    }

    private static class MyButton extends JButton {
        String parameter;
        Menu menu;

        MyButton(String text, Menu menu, String parameter){
            this.menu = menu;
            this.parameter = parameter;
            this.setText(text);
            this.setPreferredSize(new Dimension(90,30));
            this.setFocusable(false);
            this.setBackground(Color.WHITE);
            this.setForeground(Color.BLACK);
            this.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(parameter.equals("size")){
                        if (menu.getSizeParam().isEmpty()) {
                            menu.setSizeParam(text);
                            setBackground(Color.gray);
                        } else if (menu.getSizeParam().equals(text)) {
                            menu.setSizeParam("");
                            setBackground(Color.WHITE);
                        }
                    }
                    else if(parameter.equals("diff")){
                            if (menu.getDifficulty().isEmpty()) {
                                menu.setDifficulty(text);
                                setBackground(Color.gray);
                            } else if (menu.getDifficulty().equals(text)) {
                                menu.setDifficulty("");
                                setBackground(Color.WHITE);
                            }
                        }
                }
            });
        }
    }
}

