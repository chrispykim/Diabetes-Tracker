import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;


public class MyPanel extends JPanel implements MouseListener{

  ArrayList<Rectangle2D> rects = new ArrayList<Rectangle2D>();
  int minY, maxY, count, entries, n=0;
  double xspacing, yspacing;
  float thickness = 3;
  String date, sugarValue;
  ArrayList<Point> dataPoints = new ArrayList<Point>();
  final static double DEFAULT_MIDPOINT = (40+367)/2;
  
  public MyPanel(ArrayList<Point> array, InRead stuff, int n) {
  
    this.dataPoints = array;
    this.setLayout(new BorderLayout());
    this.count = n;
    this.entries = this.dataPoints.size() - count;
    this.minY = stuff.minY();
    this.maxY = stuff.maxY();
    this.addMouseListener(this);
  }
  
  public void mouseExited(MouseEvent e) {}
  public void mouseEntered(MouseEvent e) {}
  public void mouseReleased(MouseEvent e) {}
  public void mousePressed(MouseEvent e) {}
  
  public void mouseClicked(MouseEvent e) {

    for(int i = 0; i < rects.size(); i++) {
    
      if(rects.get(i).contains(e.getX(), e.getY())) {
    
        n = i;
        date = String.valueOf((int)dataPoints.get(i+count).getX());
        date = date.substring(0, 4) + "/" + date.substring(4,6) + "/" + date.substring(6,8);
        sugarValue = String.valueOf((int)dataPoints.get(i+count).getY());
        this.repaint();  
      }
    }
  }
      
  @Override
  public void paintComponent(Graphics g) {
  
    Graphics2D g2d = (Graphics2D) g;
    super.paintComponent(g2d);
    xspacing = this.getWidth()/((double)(entries+1));
    yspacing = this.getHeight()/345.0;
    if(n == 0) {
    
      if(dataPoints.isEmpty()) {
      
        JOptionPane.showMessageDialog(null, "NO DATA. REOPEN PROGRAM AND ENTER DATA", "ERROR",
                                        JOptionPane.WARNING_MESSAGE);
        System.exit(-1);
      }
      else {
      
        date = String.valueOf((int)dataPoints.get(count).getX());
        date = date.substring(0, 4) + "/" + date.substring(4,6) + "/" + date.substring(6,8);
        g2d.drawString("Date of selected point: " + date, 10, 70);
        sugarValue = String.valueOf((int)dataPoints.get(count).getY());
        g2d.drawString("Sugar level: " + sugarValue, 250, 70);
      }
    }
    else{
    
      g2d.drawString("Date of selected point: " + date, 10, 70);
      g2d.drawString("Sugar level: " + sugarValue, 250, 70);
    }
    g2d.drawString("Displaying " + entries + " most recent entries." +
                   " Click on any point for its date!", 10, 50);
    g2d.drawString("Red = Over, Blue = Under, Black = Good" +
                   " Resize window to zoom in!", 30, this.getHeight()-1);
    g2d.drawLine(0, (int)(this.getHeight()-100*yspacing), this.getWidth(), (int)(this.getHeight()-100*yspacing));
    g2d.drawLine(0, (int)(this.getHeight()-120*yspacing), this.getWidth(), (int)(this.getHeight()-120*yspacing));
    g2d.drawString("120", 10, (int)(this.getHeight()-120*yspacing));
    g2d.drawString("100", 10, (int)(this.getHeight()-100*yspacing));
    for (int i = 0; i*yspacing < (this.getHeight()-80); i += 50) {

      if (i == 100) continue;
      g2d.drawString(String.valueOf(i), 10, (int)(this.getHeight()-i*yspacing));
    }
    
    rects.clear();
    rects.trimToSize();
    
    for(int i = count; i<dataPoints.size(); i++) {
    
      double xValue = xspacing*(i-count+1)+(xspacing/2);
      double yValue = dataPoints.get(i).getY();
      
      if (yValue > 120) g2d.setPaint(Color.RED); 
      else if (yValue < 100) g2d.setPaint(Color.BLUE);
      else g2d.setPaint(Color.BLACK);
      
      Rectangle2D rect = new Rectangle2D.Double(xValue-5, this.getHeight()-yValue*yspacing-5, 10, 10);
      rects.add(rect);
      g2d.fill(rect);
    }
    Stroke oldStroke = g2d.getStroke();
    g2d.setStroke(new BasicStroke(thickness));
    g2d.setPaint(new Color(0, 255, 0));
    g2d.draw(rects.get(n));
    g2d.setStroke(oldStroke);
  }
}
