/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coe528.project;

import java.util.*;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;



public class Manager {
    
    private String username = "admin";
    private String password = "admin";
    private String role = "manager";
    
    
   
    public Manager(){
       try{
            File loginInfo = new File(username + ".txt");
            if(loginInfo.createNewFile() == true){
                FileWriter writeToFile = new FileWriter(username + ".txt");
                writeToFile.write(username + "\n");
                writeToFile.write(password);
                writeToFile.close();
            }
        }catch(IOException e){
            System.out.println("IOException occured");
        }
 
    }
    
    public Customer addCustomer(String username, String password){
        return new Customer(username,password);
    }
    
    //Right now this exposes the rep, I probably need to change this shit
    public void deleteCustomer(Customer custToDelete){
        //First we must delete the text file, then point 
        File file = new File(custToDelete.getUsername() + ".txt");
        if(file.delete())
             AlertBox.display("Confirmation", "File successfully deleted");


        
        
    }
    
}
