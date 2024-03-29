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

import com.codename1.components.FloatingActionButton;
import com.codename1.components.FloatingHint;
import com.codename1.components.InfiniteProgress;
import com.codename1.components.MultiButton;
import com.codename1.components.ShareButton;
import com.codename1.components.ToastBar;
import com.codename1.contacts.Contact;
import com.codename1.io.Util;
import com.codename1.l10n.L10NManager;
import com.codename1.messaging.Message;
import com.codename1.ui.Button;
import com.codename1.ui.Command;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Font;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import com.codename1.ui.SwipeableContainer;
import com.codename1.ui.TextField;
import com.codename1.ui.events.ScrollListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.table.TableLayout;
import com.codename1.util.CaseInsensitiveOrder;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * A list of contacts is a very common use case for developers, we tried to make
 * this list as realistic as possible allowing you to dial, email, share and
 * even delete contacts. Notice that some platforms might not support contacts
 * access (e.g. JavaScript) in which case we fallback to fake contacts.
 *
 * @author Shai Almog
 */
public class Contacts extends Demo {

    private HashMap<String, Image> letterCache = new HashMap<>();
    private Image circleLineImage;
    private Object circleMask;
    private int circleMaskWidth;
    private int circleMaskHeight;
    private Font letterFont;
    private boolean finishedLoading;
    private long lastScroll;
    private boolean messageShown;
    int k = 0;

    public String getDisplayName() {
        return "Friends";
    }

    public Image getDemoIcon() {
        return getResources().getImage("friends.png");
    }

    @Override
    public String getSourceCodeURL() {
        return "https://www.blablacar.fr/";
    }

    @Override
    public String getDescription() {
        return "CoOL friends "
                + "Contact your friends"
                ;
    }

    public Image getLetter(char c, Component cmp) {
        c = Character.toUpperCase(c);
        String cstr = "" + c;
        Image i = letterCache.get(cstr);
        if (i != null) {
            return i;
        }

        int off = (c - 'A') % 7 + 1;
        int color = cmp.getUIManager().getComponentStyle("Blank" + off).getBgColor();
        Image img = Image.createImage(circleMaskWidth, circleMaskHeight, 0);
        Graphics g = img.getGraphics();
        g.setColor(color);
        g.fillArc(1, 1, circleMaskWidth - 2, circleMaskHeight - 2, 0, 360);
        g.setFont(letterFont);
        g.setColor(0xffffff);
        int w = letterFont.charWidth(c);
        g.drawString(cstr, img.getWidth() / 2 - w / 2, img.getHeight() / 2 - letterFont.getHeight() / 2);
        g.drawImage(circleLineImage, 0, 0);
        letterCache.put(cstr, img);
        return img;
    }

    private String notNullOrEmpty(String... s) {
        StringBuilder b = new StringBuilder();
        for (String ss : s) {
            if (ss == null || ss.length() == 0) {
                return "";
            }
            b.append(ss);
        }
        return b.toString();
    }

    Contact[] got() {
        CaseInsensitiveOrder co = new CaseInsensitiveOrder();
        Collections.sort(UserDAO.users, (o1, o2) -> {
            String sname1 = o1.nom;
            String sname2 = o2.nom;
            if (sname1 == null) {
                sname1 = o1.prenom;
                if (sname1 == null) {
                    sname1 = "";
                }
            }
            if (sname2 == null) {
                sname2 = o2.prenom;
                if (sname2 == null) {
                    sname2 = "";
                }
            }
            return co.compare(sname1, sname2);
        });
        String[] usernames = new String[20];
        String[] mailss = new String[20];
        Hashtable<Integer, String> mails = new Hashtable<>();

        int i = 0;
        System.out.println("test from friends");
        System.out.println(UserDAO.users);
        for (User user : UserDAO.users) {
            String username = user.nom + " " + user.prenom;
            usernames[i] = username;
            mailss[i] = user.email;
            i++;
        }

        Contact[] ct = new Contact[i];
        int j = 0;
        for (User user : UserDAO.users) {
            ct[j] = new Contact();
            ct[j].setDisplayName(user.nom + " " + user.prenom);
            /*mails.put(j, user.email); 
            ct[j].setEmails(mails);
            mails.clear();*/
            j++;
        }


        /*for (int iter = 0; iter < usernames.length; iter++) {
            ct[iter] = new Contact();
            ct[iter].setDisplayName("test");  

          
        }*/
        return ct;
    }

    Contact[] getContacts() {
        try {
            return Display.getInstance().getAllContacts(true, true, false, true, true, false);
        } catch (Throwable t) {
            return null;
        }
    }

