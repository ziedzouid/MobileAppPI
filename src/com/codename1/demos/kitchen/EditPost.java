/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.demos.kitchen;

import static com.codename1.demos.kitchen.KitchenSink.res;
import com.codename1.entities.kitchen.Poste;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.NetworkManager;
import com.codename1.l10n.SimpleDateFormat;
import com.codename1.ui.Button;
import com.codename1.ui.ComboBox;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.TextField;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.spinner.Picker;
import com.codename1.ui.util.Resources;

import java.util.Date;
import java.util.Map;

/**
 *
 * @author esprit
 */
public class EditPost {

    Form EditPoste;
    int temp;

    public EditPost(Poste t) {
        EditPoste = new Form("Edit your Post", BoxLayout.y());

        //Container edit=new Container(new BoxLayout(BoxLayout.Y_AXIS));
        Container abc = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        Container ab = new Container(new BoxLayout(BoxLayout.X_AXIS));
        //                Container bc=new Container(new BoxLayout(BoxLayout.X_AXIS));

        //Label db = new Label("ID : " + t.getId());
        Label db = new Label("Contenu : ");
        TextField nbr = new TextField(t.getContenu());
        Label depart = new Label("Departure: ");
        ComboBox dep = new ComboBox(t.getDepart());
        dep.addItem("Ariena");
        dep.addItem("Tunis");
        dep.addItem("Beja");
        dep.addItem("tatouine");

        Label arrive = new Label("Destination: ");
        ComboBox arive = new ComboBox(t.getArrive());

        arive.addItem("Ariena");
        arive.addItem("Tunis");
        arive.addItem("Beja");
        arive.addItem("tatouine");

        TextField arr = new TextField(t.getArrive());

        Picker datedebut = new Picker();
        datedebut.setType(Display.PICKER_TYPE_DATE);

        datedebut.setDate(t.getDatePost());
        abc.add(depart);
        abc.add(dep);
        abc.add(arrive);
        abc.add(arive);
        //abc.add(datedebut);
        abc.add(db);
        abc.add(nbr);
        Button b = new Button("Update");
         UserDAO userDAO = new UserDAO();

        EditPoste.add(abc).add(b);
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                ConnectionRequest req = new ConnectionRequest();
                req.setUrl("http://localhost/TestMobile/web/app_dev.php/EditPost/" + t.getId() + "?jour" + datedebut.getDate()+"&id_user="+userDAO.user.id + "&contenu=" + nbr.getText() + "&depart=" + dep.getSelectedItem() + "&arrive=" + arive.getSelectedItem() + "");
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
            }

        });

    }

    public void show() {

        //AffichPoste.show(); 
        EditPoste.show();
    }

    public Form getF() {
        return EditPoste;
    }

    public void SetForm(Form f) {
        this.EditPoste = f;
    }
}
