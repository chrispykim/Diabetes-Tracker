import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class WriteFile {

  private String path;
  private boolean append = true;
  
  public WriteFile(String filePath, boolean value) {
  
    this.path = filePath;
    this.append = value;
  }
  
  public void writeToFile( String textLine ) throws IOException {

    FileWriter write = new FileWriter(path, append);
    PrintWriter printLine = new PrintWriter(write);
    printLine.printf("%s"+"%n", textLine);
    printLine.close();
  }
  
  public void writeToFile() throws IOException {
  
    FileWriter write = new FileWriter(path, append);
    PrintWriter printLine = new PrintWriter(write);
    printLine.print("");
    printLine.close();
  }
  
}