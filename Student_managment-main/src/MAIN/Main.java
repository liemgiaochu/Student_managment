/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MAIN;

import GUI.Login;
import MANAGER.LoginListener;
/**
 *
 * @author zink.urq00
 */
public class Main {
    public static void main(String[] args) {
        // Create and display the login form
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Login(new LoginListener()).setVisible(true);
            }
        });
    }
}
