/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.entities.kitchen;

import java.util.Date;


/**
 *
 * @author esprit
 */
public class Poste {
    
    private int id;
    private String contenu;
        private String arrive;
    private String depart;

    private Date date;
      private Date datePost;
    private int user_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getArrive() {
        return arrive;
    }

    public Poste() {
    }

    public void setArrive(String arrive) {
        this.arrive = arrive;
    }

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDatePost() {
        return datePost;
    }

    public void setDatePost(Date datePost) {
        this.datePost = datePost;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return  " "+ id + ""+ contenu +""+date+""+datePost;
    }

    public Poste(int id, String contenu) {
        this.id = id;
        this.contenu = contenu;
    }
    

}
