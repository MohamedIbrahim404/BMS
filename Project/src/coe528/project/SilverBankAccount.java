/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coe528.project;


public class SilverBankAccount extends BankAccount {
    
    public void onlinePurchase(int amount){
        if(balance >= (amount + 20)){
            balance -= (amount + 20);
            //System.out.println("You have made an online purchase of $" + (amount + 20));
            AlertBox.display("Silver Member Purchase Confirmation", "You have made a purchase of $" + amount +"\nand with the additional service charge of $20\nthe total cost is $" + (amount +20));    
        }
        else
            AlertBox.display("Error Message", "You do not have the funds to make a puchase of $" + amount +"\n which has a total cost of $" + (amount+20));
    }
    public String getAccountLevel(){
        return "You are a Silver Member";
    }
    
}
