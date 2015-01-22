package hsl.imtpmd.Eindopdracht;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import hsl.imtpmd.Eindopdracht.R;

public class AanvraagPagina extends Activity {

    // Buttons die op de aanvraagpagina pagina staan
    Button aanvraagButton;
    Button annuleerButton;

    // textviews die op de aanvraagpagina staan
    TextView Naam ;
    TextView Adres;
    TextView Telefoon;
    TextView Email;
    TextView Aantal;
    TextView Servicenaam;
    // Strings nodig voor het vullen van de velden
    private String naam = null;
    private String adres;

    private String telefoon;
    private String email;

    public static final String MyPREFERENCES = "MyPrefs" ;
    // Shared preferences voor het laden en saven
    SharedPreferences sharedPreferences;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aanvraag_pagina);

        // Achtrgrond kleur van de actionbar instellen
        setupUserinterface();

        // De buttons instellen
        buttonListeners();
        // Alle invoervelden aanwijzen
        Naam        = (TextView) findViewById(R.id.naaminvoer);
        Adres       = (TextView) findViewById(R.id.adresinvoer);

        Telefoon    = (TextView) findViewById(R.id.telefooninvoer);
        Email       = (TextView) findViewById(R.id.emailinvoerr);
       
        Servicenaam   = (TextView) findViewById(R.id.servicenaaminvoer);

        // De servicenaam instellen zodat de gebruiker deze nogmaals kan lezen alvorens er geboekt wordt
        Servicenaam.setText(MainActivity.gekozenService);

        

        // Opslag instellen
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

         // Als er opgeslagen gegevens worden gevonden dan worden deze geladen en ingevoerd.
        if (sharedPreferences.contains("naam"))
        {
            Naam.setText(sharedPreferences.getString("naam", naam));
        }

        if (sharedPreferences.contains("adres"))
        {
            Adres.setText(sharedPreferences.getString("adres", adres));
        }

        if (sharedPreferences.contains("telefoon"))
        {
            Telefoon.setText(sharedPreferences.getString("telefoon", telefoon));
        }

        if (sharedPreferences.contains("email"))
        {
            Email.setText(sharedPreferences.getString("email", email));
        }

    }

    public void setupUserinterface()
    {
        //Zet de userinterface op met de opgeslagen waarden


        //achtergrond kleur
        LinearLayout layout = (LinearLayout) findViewById( R.id.mainlayout );
        
    }

    // Methode welke de gegevens opslaat en welke checkt of de gegevens uberhaupt zijn ingevuld
    // Zijn de gegevens niet ingevuld dan komt er een toast pop up
    public void opslaanGegevens(){

        SharedPreferences.Editor editor = sharedPreferences.edit();
        // naamveld checken
        String sNaam = Naam.getText().toString();
        if (sNaam.matches("")) {
            Toast.makeText(this, "Vul hier uw naam in", Toast.LENGTH_SHORT).show();
            return;
        }

        else {
            this.naam =  Naam.getText().toString();
            // opslaan van de naam
            editor.putString("naam", naam);
            editor.commit();
        }
        // adresveld checken
        String sAdres = Adres.getText().toString();


        if (sAdres.matches("")) {
            Toast.makeText(this, "Vul hier uw adres in", Toast.LENGTH_SHORT).show();
            return;
        }
            else {
            // opslaan
            this.adres =  Adres.getText().toString();
                 editor.putString("adres", adres);
                 editor.commit();
                }


        // Telefoonveld checken
        String sTel = Telefoon.getText().toString();
        if (sTel.matches("")) {
            Toast.makeText(this, "Vul hier uw adres in", Toast.LENGTH_SHORT).show();
            return;
        }

                else {
                    this.telefoon =  Telefoon.getText().toString();
            // opslaan
                    editor.putString("telefoon", telefoon);
                    editor.commit();
        }

        // emailveld checken
        String sEmail = Email.getText().toString();
        if (sEmail.matches("")) {
            Toast.makeText(this, "Vul hier uw adres in", Toast.LENGTH_SHORT).show();
            return;
        }

                else {
                    this.email =  Email.getText().toString();
            // opslaan
                    editor.putString("email", email);
                    editor.commit();
        }
    }


    // methode van de button listeners
    public void buttonListeners(){


        aanvraagButton     = (Button) findViewById(R.id.aanvraagButton);

        // functionaliteiten van de aanvraagbutton
        aanvraagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    opslaanGegevens();
                    aanvragen(view);
                // het laden van het hoofdscherm na de aanvraag
                Intent tmpIntent;
                tmpIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(tmpIntent);

            }
        });
        // functionaliteiten van de annuleer button
        annuleerButton   = (Button) findViewById(R.id.AnnuleerButtonBoek);
        annuleerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Bij druk op de annuleer knop gaat die terug naar de main pagina
                opslaanGegevens();
                // het laden van een andere pagina
                Intent tmpIntent;
                tmpIntent = new Intent(getApplicationContext(), ServicePagina.class);
                startActivity(tmpIntent);
            }
        });
    }

    // Methode welke alles voor de aanvraag regelt, dus het ophalen van de gegevens en het verzenden naar de server
    // Ook het tonen van de reactie van de server gebeurd hier
    public void aanvragen(View view) {

    JSONArray aanvraagInfo          = new JSONArray();
    JSONObject aanvraag     = new JSONObject();
    JSONObject koper        = new JSONObject();
    JSONObject order = new JSONObject();

    try
    {
        //Aanvraag gegevens in een object putten
        // service naam ophalen en erin stoppen
        String servicenaam = MainActivity.gekozenService;
        aanvraag.put("servicenaam", servicenaam);

        

        koper.put("kopernaam",  naam);
        koper.put("koperadres", adres);
        koper.put("kopertelnr", telefoon );
        koper.put("koperemail", email);

        //JSON array welke de server verwacht
        aanvraagInfo.put(aanvraag);
        aanvraagInfo.put(koper);

        //Deze array in een JSON object stoppen zodat deze verzonden kan worden
        order.put("aanvraag", aanvraagInfo);
    }
    catch (JSONException e)
    {
        e.printStackTrace();
    }

        // Versturen van de data naar de server en de reactie van de server opvangen om aan de gebruiker te tonen
        String serverAntwoord = null;

        try {
            serverAntwoord = new ServerCommunicator( view.getContext(), MainActivity.ip, 4444, order.toString()).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        // Melding met de serverstatus voor de gebruiker na de aanvraag
        Toast.makeText(getApplicationContext(), "Bericht van Mario & Co:"+"\n" + serverAntwoord, Toast.LENGTH_LONG).show();

}

}