    public Container createDemo(Form parentForm) {
        Image circleImage = getResources().getImage("circle.png");
        circleLineImage = getResources().getImage("circle-line.png");

        parentForm.addPointerDraggedListener(e -> lastScroll = System.currentTimeMillis());
        parentForm.addShowListener(e -> {
            if (!messageShown) {
                messageShown = true;
                ToastBar.showMessage("Swipe the contacts to both sides to expose additional options", FontImage.MATERIAL_COMPARE_ARROWS, 2000);
            }
        });

        circleMask = circleImage.createMask();
        circleMaskWidth = circleImage.getWidth();
        circleMaskHeight = circleImage.getHeight();
        letterFont = Font.createTrueTypeFont("native:MainThin", "native:MainThin");
        letterFont = letterFont.derive(circleMaskHeight - circleMaskHeight / 3, Font.STYLE_PLAIN);

        final Container contactsDemo = new Container(BoxLayout.y());
        contactsDemo.setScrollableY(true);
        contactsDemo.add(FlowLayout.encloseCenterMiddle(new InfiniteProgress()));

        Display.getInstance().scheduleBackgroundTask(() -> {
            Contact[] tcontacts = got();
            if (tcontacts == null) {
                tcontacts = got();
            }

            int i = 0;
            String[] mailss = new String[20];

            for (User user : UserDAO.users) {
                mailss[i] = user.email;
                i++;
            }
            Contact[] contacts = tcontacts;
            CaseInsensitiveOrder co = new CaseInsensitiveOrder();

            /*  Arrays.sort(contacts, (o1, o2) -> {
                String sname1 = o1.getFamilyName();
                String sname2 = o2.getFamilyName();
                if (sname1 == null) {
                    sname1 = o1.getDisplayName();
                    if (sname1 == null) {
                        sname1 = "";
                    }
                }
                if (sname2 == null) {
                    sname2 = o2.getDisplayName();
                    if (sname2 == null) {
                        sname2 = "";
                    }
                }
                return co.compare(sname1, sname2);
            });*/
            Display.getInstance().callSerially(() -> {
                contactsDemo.removeAll();
                k = 0;
                for (Contact c : contacts) {

                    String dname = c.getDisplayName();
                    String dmail = UserDAO.users.get(k).email;
                    int phone = UserDAO.users.get(k).tel;
                    String dphone = Integer.toString(phone);
                    String drole = UserDAO.users.get(k).role;
                    k++;
                    if (dname == null || dname.length() == 0) {
                        continue;
                    }
                    MultiButton mb = new MultiButton(dname);
                    mb.setIconUIID("ContactIcon");

                    // we need this for the SwipableContainer below
                    mb.getAllStyles().setBgTransparency(255);
                    mb.setTextLine2(c.getNote());
                    mb.setIcon(getLetter(dname.charAt(0), mb));
                    Button delete = new Button();
                    delete.setUIID("SwipeableContainerButton");
                    FontImage.setMaterialIcon(delete, FontImage.MATERIAL_DELETE, 8);

                    Button info = new Button();
                    info.setUIID("SwipeableContainerInfoButton");
                    FontImage.setMaterialIcon(info, FontImage.MATERIAL_INFO, 8);

                    info.addActionListener(e -> {
                        Dialog dlg = new Dialog(dname);
                        TableLayout tl = new TableLayout(3, 2);
                        dlg.setLayout(tl);
                        // Map emailHash = c.getEmails();
                        Container emails;
                        // if (mailss != null && mailss.length > 0) {
                        Button emailArr = new Button();
                        int off = 0;
                        System.out.println(k);
                        //  for (String ee : mailss) {
                        // String mail = UserDAO.users.get(k).email;
                        //k++;
                        emailArr = new Button(dmail);
                        FontImage.setMaterialIcon(emailArr, FontImage.MATERIAL_EMAIL);
                        emailArr.addActionListener(ev -> {
                            dlg.dispose();
                            Message m = new Message("");
                            Display.getInstance().sendMessage(new String[]{(String) dmail}, "Sent from Codename One!", m);
                        });
                        off++;

                        // }
                        emails = BoxLayout.encloseY(emailArr);
                        // } else {
                        //emails = new Container(BoxLayout.y());
                        //}

                        // Map phonesHash = c.getPhoneNumbers();
                        Container phones;
                        //if (phonesHash != null && phonesHash.size() > 0) {
                        Button phoneArr = new Button();
                        //int off = 0;
                        // for (Object ee : phonesHash.values()) {
                        phoneArr = new Button(dphone);
                        FontImage.setMaterialIcon(phoneArr, FontImage.MATERIAL_PHONE);
                        phoneArr.addActionListener(ev -> {
                            dlg.dispose();
                            Display.getInstance().dial((String) dphone);
                        });
                        off++;
                        // }
                        phones = BoxLayout.encloseY(phoneArr);
                        // } else {
                        // phones = new Container(BoxLayout.y());
                        // }

                        dlg.add("Phone").add(phones).
                                add("Email").add(emails);

                        // if (c.getBirthday() == 0) {
                        //dlg.add("Birthday").add("---");
                        // } else {
                        dlg.add("Post").add(drole);
                        // }
                        dlg.setDisposeWhenPointerOutOfBounds(true);
                        dlg.setBackCommand(new Command(""));
                        dlg.showPacked(BorderLayout.SOUTH, true);
                    });

                    /* ShareButton share = new ShareButton();
                    share.setUIID("SwipeableContainerShareButton");
                    FontImage.setMaterialIcon(share, FontImage.MATERIAL_SHARE, 8);
                    share.setText("");
                    share.setTextToShare(dname + notNullOrEmpty(" phone: ", c.getPrimaryPhoneNumber())
                            + notNullOrEmpty(" email: ", c.getPrimaryEmail()));*/
                    Button call = new Button();
                    call.setUIID("SwipeableContainerInfoButton");
                    FontImage.setMaterialIcon(call, FontImage.MATERIAL_CALL, 8);
                    call.addActionListener(e -> Display.getInstance().dial(c.getPrimaryPhoneNumber()));

                    Container options;
                    if (c.getPrimaryEmail() != null && c.getPrimaryEmail().length() > 0) {
                        Button email = new Button();
                        email.setUIID("SwipeableContainerInfoButton");
                        FontImage.setMaterialIcon(email, FontImage.MATERIAL_EMAIL, 8);
                        email.addActionListener(e -> {
                            Message m = new Message("");
                            Display.getInstance().sendMessage(new String[]{c.getPrimaryEmail()}, "Sent from Codename One!", m);
                        });
                        options = GridLayout.encloseIn(4, call, email, info);
                    } else {
                        options = GridLayout.encloseIn(3, call, info);
                    }

                    SwipeableContainer sc = new SwipeableContainer(
                            options,
                            GridLayout.encloseIn(1, delete),
                            mb);
                    contactsDemo.add(sc);
                    sc.addSwipeOpenListener(e -> {
                        // auto fold the swipe when we go back to scrolling
                        contactsDemo.addScrollListener(new ScrollListener() {
                            int initial = -1;

                            @Override
                            public void scrollChanged(int scrollX, int scrollY, int oldscrollX, int oldscrollY) {
                                // scrolling is very sensitive on devices...
                                if (initial < 0) {
                                    initial = scrollY;
                                }
                                lastScroll = System.currentTimeMillis();
                                if (Math.abs(scrollY - initial) > mb.getHeight() / 2) {
                                    if (sc.getParent() != null) {
                                        sc.close();
                                    }
                                    contactsDemo.removeScrollListener(this);
                                }
                            }
                        });
                    });

                    delete.addActionListener(e -> {
                        if (Dialog.show("Delete", "Are you sure?\nThis will delete this contact permanently!", "Delete", "Cancel")) {
                            // can happen in the case of got() contacts
                            if (c.getId() != null) {
                                Display.getInstance().deleteContact(c.getId());
                            }
                            sc.remove();
                            contactsDemo.animateLayout(800);
                        }
                    });

                    // can happen in the case of got() contacts
                    if (c.getId() != null) {
                        Display.getInstance().scheduleBackgroundTask(() -> {
                            // let the UI finish loading first before we proceed with the images
                            while (!finishedLoading) {
                                Util.sleep(100);
                            }

                            int scrollY = contactsDemo.getScrollY();

                            // don't do anything while we are scrolling or animating
                            long idle = System.currentTimeMillis() - lastScroll;
                            while (idle < 1500 || contactsDemo.getAnimationManager().isAnimating() || scrollY != contactsDemo.getScrollY()) {
                                scrollY = contactsDemo.getScrollY();
                                Util.sleep(Math.min(1500, Math.max(100, 2000 - ((int) idle))));
                                idle = System.currentTimeMillis() - lastScroll;
                            }

                            // fetch only the picture which is the last missing piece
                            Contact picContact = Display.getInstance().getContactById(c.getId(), false, true, false, false, false);
                            Image img = picContact.getPhoto();
                            if (img != null) {
                                // UI/Image manipulation must be done on the EDT
                                Display.getInstance().callSerially(() -> {
                                    Image rounded = img.fill(circleMaskWidth, circleMaskHeight).applyMask(circleMask);
                                    Image mutable = Image.createImage(circleMaskWidth, circleMaskHeight, 0);
                                    Graphics g = mutable.getGraphics();
                                    g.drawImage(rounded, 0, 0);
                                    g.drawImage(circleLineImage, 0, 0);
                                    mb.setIcon(mutable);
                                });

                                // yield slightly so we don't choke the EDT while a user might be scrolling...
                                Util.sleep(5);
                            }
                        });
                    }

                }
                contactsDemo.revalidate();

                finishedLoading = true;
            });
        });
        FloatingActionButton fab = FloatingActionButton.createFAB(FontImage.MATERIAL_INFO);
        fab.addActionListener(e -> {
            Dialog.show("About", "Comunity "
                    + "here you friends sorted by name!", "OK", null);
        });
        Container c = fab.bindFabToContainer(contactsDemo);
        return c;
    }

}
