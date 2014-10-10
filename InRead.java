import java.io.*;
import static java.lang.System.*;
import java.util.*;
import java.lang.Object;
import java.awt.geom.Point2D;
import java.awt.Point;
import javax.swing.JOptionPane;


public class InRead {

  private String path;
  private int minY, maxY; 
  
  public InRead(String name) {
  
    path = name;
  }

  public ArrayList<Point> readFile() {
  
    ArrayList<Point> outputArray = new ArrayList<Point>();
    int x, y, smallestY = Integer.MAX_VALUE, biggestY = 0;
  
    try {
    
      FileReader fr = new FileReader(path);
      BufferedReader br = new BufferedReader(fr);
      
      String str = null;
      str = br.readLine();
      while (str != null) {
      
        str = str.replaceAll("/", "");
        x = Integer.parseInt(str);
        str = br.readLine();
        y = Integer.parseInt(str);
        if (y < smallestY) smallestY = y;
        if (y > biggestY) biggestY = y;
        outputArray.add(new Point(x,y));
        str = br.readLine();
      }
      
      minY = smallestY;
      maxY = biggestY;
      br.close();
      
      Collections.sort(outputArray, new Comparator<Point>() {
      
        public int compare (Point a, Point b) {
        
          if (a.x < b.x) {
            return -1;
          }
          else if (a.x > b.x) {
            return 1;
          }
          else {
            return 0;
          }
          
        }
      });
      
      WriteFile stuff = new WriteFile(path, false);
      stuff.writeToFile();
      WriteFile stuff2 = new WriteFile(path, true);
      for(int i = 0; i < outputArray.size(); i++) {
       
        String date = new String();
        date = String.valueOf((int)outputArray.get(i).getX());
        date = date.substring(0, 4) + "/" + date.substring(4,6) + "/" + date.substring(6,8);
        stuff2.writeToFile(date);
        String sugar = new String();
        sugar = String.valueOf((int)outputArray.get(i).getY());
        stuff2.writeToFile(sugar); 
      }     
    }
    catch (NumberFormatException e) {
    
      JOptionPane.showMessageDialog(null, "Please check text file", "UNSPECIFIED ERROR",
                                    JOptionPane.WARNING_MESSAGE);
      System.exit(-1);
    }
    catch (IOException e) {
    
      JOptionPane.showMessageDialog(null, "File Not Found", "ERROR",
                                    JOptionPane.WARNING_MESSAGE);
    }
    return outputArray;

  }
  
  public int minY() {
  
    return minY;
  }
  
  public int maxY() {
  
    return maxY;
  }
  
}