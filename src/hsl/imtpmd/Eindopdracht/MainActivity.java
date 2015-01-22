package hsl.imtpmd.Eindopdracht;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import hsl.imtpmd.Eindopdracht.R;

public class MainActivity extends ListActivity
{
	   

    // Controleer of het IPADRES klopt met de localhost.
    // LET OP: Wanneer dit niet overeenkomt start de app niet!
    public static String ip = "192.168.253.152"; 


    private ListView mainListView ;
    public ServerCommunicator serverCommunicator;
    
    public static String infoservice;

    public static String gekozenService;

    private static MainActivity activity;
    // array met de soorten services
    static ArrayList<String> soortenServiceLijst;
    static ArrayList<HashMap<String, String>> serviceData;


    // Int welke het type service is, dus 0,1,2,3,4
    public static int soortService;
    // Int welke de positie van de Klik op de lisview rij heeft
    public static int positieListviewpresd;
    public static int positieListviewpresd1;

    public ArrayList<HashMap<String, String>> lijst;

    //private int gekozenSoortservice;

    private SharedPreferences sharedPreferences;

    //deze methode wordt elke keer aangeroepen als de applicatie opstart
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        // userinterface inladen
        setupUserinterface();
        // de service catagorie laden
        sharedPreferences = this.getSharedPreferences("Service",soortService);
        soortService = sharedPreferences.getInt("Service",soortService);

        mainListView = getListView();

        soortenServiceLijst = getSoortenServiceLijst();
        // de lijst vullen met de juiste data
        lijst  = getServiceData(soortService);

      ListAdapter adapter = new SimpleAdapter(this, lijst , R.layout.listview_lijsten,
               new String[] { "servicenaam" },
               new int[] { R.id.naam });

        // Koppel de adapter met de service aan de listview
      mainListView.setAdapter(adapter);

        // ListView Item Click Listener opzetten
        setMyListListener();

    }

// methode welke de listeners opzet
    private void setMyListListener(){
        getListView().setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id){
                Intent tmpIntent;
                tmpIntent = new Intent(getApplicationContext(), ServicePagina.class);
                startActivity(tmpIntent);

                // het zetten van de positieListviewpresd int zodat de service pagina deze kan gebruiken
                positieListviewpresd = position;
                positieListviewpresd1 = position;

                // de gekozen service wordt gezocht, dit gebeurd aan de hand van de listview 
           
                gekozenService = lijst.get(positieListviewpresd).get("servicenaam");
                infoservice =  lijst.get(positieListviewpresd1).get("Info");
            }
        });
    }

    // Methode welke de user interface opzet
    public void setupUserinterface()
    {
        
     

        //achtergrond kleur
        LinearLayout layout = (LinearLayout) findViewById( R.id.mainlayout );
      
    }

    // methode welke de soorten services in de listview gaat zetten, hij krijgt als parameter de
    // gekozen service mee, welke bepaalt wordt door de knoppen
    // Ook zorgt deze methode ervoor dat het gekozen soort service wordt opgeslagen voor hergebruik.
    public void soortServiceInladen(int soortService){

        //de gekozen service opslaan sharedpreferences
        Editor editor = sharedPreferences.edit();
        this.soortService = soortService;

        editor.putInt("Service",soortService);
        editor.commit();

        lijst = getServiceData(soortService);

        // De adapter vullen met de lijst en aangeven met welke onderdelen van het JSON object
        // De String namen moeten overeen komen met de stringnamen van het jsonobject wat ontvangen is
        ListAdapter adapter = new SimpleAdapter(this, lijst , R.layout.listview_lijsten,
                new String[] { "servicenaam", },
                new int[] { R.id.naam });

        mainListView.setAdapter(adapter);

    }


// knop van Riolering, deze zet het soort service op Riolering
    public void Riolering(View view) throws ExecutionException, InterruptedException {

        // zet de INT soort service goed zodat deze opgeslagen kan worden om, zodat de keuze
       this.soortService = 0;
       soortServiceInladen(0);
    }

// Knop van DakLekkage
    public void DakLekkage(View view){

    	soortService = 1;
    	soortServiceInladen(1);
        }

    // knop van de PrinsesInNood

    public void PrinsesInNood(View view){

    	soortService = 2;
    	soortServiceInladen(2);

    }

   


    //methode om de lijst met categorieën van de server op te halen
    static ArrayList<String> getSoortenServiceLijst()
    {
    	soortenServiceLijst = new ArrayList<String>();

        //aanmaken van een nieuw jsonobject
        JSONObject categorieJObject = new JSONObject();
        try
        {
            //verzenden van het jsonobject
            categorieJObject.put("servicelijst", "" );
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        String reactie = null;
        try
        {
            try
            {
                //servercommunicator proberen te verbinden met de server
                reactie = new ServerCommunicator( activity, ip, 4444, categorieJObject.toString()).execute().get();
            }
            catch (ExecutionException e)
            {
                e.printStackTrace();
            }
        }
        catch (InterruptedException e1)
        {
            e1.printStackTrace();
        }


        JSONArray soortenServiceLijstJA = null;
        try
        {
        	soortenServiceLijstJA = new JSONArray(reactie);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        JSONObject jObject = null;
        String value = null;
        soortenServiceLijst = new ArrayList<String>();

        for (int i = 0 ; i < soortenServiceLijstJA.length(); i++)
        {
            try
            {
                jObject = (JSONObject) soortenServiceLijstJA.getJSONObject(i);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            try
            {
                value = jObject.getString("servicelijst");
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            soortenServiceLijst.add(value);
        }
        //geef de lijst met services terug  
        return soortenServiceLijst;
    }


    //methode om de services van categorieën van de server op te halen
    public static ArrayList<HashMap<String, String>> getServiceData(int soortService)
    {
        // hashmap om de services  op te slaan
        serviceData = new ArrayList<HashMap<String, String>>();

        JSONObject servicedataJObject = new JSONObject();
        try
        {
            //Het verzenden van het object
            servicedataJObject.put("informatie", soortenServiceLijst.get(soortService) );

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        String reactie = null;
        try
        {
            try
            {
                //Proberen verbinding te maken
                reactie = new ServerCommunicator( activity, ip, 4444, servicedataJObject.toString()).execute().get();
            }
            catch (ExecutionException e)
            {
                e.printStackTrace();
            }
        }
        catch (InterruptedException e1)
        {
            e1.printStackTrace();
        };


        JSONArray serviceJArray = null;
        try
        {
            serviceJArray = new JSONArray(reactie);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        JSONObject serviceobject = null;

        //Het aanmaken van de servicelijst
        ArrayList<HashMap<String, String>>servicelijst = new ArrayList<HashMap<String, String>>();

        for (int i = 0 ; i < serviceJArray.length(); i++)
        {
            try
            {
                serviceobject = serviceJArray.getJSONObject(i);

                //Put de services in de serviceslijst.
                HashMap<String, String> services = new HashMap<String, String>();
                services = new HashMap<String,String>();
                services.put("servicenaam", serviceobject.getString("servicenaam"));
                serviceData.add(services);
                servicelijst.add(services);

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        return servicelijst;
    }



 



    }

   



