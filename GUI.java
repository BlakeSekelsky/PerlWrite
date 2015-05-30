/*
Computer Science Final Project
Author: Blake Sekelsky

GUI.java

Holds the UI frontend of the program, as well as backend functions for running the Perl scripts and opening/saving in the editor
*/

import java.io.*;
import java.awt.FlowLayout;
import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Component;
import java.nio.file.Files;
import java.util.logging.Level;

public class GUI
{
   public JFrame frame = new JFrame("PerlWrite");
   public JPanel panel = new JPanel();
   public JTextArea text = new JTextArea(); 
   public JScrollPane scroll = new JScrollPane(text);
    
    void createGUI(){
        //Setup panel
        panel.setLayout(new BorderLayout());
        panel.add(scroll, BorderLayout.CENTER); 
        
        //Frame declarations
        frame.add(panel);
        frame.setResizable(false);
        frame.setSize(640, 480);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
   }
   
   void sideGUI(){
        //Create Window
        JFrame frame2 = new JFrame("Actions");
        
        //Run Button
        JButton runBtn = new JButton("Run Script");
        runBtn.setHorizontalAlignment(SwingConstants.CENTER);
        runBtn.setLocation(10, 20);
        runBtn.setSize(105, 50);
        
        //Clear Console Button
        JButton clrBtn = new JButton("Clear Console");
        clrBtn.setHorizontalAlignment(SwingConstants.CENTER);
        clrBtn.setLocation(125, 20);
        clrBtn.setSize(105, 50);
        
        //New Script Button
        JButton newBtn = new JButton("New Script");
        newBtn.setHorizontalAlignment(SwingConstants.CENTER);
        newBtn.setLocation(10, 80);
        newBtn.setSize(220, 50);
        
        //Open Script Button
        JButton opnBtn = new JButton("Open Script");
        opnBtn.setHorizontalAlignment(SwingConstants.CENTER);
        opnBtn.setLocation(10, 140);
        opnBtn.setSize(220, 50);
        
        //Adding UI
        frame2.add(clrBtn);
        frame2.add(runBtn);
        frame2.add(newBtn);
        frame2.add(opnBtn);

        //Init Window
        frame2.setLayout(null);
        frame2.setResizable(false);
        frame2.setSize(240, 240);        
        frame2.setLocationRelativeTo(null);
        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame2.setVisible(true);
        
        //Action Handlers
        opnBtn.addActionListener(new ActionListener(){
           public void actionPerformed(ActionEvent e){
               try{
                   JFileChooser fileChooser = new JFileChooser();
                   fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                   int returnVal = fileChooser.showOpenDialog(null);
                   File selectedFile = fileChooser.getSelectedFile();
                    
                   String str ;
                   try {
                       byte bt[]= Files.readAllBytes(selectedFile.toPath());   
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
        
        newBtn.addActionListener(new ActionListener(){
           public void actionPerformed(ActionEvent e){
               text.setText("");
            }
        });
        
        clrBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                System.out.print("\f");
            }
        });
        
        runBtn.addActionListener(new ActionListener(){
 
            public void actionPerformed(ActionEvent e){
                executeScript("test.pl");
            }
        });
    }
   
   void executeScript(String fileName){
        Process process;

        try{
            process = Runtime.getRuntime().exec("perl " + fileName);
            process.waitFor();
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

