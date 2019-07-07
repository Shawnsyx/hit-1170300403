package visual;

import static run.Client.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import ladders.Ladder;
import monkeys.Monkey;

public class Cartoon implements Runnable {

  class ladderPanel extends JPanel {
    /**
     * This is the ladder. Draw ladder and the monkey on it.
     */
    private static final long serialVersionUID = 1L;
    private final List<Ladder> ladders;
    private int x0;
    private int y0;
    private Image portrait = new ImageIcon("1.png").getImage();

    public ladderPanel(List<Ladder> inputLadders) {
      this.ladders = inputLadders;
      x0 = 50; // original x - coordinate
      y0 = 30; // original y - coordinate
    }

    @Override
    public void paint(Graphics g) {   
      super.paint(g);
      x0 = 50;
      y0 = 30;
      g.setColor(new Color(0, 176, 240));
      int height = 30;
      int width = 760;
      for (int j = 0; j < ladders.size(); j++) {
        Ladder iLadder = ladders.get(j);
        Map<Integer, Monkey> monkeysOnLadder = iLadder.getMonkeyCondition();
        y0 = 50 * (j + 1);
        /* Draw ladders */
        g.drawRect(x0, y0, width, height);
        g.drawLine(x0 - 30, y0, x0, y0);
        g.drawLine(x0 - 30, y0 + height, x0, y0 + height);
        g.drawLine(x0 + width, y0, x0 + width + 30, y0);
        g.drawLine(x0 + width, y0 + height, x0 + width + 30, y0 + height);
        /* End of ladder decoration */
        g.drawString("Ladder:" + iLadder.getLadderIndex() + " Direction:" + iLadder.getDirection(),
            x0 + width + 40, y0 + height / 2);
        /* Draw the monkey picture */
        if (monkeysOnLadder.get(1) != null) {
          g.drawImage(portrait, x0 - 15, y0 + 5, 30, 30, null);
          g.drawString("ID" + monkeysOnLadder.get(1).getID(), x0 - 8, y0 + 50);
        }
        for (int i = 1; i < 20; i++) {
          int x1 = x0 + i * 40;
          int y1 = y0;
          int x2 = x0 + i * 40;
          int y2 = y1 + height;
          g.drawLine(x1, y1, x2, y2);
          if (monkeysOnLadder.get(i + 1) != null) { // The position arc is complex.
            g.drawImage(portrait, x1 - 15, y1 + 5, 30, 30, null);
            g.drawString("ID" + monkeysOnLadder.get(i + 1).getID(), x1 - 8, y1 + 50);
          }
        }
        g.setColor(new Color(0, 176, 240));
      }
      boolean isGoingOn = false;
      for (Ladder i : manyLadders) {
        if (!i.isNoMonkey()) {
          isGoingOn = true;
        }
      }    
        repaint();
        if(!isGoingOn) {
          g.drawString("ALL THE MONKEYS CROSS THE RIVER" , 300 , 35 );
        }
      try {
        Thread.sleep(1000);    
      } catch (InterruptedException e) {
        return;
      }  
      
    }
    

  }

  @Override
  public void run() {
    JFrame windowFrame = new JFrame("River");
    JPanel ladderJPanel = new JPanel();
    if (manyLadders instanceof List<?>) {
      ladderJPanel = new ladderPanel(manyLadders);
    }
    windowFrame.add(ladderJPanel);
    windowFrame.setBackground(Color.white);
    windowFrame.setBounds(0, 0, 1000, 680);
    windowFrame.setVisible(true);
    
    return;
  }
}


