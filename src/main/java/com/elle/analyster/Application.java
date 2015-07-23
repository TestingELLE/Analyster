package com.elle.analyster;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Created with IntelliJ IDEA.
 * User: danielabecker
 * Date: 5/25/15
 * Time: 7:32 PM
 * To change this template use File | Settings | File Templates.
 */
//@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        
        

        //ConfigurableApplicationContext applicationContext = new SpringApplicationBuilder(Application.class).headless(false).run(args);
        //System.out.println("Enter the main");
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
            //</editor-fold>
        /* Create and display the form */
            java.awt.EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {

                    //Analyster analyster = applicationContext.getBean(Analyster.class);
                    //analyster.setVisible(false);
                    //new LoginWindow(analyster).setVisible(true);
                    
                    
                    // set the look and feel
                    try {
                        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                            if ("Nimbus".equals(info.getName())) {
                                javax.swing.UIManager.setLookAndFeel(info.getClassName());
                                break;
                            }
                        }
                    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
                        java.util.logging.Logger.getLogger(Analyster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    }

                    // this is the first window that is shown to log in
                    // to the database.
                    // Once the database connection is made, then an instance
                    // of Analyster is created.
                    LoginWindow loginWindow = new LoginWindow();
                    loginWindow.setLocationRelativeTo(null);
                    loginWindow.setVisible(true);
                    
                }
            });
    }

}
