import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Sugar {

  JFrame frame = new JFrame("Blood Sugar Data");
  CardLayout c = new CardLayout();
  JPanel panelCont = new JPanel(c);
  
  JPanel initPanel = new JPanel();
  JLabel welcome = new JLabel("MAIN MENU");
  JButton prevDataButton = new JButton("View trends");
  JButton newDataButton = new JButton("Enter new data");
  JPanel buttons = new JPanel();
  
  JPanel entryNumberPanel = new JPanel();
  JPanel buttonPanel = new JPanel();
  JButton returnToMain3 = new JButton("Return to main menu");
  JLabel entryNumberLabel = new JLabel("Enter how many entries to graph: ");
  JButton viewData = new JButton("View Graphs");
  JTextField entryNumber = new JTextField(7);
  JPanel enterNumber = new JPanel();
  
  JPanel graphButtons = new JPanel();
  JButton returnToMain1 = new JButton("Return to main menu");
  JButton goBack = new JButton("Back");
  
  JPanel newDataPanel = new JPanel();
  JLabel dateLabel = new JLabel("Enter the date: ");
  JLabel sugarLabel = new JLabel("Enter the sugar level: ");
  JButton enterData = new JButton("Save data");
  JButton returnToMain2 = new JButton("View Trends");
  JTextField sugarData = new JTextField(7);
  JPanel datePanel = new JPanel();
  JPanel sugarPanel = new JPanel();
  
  String str1, str2;
  ArrayList<Point> dataTotal = new ArrayList<Point>();
  
  String file_name = "dateFile.txt";
  int count, entries;
  
  public Sugar() {
  
    graphButtons.add(goBack);
    graphButtons.add(returnToMain1);
    
    DateFormat df = new java.text.SimpleDateFormat("yyyy/MM/dd");
    Date d = Calendar.getInstance().getTime();
    JFormattedTextField date = new JFormattedTextField(df.format(d));
   
    buttons.setLayout(new GridLayout(3,1));
    welcome.setHorizontalAlignment(JLabel.CENTER);
    welcome.setFont(new Font("Cambria", 1, 30));
    buttons.add(welcome);
    buttons.add(prevDataButton);
    buttons.add(newDataButton);
    initPanel.setLayout(new GridBagLayout());
    GridBagConstraints constraints = new GridBagConstraints();
    constraints.anchor = GridBagConstraints.CENTER;
    initPanel.add(buttons, constraints);
    initPanel.validate();
    
    entryNumberPanel.setLayout(new GridBagLayout());
    GridBagConstraints constr = new GridBagConstraints();
    constr.anchor = GridBagConstraints.CENTER;
    constr.gridy = 0;
    enterNumber.add(entryNumberLabel);
    enterNumber.add(entryNumber);
    entryNumberPanel.add(enterNumber, constr);
    constr.gridy = 1;
    buttonPanel.add(returnToMain3);
    buttonPanel.add(viewData);
    entryNumberPanel.add(buttonPanel, constr);
    entryNumberPanel.validate();
    
    entryNumber.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent evt) {

        viewData.doClick();
      }
    });
    
    entryNumber.getDocument().addDocumentListener(new DocumentListener(){
    
        @Override
        public void insertUpdate(DocumentEvent de)
        {
            viewData.setEnabled(true);
        }
        public void changedUpdate(DocumentEvent de)
        {
            viewData.setEnabled(true);
        }
        public void removeUpdate(DocumentEvent de)
        {
            viewData.setEnabled(true);
        }
    });
        
    datePanel.add(dateLabel);
    datePanel.add(date);
    sugarPanel.add(sugarLabel);
    sugarPanel.add(sugarData);
    date.setColumns(7);
    newDataPanel.setLayout(new GridBagLayout());
    GridBagConstraints cont = new GridBagConstraints();
    cont.gridy = 0;
    cont.ipady = 10;
    newDataPanel.add(datePanel, cont);
    cont.gridy = 1;
    newDataPanel.add(sugarPanel, cont);
    cont.gridy = 2;
    cont.ipady = 0;
    newDataPanel.add(enterData, cont);
    cont.gridy = 3;
    newDataPanel.add(returnToMain2, cont);
    resetDate(d, df, date);
    newDataPanel.validate();
    
    date.addActionListener(new ActionListener() {
    
      @Override
      public void actionPerformed(ActionEvent evt) {
      
        sugarData.requestFocusInWindow();
      }
    });
    
    sugarData.addActionListener(new ActionListener() {
    
      @Override
      public void actionPerformed(ActionEvent evt) {
      
        enterData.doClick();
      }
    });
    
    sugarData.getDocument().addDocumentListener(new DocumentListener(){
    
        @Override
        public void insertUpdate(DocumentEvent de)
        {
            enterData.setEnabled(true);
        }
        public void changedUpdate(DocumentEvent de)
        {
            enterData.setEnabled(true);
        }
        public void removeUpdate(DocumentEvent de)
        {
            enterData.setEnabled(true);
        }
    });
    
    panelCont.add(initPanel, "1");
    panelCont.add(newDataPanel, "3");
    panelCont.add(entryNumberPanel, "4");
    c.show(panelCont, "1");
  
    //from main menu to input # values  
    prevDataButton.addActionListener(new ActionListener() {
    
      @Override
      public void actionPerformed(ActionEvent evt) {
      
        c.show(panelCont, "4");
        entryNumber.setText(null);
        entryNumber.requestFocusInWindow();
        count = 0; 
        entries = 0;
      }
    });
    
    //from main menu to new data entering
    newDataButton.addActionListener(new ActionListener() {
    
      @Override
      public void actionPerformed(ActionEvent evt) {
      
        c.show(panelCont, "3");
        sugarData.setText(null);
        resetDate(d, df, date);
        date.requestFocusInWindow();
      }
    });   
    
    //from input # values to graph
    viewData.addActionListener(new ActionListener() {
    
      @Override
      public void actionPerformed(ActionEvent evt) {
      
        entries = 0;
        count = 0;
      
        try {
        
          entries = Integer.parseInt(entryNumber.getText());
          InRead blah = new InRead(file_name);
          dataTotal = blah.readFile();
           
          if (dataTotal.size() > entries) {
          
            count = dataTotal.size() - entries;
          }
          else count = 0;
                    
          JPanel prevDataGraph = new MyPanel(dataTotal, blah, count);
          prevDataGraph.setLayout(new BorderLayout());
          prevDataGraph.add(graphButtons, BorderLayout.NORTH);
          prevDataGraph.validate();
          panelCont.add(prevDataGraph, "2");
          c.show(panelCont, "2");
        }
        catch(NumberFormatException e) {
        
          JOptionPane.showMessageDialog(null, "Enter a valid range", "ERROR",
                                        JOptionPane.WARNING_MESSAGE);
          c.show(panelCont, "4");
          entryNumber.setText(null);
          entryNumber.requestFocusInWindow();
        }
      }
    });
    
    //from graph to input # values
    goBack.addActionListener(new ActionListener() {
    
      @Override
      public void actionPerformed(ActionEvent evt) {
      
        c.show(panelCont, "4");
        entryNumber.setText(null);
        entryNumber.requestFocusInWindow();
        count = 0;
        entries = 0;
      }
    });
        
    //from graph to main menu
    returnToMain1.addActionListener(new ActionListener() {
    
      @Override
      public void actionPerformed(ActionEvent evt) {
      
        c.show(panelCont, "1");
      }
    });
    
    //from new data to view data
    returnToMain2.addActionListener(new ActionListener() {
    
      @Override
      public void actionPerformed(ActionEvent evt) {
      
        c.show(panelCont, "4");
        entryNumber.setText(null);
        entryNumber.requestFocusInWindow();
        count = 0; 
        entries = 0;
      }
    });
    
    //from input # values to main menu
    returnToMain3.addActionListener(new ActionListener() {
    
      @Override
      public void actionPerformed(ActionEvent evt) {
      
        c.show(panelCont, "1");
      }
    });
    
    //save data button
    enterData.addActionListener(new ActionListener() {
    
      @Override
      public void actionPerformed(ActionEvent evt) {
      
        str1 = date.getText();
        str2 = sugarData.getText();
        
        if(!isValidDate(str1)) {
        
          JOptionPane.showMessageDialog(null, "Invalid Date Entered", "ERROR",
                                        JOptionPane.WARNING_MESSAGE);
          resetDate(d, df, date);
          date.requestFocusInWindow();
        }        
        else if (str2.equals("")) {
        
          JOptionPane.showMessageDialog(null, "No Sugar Level Data Entered", "ERROR",
                                        JOptionPane.WARNING_MESSAGE);
          sugarData.requestFocusInWindow();
        }
        else {
        
          try {
            WriteFile dateData = new WriteFile(file_name, true);
            dateData.writeToFile(str1);
            dateData.writeToFile(str2);
          }
          catch(IOException e) {
            JOptionPane.showMessageDialog(null, "Error writing to file", "ERROR",
                                        JOptionPane.WARNING_MESSAGE);
          }
          
          JOptionPane.showMessageDialog(null, "Data Saved", "CONFIRMATION", 
                                        JOptionPane.INFORMATION_MESSAGE);
          resetDate(d, df, date);
          sugarData.setText(null);
          date.requestFocusInWindow();
        }
      }
    });
        
    frame.add(panelCont);
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.setSize(500, 400);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }
  
  private void resetDate(Date d, DateFormat df, JFormattedTextField date) {
   
    date.setValue(df.format(d));
    try {
      MaskFormatter dateMask = new MaskFormatter("####/##/##");
      dateMask.install(date);
    } 
    catch (java.text.ParseException exc) {
      System.err.println("formatter is bad: " + exc.getMessage());
      System.exit(-1);
    }
  }
  
  private void printArray(ArrayList<Point> array) {
  
    for(int i = 0; i < array.size(); i++) {
    
      System.out.println(array.get(i).getX() + " " + array.get(i).getY());
    }
  }
  
  public boolean isValidDate(String date) {
  
    DateFormat sdf = new java.text.SimpleDateFormat("yyyy/MM/dd");
    Date testDate = null;
    try 
    {
      testDate = sdf.parse(date);
    }
    catch (ParseException e) 
    {
      return false;
    }
    if (!sdf.format(testDate).equals(date)) {
      return false;
    }
    return true;
  }
  
  public static void main(String[] args){
  
    SwingUtilities.invokeLater(new Runnable() {  
      @Override
      public void run() {   
        new Sugar();
      }
    });
  }
   
}