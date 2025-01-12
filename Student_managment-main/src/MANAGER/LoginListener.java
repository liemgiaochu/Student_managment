/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MANAGER;

import GUI.Menu;
import GUI.Login;

/**
 *
 * @author zink.urq00
 */

public class LoginListener implements GUI.LoginListener{
    private Login loginScreen;
    
    public void setLoginScreen(Login screen) {
        this.loginScreen = screen;
    }
    
    @Override
    public void onLoginSuccess() {
        if (loginScreen != null) {
            loginScreen.dispose(); // Đóng Login window 
            
            // Mở Home window
            java.awt.EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new Menu().setVisible(true);
                }
            });
        }
    }
    
}
