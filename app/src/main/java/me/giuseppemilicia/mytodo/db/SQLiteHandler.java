package me.giuseppemilicia.mytodo.db;

/**
 * Created by Giuseppe on 22/02/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import me.giuseppemilicia.mytodo.models.Nota;


public class SQLiteHandler extends SQLiteOpenHelper {

    // Notes Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TITOLO = "titolo";
    private static final String KEY_CORPO = "corpo";
    private static final String KEY_COLORE = "colore";
    private static final String KEY_DATA_CREAZIONE = "data_creazione";
    private static final String KEY_DATA_ULTIMA_MODIFICA = "data_ultima_modifica";
    private static final String KEY_DATA_SCADENZA = "data_scadenza";
    private static final String KEY_STATO = "stato";
    private static final String KEY_SPECIAL = "special";
    private static final String KEY_POSIZIONE = "posizione";
    private static final String KEY_CANCELLATO = "cancellato";


    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "MyToDoDatabase";

    // Contacts table name
    private static final String TABLE_NOTES = "notes";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_NOTE_TABLE =
                "CREATE TABLE " + TABLE_NOTES
                        + "(" +
                        KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITOLO + " TEXT," +
                        KEY_CORPO + " TEXT," + KEY_DATA_CREAZIONE + " INTEGER," +
                        KEY_DATA_ULTIMA_MODIFICA + " INTEGER," + KEY_DATA_SCADENZA + " INTEGER," +
                        KEY_STATO + " TEXT," + KEY_COLORE + " INTEGER," + KEY_SPECIAL + " INTEGER," +
                        KEY_POSIZIONE + " INTEGER"
                        + ")";
        db.execSQL(CREATE_NOTE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1) {
            db.execSQL("ALTER TABLE " + TABLE_NOTES + " ADD " + KEY_CANCELLATO + " INTEGER");
        }
    }


    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */


    public long addNote(Nota nota) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITOLO, nota.getTitolo());
        values.put(KEY_CORPO, nota.getCorpo());
        values.put(KEY_COLORE, nota.getColore());
        values.put(KEY_STATO, nota.getStato().toString());
        values.put(KEY_SPECIAL, nota.isSpeciale());
        values.put(KEY_POSIZIONE, nota.getPosizione());
        values.put(KEY_DATA_CREAZIONE, nota.getDataCreazione().getTime());
        values.put(KEY_DATA_ULTIMA_MODIFICA, nota.getDataUltimaModifica().getTime());
        values.put(KEY_DATA_SCADENZA, nota.getDataScandenza().getTime());

        // Inserting Row
        long result = db.insert(TABLE_NOTES, null, values);
        db.close(); // Closing database connection
        return result;
    }

    public long addNote(Nota nota, boolean index) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, nota.getId());
        values.put(KEY_TITOLO, nota.getTitolo());
        values.put(KEY_CORPO, nota.getCorpo());
        values.put(KEY_COLORE, nota.getColore());
        values.put(KEY_POSIZIONE, nota.getPosizione());
        values.put(KEY_STATO, nota.getStato().toString());
        values.put(KEY_SPECIAL, nota.isSpeciale());
        values.put(KEY_DATA_CREAZIONE, nota.getDataCreazione().getTime());
        values.put(KEY_DATA_ULTIMA_MODIFICA, nota.getDataUltimaModifica().getTime());
        values.put(KEY_DATA_SCADENZA, nota.getDataScandenza().getTime());

        // Inserting Row
        long result = db.insert(TABLE_NOTES, null, values);
        db.close(); // Closing database connection
        return result;
    }

    // Getting All Notes
    public ArrayList<Nota> getAllNotes() {
        ArrayList<Nota> notesList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NOTES + " ORDER BY " + KEY_SPECIAL + " DESC" + "," + KEY_POSIZIONE + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Nota nota = new Nota();
                nota.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                nota.setTitolo(cursor.getString(cursor.getColumnIndex(KEY_TITOLO)));
                nota.setCorpo(cursor.getString(cursor.getColumnIndex(KEY_CORPO)));
                nota.setStato(cursor.getString(cursor.getColumnIndex(KEY_STATO)));
                nota.setSpeciale(cursor.getInt(cursor.getColumnIndex(KEY_SPECIAL)) > 0);
                nota.setColore(cursor.getInt(cursor.getColumnIndex(KEY_COLORE)));
                nota.setPosizione(cursor.getInt(cursor.getColumnIndex(KEY_POSIZIONE)));
                nota.setDataCreazione(cursor.getInt(cursor.getColumnIndex(KEY_DATA_CREAZIONE)));
                nota.setDataUltimaModifica(cursor.getInt(cursor.getColumnIndex(KEY_DATA_ULTIMA_MODIFICA)));
                nota.setDataScandenza(cursor.getInt(cursor.getColumnIndex(KEY_DATA_SCADENZA)));
                // Adding note to list
                notesList.add(nota);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        // return notes list
        return notesList;
    }

    // Updating single note
    public int updateNote(Nota nota) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITOLO, nota.getTitolo());
        values.put(KEY_CORPO, nota.getCorpo());
        values.put(KEY_COLORE, nota.getColore());
        values.put(KEY_STATO, nota.getStato().toString());
        values.put(KEY_SPECIAL, nota.isSpeciale());
        values.put(KEY_POSIZIONE, nota.getPosizione());
        values.put(KEY_DATA_CREAZIONE, nota.getDataCreazione().getTime());
        values.put(KEY_DATA_ULTIMA_MODIFICA, nota.getDataUltimaModifica().getTime());
        values.put(KEY_DATA_SCADENZA, nota.getDataScandenza().getTime());
        // updating row
        int result = db.update(TABLE_NOTES, values, KEY_ID + " = ?",
                new String[]{String.valueOf(nota.getId())});
        db.close();
        return result;
    }

    // Deleting single note
    public int deleteNote(Nota nota) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NOTES, KEY_ID + " = ?",
                new String[]{String.valueOf(nota.getId())});
        db.close();
        return result;
    }

}