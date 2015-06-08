package com.groenland.stefano.weerapp;

/**
 * Created by Stefano on 8-6-2015.
 */
public class BierWeer {
    int id;
    String plaats;
    String naam;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getNaam(){
        return naam;
    }
    public void setNaam(String naam){
        this.naam = naam;
    }
    public String getPlaats(){
        return plaats;
    }
    public void setPlaats(String plaats){
        this.plaats = plaats;
    }

}
