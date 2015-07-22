package com.elle.analyster.configuration;

import com.elle.analyster.Analyster;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created with IntelliJ IDEA.
 * User: danielabecker
 * Date: 6/1/15
 * Time: 7:08 PM
 * To change this template use File | Settings | File Templates.
 */
@Configuration
public class AnalysterConfiguration {

    @Bean
    public Analyster analyster() {
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
        
        Analyster analyster = new Analyster();
        return analyster;
    }
}
