/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.demos.kitchen;

import com.codename1.l10n.L10NManager;
import com.codename1.ui.Container;
import com.codename1.ui.Form;
import com.codename1.ui.NavigationCommand;
import com.codename1.ui.layouts.BorderLayout;
import java.util.Date;

/**
 *
 * @author azuz
 */
public class Stat extends Form {
    
     
    private final Container subCenterContainer ;
   // private WebBrowser browser ;
    
    public Stat() {
        
     this.setLayout(new BorderLayout());
     
      subCenterContainer = new Container() ;
  //   browser = new WebBrowser();
//     BrowserComponent browser = new BrowserComponent();
//browser.setURL("http://localhost/thuggies/web/app_dev.php/Graphe/histo");
    
 subCenterContainer.setLayout(new BorderLayout());
// subCenterContainer.addComponent(BorderLayout.CENTER, browser);
// browser.setURL("https://www.google.com");
 

  
     this.setTitle("Statistique") ;
      //this.add(BorderLayout.NORTH, browser);
   
                
       NavigationCommand cmd3 = new NavigationCommand("Statistique");
        cmd3.setNextForm(this);
       // SeeAllRandonnees a = new SeeAllRandonnees();
        this.getToolbar().addCommandToSideMenu(cmd3);
      //  a.getToolbar().addCommandToSideMenu(cmd3);
        
            
        this.getContentPane().addPullToRefresh(() -> {
    this.add("Pulled at " + L10NManager.getInstance().formatDateTimeShort(new Date()));
    });
 
    }
    
    
    
    
}
