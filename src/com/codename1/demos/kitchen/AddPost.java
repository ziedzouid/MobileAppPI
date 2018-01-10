/*
 * Copyright (c) 2012, Codename One and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Codename One designates this
 * particular file as subject to the "Classpath" exception as provided
 * in the LICENSE file that accompanied this code.
 *  
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 * 
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 * Please contact Codename One through http://www.codenameone.com/ if you 
 * need additional information or have any questions.
 */
package com.codename1.demos.kitchen;

import com.codename1.capture.Capture;
import com.codename1.components.InfiniteProgress;
import com.codename1.components.MediaPlayer;
import com.codename1.components.MultiButton;
import com.codename1.components.ToastBar;
import static com.codename1.demos.kitchen.KitchenSink.res;
import com.codename1.entities.kitchen.Poste;
import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.FileSystemStorage;
import com.codename1.io.JSONParser;
import com.codename1.io.Log;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.io.Util;
import com.codename1.l10n.SimpleDateFormat;
import com.codename1.media.Media;
import com.codename1.media.MediaManager;
import com.codename1.ui.Button;
import com.codename1.ui.ComboBox;
import com.codename1.ui.Component;
import com.codename1.ui.ComponentGroup;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.TextField;
import com.codename1.ui.animations.CommonTransitions;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.spinner.Picker;
import com.codename1.ui.table.TableLayout;
import com.twilio.Twilio;
import com.twilio.type.PhoneNumber;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * You can play videos either from remote or local sources very easily in Codename One, here we also
 * show the ability to record a video that we can playback later.
 *
 * @author Shai Almog
 */
public class AddPost  extends Demo {
    //static Label contenu,arrive,depart,date;
    //Container comps;
    int temp;
   
    public static final String ACCOUNT_SID = "AC6452be109fb5ab987f6a54860a684364";
         public static final String AUTH_TOKEN = "b280c0c061cbd6e1c37944024cdfd658";

    public String getDisplayName() {
        return "addPost";
    }

    public Image getDemoIcon() {
        return getResources().getImage("input.png");
    }

    @Override
    public String getDescription() {
        return "Demonstrates basic usage of input facilities, device orientation behavior as well as adapting the UI to tablets."
                + "This demo shows off a typical form with user information, different keyboard types, ability to capture an "
                + "avatar image and style it etc.";
    }

    @Override
    public String getSourceCodeURL() {
        return "https://github.com/codenameone/KitchenSink/blob/master/src/com/codename1/demos/kitchen/Input.java";
    }

    private void addComps(Form parent, Container cnt, Component... cmps) {
        if (Display.getInstance().isTablet() || !Display.getInstance().isPortrait()) {
            TableLayout tl = new TableLayout(cmps.length / 2, 2);
            cnt.setLayout(tl);
            tl.setGrowHorizontally(true);
            for (Component c : cmps) {
                if (c instanceof Container) {
                    cnt.add(tl.createConstraint().horizontalSpan(2), c);
                } else {
                    cnt.add(c);
                }
            }
        } else {
            cnt.setLayout(BoxLayout.y());
            for (Component c : cmps) {
                cnt.add(c);
            }
        }
        if (cnt.getClientProperty("bound") == null) {
            cnt.putClientProperty("bound", "true");
            if (!Display.getInstance().isTablet()) {
                parent.addOrientationListener((e) -> {
                    Display.getInstance().callSerially(() -> {
                        cnt.removeAll();
                        addComps(parent, cnt, cmps);
                        cnt.animateLayout(800);
                    });
                });
            }
        }
    }

