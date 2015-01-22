package hsl.imtpmd.Eindopdracht;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import hsl.imtpmd.Eindopdracht.R;


@SuppressLint("NewApi")
public class ServicePagina extends Activity{

    // Buttons die op de service pagina staan
    Button aanvraagButton;
    Button annuleerButton;

   


public void onCreate(Bundle savedInstanceState)
{
    super.onCreate(savedInstanceState);
    setContentView(R.layout.servicepagina);
    // het opzetten van de interface
    setupUserinterface();
    // het aanroepen van de listeners methode
    buttonListeners();

   

    // ophalen van de textvelden
    TextView servicenaam = (TextView) findViewById(R.id.servicenaam);
    TextView serviceinfo = (TextView) findViewById(R.id.serviceinfo);
    TextView productinfo = (TextView) findViewById(R.id.productinfo);
   

    // Het vullen van de textvelden

    servicenaam.setText(MainActivity.soortenServiceLijst.get(MainActivity.soortService));
    serviceinfo.setText(MainActivity.gekozenService);
    productinfo.setText(MainActivity.infoservice);

}

    // methode welke de buttons,listeners en gelijk methodes geeft. Dit gebeurd in een aparte
    // methode om conflicten met threads te voorkomen.
 public void buttonListeners(){
       aanvraagButton     = (Button) findViewById(R.id.aanvraagbutton);


    // functionaliteiten van de aanvraagbutton
       aanvraagButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

                          
            {
               Intent tmpIntent;
                 tmpIntent = new Intent(getApplicationContext(), AanvraagPagina.class);
                startActivity(tmpIntent);
            }

           }
       });


        // functionaliteiten van de annuleer button
        annuleerButton   = (Button) findViewById(R.id.annuleerbutton);
        annuleerButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

            // Bij druk op de annuleer knop gaat de app terug naar de main pagina

               Intent tmpIntent;
               tmpIntent = new Intent(getApplicationContext(), MainActivity.class);
               startActivity(tmpIntent);
           }
       });
   }



    public void setupUserinterface()
    {
       
       

        //achtergrond kleur
        RelativeLayout layout = (RelativeLayout) findViewById( R.id.mainlayout );


        
    }
    }