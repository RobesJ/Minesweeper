import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Field extends JButton {
    private boolean flagged;
    private boolean revealed;
    private int numb = 1;
    private Type type;
    private final int mineRow;
    private final int mineCol;
    private static ImageIcon flagIcon;

    static {
        try {
            BufferedImage img = ImageIO.read(new File("/home/robes/Plocha/MinesweeperV2/out/production/MinesweeperV2/business.png"));
            Image scaledImg = img.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            flagIcon = new ImageIcon(scaledImg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Field(boolean revealed, boolean flagged, int mineRow, int mineCol) {
        this.flagged = flagged;
        this.revealed = revealed;
        this.mineCol = mineCol;
        this.mineRow = mineRow;
        this.setFocusable(false);
        this.setMargin(new Insets(0, 0, 0, 0));
        this.setPreferredSize(new Dimension(30, 30)); // Ensure consistent size
    }

    public void incrementNumb(){
        this.numb++;
    }

    public void setType(Type type){
        this.type = type;
    }

    public Type getType(){
        return this.type;
    }

    public void setFlagged(boolean flagged){
        this.flagged = flagged;
        if(isFlagged()){
            this.setIcon(flagIcon);
        }
        else{
            this.setIcon(null);
        }
    }

    public void setRevealed(){
        this.revealed = true;
        if(this.getType() == Type.NUMBER){
            this.setText(Integer.toString(numb));
        }
    }

    public boolean isFlagged(){
        return flagged;
    }

    public boolean notRevealed(){
        return !revealed;
    }

    public int getMineRow(){
        return this.mineRow;
    }

    public int getMineCol(){
        return this.mineCol;
    }
}