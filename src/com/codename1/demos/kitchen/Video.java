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
import com.codename1.components.ImageViewer;
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
import com.codename1.media.Media;
import com.codename1.media.MediaManager;
import com.codename1.ui.Button;
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
import com.codename1.ui.table.TableLayout;
import com.codename1.ui.util.Resources;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * You can play videos either from remote or local sources very easily in
 * Codename One, here we also show the ability to record a video that we can
 * playback later.
 *
 * @author Shai Almog
 */
public class Video extends Demo {

    static TextField emaillog, passwordlog;
    //static Label contenu,arrive,depart,date;
    //Container comps;
    int temp;
    Resources theme;

    public String getDisplayName() {
        return "Posts";
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
        Container comps = new Container();
      
        ConnectionRequest con = new ConnectionRequest();
        con.setUrl("http://localhost/TestMobile/web/app_dev.php/showPoste");

        //emaillog = new TextField("", "E-mail", 20, TextField.EMAILADDR);
//        FontImage.setMaterialIcon(emaillog.getHintLabel(), FontImage.MATERIAL_EMAIL);
        //passwordlog = new TextField("", "Password", 20, TextField.PASSWORD);
        //      FontImage.setMaterialIcon(passwordlog.getHintLabel(), FontImage.MATERIAL_LOCK);
        con.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                for (Poste t : getListEtudiant(new String(con.getResponseData()))) {
                    /* addComps(parent, comps, new Label(UserDAO.user.nom, "InputContainerLabel"),
                            new Label("Date of going" + t.getDatePost(), "InputContainerLabel"),
                            new Label("Hello I would like to go from" + t.getDepart() + "To" + t.getArrive(), "InputContainerLabel"),
                            new Label("Ps" + t.getContenu(), "InputContainerLabel")
                            
                    );*/
                  //ImageViewer imge=new  ImageViewer(theme.getImage("no_image_user.png"));
                  // comps.add(imge);
             Image circleImage = getResources().getImage("post.png").scaled(100,100);
             comps.add(circleImage);

                    comps.add(new Label(UserDAO.user.nom, "InputContainerLabel"));
                    comps.add(new Label("Date of going" + t.getDatePost(), "InputContainerLabel"));
                    comps.add(new Label("Hello I would like to go from" + t.getDepart() + "To" + t.getArrive(), "InputContainerLabel"));
                    comps.add(   new Label("Ps:"+"" + t.getContenu(), "InputContainerLabel"));
        Button delete = new Button("Delete");
        Button Edit = new Button("Edit");
                  //  System.out.println(t.getUser_id());
       if(UserDAO.user.id==t.getUser_id())
       { comps.add(delete);
                    comps.add(Edit);}
        delete.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent evt) {
                       Dialog k = new Dialog();
                            
                            if (Dialog.show("Confirmation", "Poste successfuly deleted", "Ok", null)) {
                                ConnectionRequest req = new ConnectionRequest();
                                
                                req.setUrl("http://localhost/TestMobile/web/app_dev.php/" + t.getId()+ "/Delete" + "");
                                //System.out.println(t.getIdChambre());
                                NetworkManager.getInstance().addToQueue(req);
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
            f.show();
                            }   }
                    });
            Edit.addActionListener(e -> {
              EditPost u=new EditPost(t);
               u.getF().show();
        });

                }

            }
        });
        NetworkManager.getInstance().addToQueue(con);

        comps.setScrollableY(true);
        comps.setUIID("PaddedContainer");

        Container content = BorderLayout.center(comps);

        // delete.setUIID("InputAvatarImage");
        // Container ctnbt = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        //ctnbt.add(delete);
        //ctnbt.add(Edit);
        // content.add(BorderLayout.SOUTH, ctnbt);
        UserDAO userDAO = new UserDAO();
        /* delete.addActionListener(e -> {
            ToastBar.showMessage("Connecting...", FontImage.MATERIAL_ACCOUNT_BALANCE_WALLET);
            //userDAO.RegisterUser();
            userDAO.loginUser();
        });*/

    

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

       /* Container actualContent = LayeredLayout.encloseIn(content,
                FlowLayout.encloseCenter(avatar));*/

        Container Video;
        if (!Display.getInstance().isTablet()) {
            Label placeholder = new Label(" ");

            Component.setSameHeight(content, placeholder);
            Component.setSameWidth(content, placeholder);

            Video = BorderLayout.center(placeholder);

            parent.addShowListener(e -> {
                if (placeholder.getParent() != null) {
                    Video.replace(placeholder, content, CommonTransitions.createFade(1500));
                }
            });
        } else {
            Video = BorderLayout.center(content);
        }
        Video.setUIID("InputContainerBackground");

        return Video;
    }

    public ArrayList<Poste> getListEtudiant(String json) {
        ArrayList<Poste> listePostes = new ArrayList<>();
        System.out.println("JSON*************\n" + json);
        try {

            JSONParser j = new JSONParser();

            Map<String, Object> postes = j.parseJSON(new CharArrayReader(json.toCharArray()));

            System.out.println();
            List<Map<String, Object>> liste = (List<Map<String, Object>>) postes.get("root");

            for (Map<String, Object> o : liste) {
                Poste e = new Poste();
                //id, json, status);
                e.setId((int) Float.parseFloat(o.get("id").toString()));
                Map<String, Object> data2 = (Map<String, Object>) (o.get("datePost"));
                temp = (int) Float.parseFloat(data2.get("timestamp").toString());
                Date myDate = new Date(temp * 1000L);
                e.setDatePost(myDate);
                //  e.setContenu(e.toString());
                System.out.println(o.get("userId").toString());
              e.setUser_id((int) Float.parseFloat(o.get("userId").toString()));
                e.setContenu(o.get("contenu").toString());
                e.setArrive(o.get("arrive").toString());
                e.setDepart(o.get("depart").toString());
                // System.out.println(o);

                // System.out.println(e.toString());
                //System.out.println(o);
                listePostes.add(e);
                //System.out.println(listePostes);
            }

        } catch (IOException ex) {
        }
        return listePostes;

    }
  

}