    @Override
    public Container createDemo(Form parent) {
                   Container comps = new Container(BoxLayout.y());
        //Button delete = new Button("Delete");
        //Button Edit = new Button("Edit");
        Button Add=new Button("Add");
         ConnectionRequest con = new ConnectionRequest();
      //  con.setUrl("http://localhost/TestMobile/web/app_dev.php/9613628/showPosteUser");
    
        
          //  con.addResponseListener(new ActionListener<NetworkEvent>()
              // comps.add(new Label());
              
          Label depart=new Label("Departure");
        ComboBox dep = new ComboBox();
              dep.addItem("Ariena");
              dep.addItem("Tunis");
              dep.addItem("Beja");
              dep.addItem("tatouine");
             

 Label arrive=new Label("Destination");
        ComboBox arr = new ComboBox();
        arr.addItem("Ariena");
        arr.addItem("Tunis");
        arr.addItem("Beja");
        arr.addItem("tatouine");
           Label db=new Label("Date ");
    Picker datePoste = new Picker();
   datePoste.setType(Display.PICKER_TYPE_DATE);

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
              
            datePoste.setFormatter(format);
          Label contenu=new Label("Content ");
          TextField t=new TextField();
         comps.add(depart);
         comps.add(dep);
         comps.add(arrive);
         comps.add(arr);
        // comps.add(db);
         //comps.add(datePoste);
         comps.add(contenu);
         comps.add(t);
     

          comps.setScrollableY(true);
        comps.setUIID("PaddedContainer");

Container content = BorderLayout.center(comps);
        
        
        Container ctnbt = new Container(new BoxLayout(BoxLayout.Y_AXIS));
     //ctnbt.add(delete);
       //ctnbt.add(Edit);
       ctnbt.add(Add);

        content.add(BorderLayout.SOUTH, ctnbt);

        UserDAO userDAO = new UserDAO();
        System.out.println(userDAO.user.id);
        Add.addActionListener(new ActionListener() {
           
                                  

                       @Override
                       public void actionPerformed(ActionEvent evt) {
                     ConnectionRequest req = new ConnectionRequest();
                    // int foo = ;

                     
                req.setUrl("http://localhost/TestMobile/web/app_dev.php/newPoste?&id="+userDAO.user.id+"&jour"+datePoste.getDate()+ "&contenu="+ t.getText() + "&depart=" + dep.getSelectedItem() + "&arrive="+arr.getSelectedItem() + "");
                            System.out.println(t.getText());
                            
                       System.out.println(datePoste.getDate());

                          System.out.println(dep.getSelectedItem() );
                           System.out.println(arr.getSelectedItem()); 

                Demo d = new Video();
            d.init(res);
            Form f = new Form("Posts", new BorderLayout());
            f.add(BorderLayout.CENTER, d.createDemo(f));
            Form previous = Display.getInstance().getCurrent();
            f.getToolbar().setBackCommand(" ", ee -> {
                if (d.onBack()) {
                    previous.showBack();
                }
            });
            f.show();   NetworkManager.getInstance().addToQueue(req); 
                       
                Twilio.init("AC6452be109fb5ab987f6a54860a684364", "b280c0c061cbd6e1c37944024cdfd658");
 com.twilio.rest.api.v2010.account.Message message = com.twilio.rest.api.v2010.account.Message.creator(new PhoneNumber("+21692714774"),
       new PhoneNumber("+18452456432"),"your Post was added").create();}               

                   });
        /*delete.addActionListener(e -> {
            ToastBar.showMessage("Connecting...", FontImage.MATERIAL_ACCOUNT_BALANCE_WALLET);
            //userDAO.RegisterUser();
            userDAO.loginUser();
        });

        Edit.addActionListener(e -> {
            Demo d = new Video();
            d.init(res);
            Form f = new Form("REGISTER", new BorderLayout());
            f.add(BorderLayout.CENTER, d.createDemo(f));
            Form previous = Display.getInstance().getCurrent();
            f.getToolbar().setBackCommand(" ", ee -> {
                if (d.onBack()) {
                    previous.showBack();
                }
            });
            f.show();
        });
*/
        content.setUIID("InputContainerForeground");

        Button avatar = new Button("");
        avatar.setUIID("InputAvatar");
        Image defaultAvatar = FontImage.createMaterial(FontImage.MATERIAL_CAMERA, "InputAvatarImage", 8);
        Image circleMaskImage = getResources().getImage("circle.png");
        defaultAvatar = defaultAvatar.scaled(circleMaskImage.getWidth(), circleMaskImage.getHeight());
        defaultAvatar = ((FontImage) defaultAvatar).toEncodedImage();
        Object circleMask = circleMaskImage.createMask();
        defaultAvatar = defaultAvatar.applyMask(circleMask);
        avatar.setIcon(defaultAvatar);

        avatar.addActionListener(e -> {
            if (Dialog.show("Camera or Gallery", "Would you like to use the camera or the gallery for the picture?", "Camera", "Gallery")) {
                String pic = Capture.capturePhoto();
                if (pic != null) {
                    try {
                        Image img = Image.createImage(pic).fill(circleMaskImage.getWidth(), circleMaskImage.getHeight());
                        avatar.setIcon(img.applyMask(circleMask));
                    } catch (IOException err) {
                        ToastBar.showErrorMessage("An error occured while loading the image: " + err);
                        Log.e(err);
                    }
                }
            } else {
                Display.getInstance().openGallery(ee -> {
                    if (ee.getSource() != null) {
                        try {
                            Image img = Image.createImage((String) ee.getSource()).fill(circleMaskImage.getWidth(), circleMaskImage.getHeight());
                            // Image img = Image.createImage("/temp6510064849086438710.jpg").fill(circleMaskImage.getWidth(), circleMaskImage.getHeight());
                            System.out.println((String) ee.getSource());
                            avatar.setIcon(img.applyMask(circleMask));
                        } catch (IOException err) {
                            ToastBar.showErrorMessage("An error occured while loading the image: " + err);
                            Log.e(err);
                        }
                    }
                }, Display.GALLERY_IMAGE);
            }
        });

        Container actualContent = LayeredLayout.encloseIn(content,
                FlowLayout.encloseCenter(avatar));

        Container  Video;
        if (!Display.getInstance().isTablet()) {
            Label placeholder = new Label(" ");

            Component.setSameHeight(actualContent, placeholder);
            Component.setSameWidth(actualContent, placeholder);

             Video = BorderLayout.center(placeholder);

            parent.addShowListener(e -> {
                if (placeholder.getParent() != null) {
                     Video.replace(placeholder, actualContent, CommonTransitions.createFade(1500));
                }
            });
        } else {
          Video = BorderLayout.center(actualContent);
        }
         Video.setUIID("InputContainerBackground");

        return  Video;
    }
  
    



}
