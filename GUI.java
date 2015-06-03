/*
Computer Science Final Project
Author: Blake Sekelsky

GUI.java
Holds the UI frontend of the program, as well as backend functions for running
the Perl scripts and opening/saving in the editor
*/

import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Component;
import java.nio.file.Files;
import java.util.logging.Level;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GUI
{
   //Initializing global variables and GUI elements
   public JFrame frame = new JFrame("PerlWrite");
   public JTextArea text = new JTextArea(); 
   public JScrollPane scroll = new JScrollPane(text);
   public String activeScript = "";
   public String activeScriptpwd = "";
   public String activeScriptname = "";
   public File f;
   public Path p;
   public String file;
    
    void createGUI(){
        //System.setProperty("apple.laf.useScreenMenuBar", "true");
        // Creates a menubar for a JFrame
        JMenuBar menuBar = new JMenuBar();
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        frame.add(scroll);
                 
        // Define and add two drop down menu to the menubar
        JMenu fileMenu = new JMenu("File");
        JMenu Console = new JMenu("Console");
        menuBar.add(fileMenu);
        menuBar.add(Console);
         
        //Adding Menu Items
        JMenuItem newAction = new JMenuItem("New");
        JMenuItem saveAction = new JMenuItem("Save");
        JMenuItem executeAction = new JMenuItem("Execute");
        JMenuItem clearConsoleAction = new JMenuItem("Clear Console");
        JMenuItem openAction = new JMenuItem("Open");
        JMenuItem exitAction = new JMenuItem("Exit");
        
        //Adds buttns group to the menu bar
        ButtonGroup bg = new ButtonGroup();
        fileMenu.add(newAction);
        fileMenu.add(saveAction);
        fileMenu.add(openAction);
        fileMenu.addSeparator();
        fileMenu.add(exitAction);
        Console.add(executeAction);
        Console.add(clearConsoleAction);

        //Listeners
        clearConsoleAction.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                System.out.print("\f");
            }
        });
        
        //Saves file to the program's local directory
        saveAction.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                try{
                    file = JOptionPane.showInputDialog(null, "Enter perl script name to save: ");
                    String s = text.getText();
                    byte writeData[] = s.getBytes();
                    FileOutputStream out = new FileOutputStream(file);
                    out.write(writeData);
                    out.flush();
                    out.close();
                }
                catch(Exception ex3){
                    System.out.println(ex3.toString());
                }
            }
        });
        
        //Opens file in the program's local directory
        openAction.addActionListener(new ActionListener(){
           public void actionPerformed(ActionEvent e){
               try{
                   file = JOptionPane.showInputDialog(null, "Enter perl script name to open: ");
                   p = Paths.get(file);
                    
                   String str ;
                   try {

                       byte bt[]= Files.readAllBytes(p);   
                       str=new String(bt,"UTF-8");
                       text.setText(str);
                   }
                   catch (IOException ex) {
                       System.out.println(ex.toString());
                   }
               }
               catch(Exception ex2){
                   System.out.println(ex2.toString());
               }
            }
        });
        
        newAction.addActionListener(new ActionListener(){
           public void actionPerformed(ActionEvent e){
               text.setText("");
            }
        });
        
        executeAction.addActionListener(new ActionListener(){
 
            public void actionPerformed(ActionEvent e){
                executeScript(activeScript); //Calls executeScript with the script's file location variable
            }
        });
        
        exitAction.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                System.exit(0);
            }
        });
        
        
        //Frame declarations
        frame.setJMenuBar(menuBar);
        frame.setResizable(true);
        frame.setSize(640, 480);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
   }
   
   void executeScript(String fileName){
        Process process; //Creates new process

        try{
            //runs a terminal command that runs the Perl Script
            String cmd = ("perl " + file);
            
            System.out.println(cmd);
            process = Runtime.getRuntime().exec(cmd); 
            process.waitFor();
            
            /*
             * if the process returns 0, read the program's buffer and print it out.
             */
            if(process.exitValue() == 0){
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line = null;
                    while ((line = in.readLine()) != null) {
                        System.out.println(line);
                    }
                }    
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                System.out.println("Command Failure");
            }
        }
        catch(Exception e){
            System.out.println("Exception: "+ e.toString());
        }
   }
}
