import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class InRead {

  private String path;
  private int minY, maxY; 
  
  public InRead(String name) {
    path = name;
  }

  public ArrayList<Point> readFile() {
    ArrayList<Point> outputArray = new ArrayList<>();
    int x, y, smallestY = Integer.MAX_VALUE, biggestY = 0;
  
    try {
      FileReader fr = new FileReader(path);
      BufferedReader br = new BufferedReader(fr);
      
      String str = br.readLine();
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
      
      Collections.sort(outputArray, (a, b) -> {

        if (a.x < b.x) {
          return -1;
        }
        else if (a.x > b.x) {
          return 1;
        }
        else {
          return 0;
        }

      });
      
      WriteFile stuff = new WriteFile(path, false);
      stuff.writeToFile();
      WriteFile stuff2 = new WriteFile(path, true);
      for(int i = 0; i < outputArray.size(); i++) {
       
        String date;
        date = String.valueOf((int)outputArray.get(i).getX());
        date = date.substring(0, 4) + "/" + date.substring(4,6) + "/" + date.substring(6,8);
        stuff2.writeToFile(date);
        String sugar;
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