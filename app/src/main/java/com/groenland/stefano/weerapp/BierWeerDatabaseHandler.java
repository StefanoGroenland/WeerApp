package com.groenland.stefano.weerapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stefano on 8-6-2015.
 */
public class BierWeerDatabaseHandler extends SQLiteOpenHelper {

    // Naamgeving van de database en tabel middels constanten
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "weerapp.db"; // Let op, gebruik .db als extensie
    private static final String BIERWEER_TABLE = "users";

    // Constanten om de table layout te definieren
    private static final String KEY_ID = "id";
    private static final String PLAATS = "locatie";
    private static final String NAAM = "naam";


    // De constructor. Deze zorgt ervoor dat de juiste initialisatie plaats vind. Als de database
    // nog niet bestaan wordt onCreate getriggerd. Bij een nieuwer versienummer van de database
    // wordt juist onUpgrade aangeroepen.
    public BierWeerDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // In de onCreate komt alle code om de tabellen die nodig zijn te maken. Dat gebeurd door
    // een SQL statement samen te stellen en uit te voeren.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BIERWEER_TABLE = "CREATE TABLE " + BIERWEER_TABLE + "(" +
                KEY_ID + " INTEGER PRIMARY KEY," + PLAATS + " TEXT," + NAAM +
                " TEXT" + ")";
        db.execSQL(CREATE_BIERWEER_TABLE);
    }

    // Om te zorgen dat bij database wijzigingen tussen versies niks kwijt te raken kan het
    // onUpgrade mechanisme gebruikt worden. Nu wist hij de tabel en maakt hem opnieuw aan.
    // Wat gebeurd er met de data die er in zit?
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + BIERWEER_TABLE);
        onCreate(db);
    }


    // addRecord voegt een enkel record toe in de tabel. Dat kan door SQL te schrijven maar
    // in dit geval wordt gebruik gemaakt van ContentValues. De benodigde velden in een record
    // worden als kolomnaam en waarde in de ContentValues structuur geplaatst en vervolgens
    // met insert in de database geschoten
    public void addRecord(BierWeer record)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PLAATS, record.getPlaats());
        values.put(NAAM, record.getNaam());

        db.insert(BIERWEER_TABLE, null, values);
        db.close();
    }

    // zoekPlaats geeft aan de hand van de naam die als parameter wordt doorgegeven de plaatsnaam
    // waar de betreffende persoon woont.
    public String zoekPlaats(String naam) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " + PLAATS + " FROM " + BIERWEER_TABLE + " WHERE " + NAAM + "='" + naam + "';", null);
        if (cursor.moveToFirst()) {
            return cursor.getString(0);
        }
        return "";
    }

    // emptyBierWeer maakt de tabel leeg. Wat gebeurd er als er door upgrades meer tabel in de database
    // zitten met de andere tabellen?
    public void emptyBierWeer() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS " + BIERWEER_TABLE);
        onCreate(db);
    }

    // getBierWeer geeft een lijst met de records die in de tabel zitten terug. De objecten in de
    // lijst zijn van het type BierWeer, de klasse die je als data access objects geprogrameerd hebt.
    // In de presentatie bij Les 3 vind je terug hoe je deze lijst kunt gebruiken om je GUI te vullen.
    public List<BierWeer> getBierWeer()
    {
        List<BierWeer> bierWeerList = new ArrayList<BierWeer>();
        String selectQuery = "SELECT * FROM " + BIERWEER_TABLE + ";";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                BierWeer bierWeer = new BierWeer();
                bierWeer.setId(Integer.parseInt(cursor.getString(0)));
                bierWeer.setPlaats(cursor.getString(1));
                bierWeer.setNaam(cursor.getString(2));
                bierWeerList.add(bierWeer);
            } while(cursor.moveToNext());
        }
        return bierWeerList;
    }
}