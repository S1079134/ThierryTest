package hsl.imtpmd.Eindopdracht;

/**
 
 */


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class ServerCommunicator extends AsyncTask<Void, Void, String>
{

    private String message;
    private String ip;
    private String reactie = null;
    private int port;
    private Context context;


    public ServerCommunicator(Context context, String ip, int port, String message  )
    {
        // Variabelen welke de communicator nodig heeft.

        this.context = context;
        this.message = message;
        this.ip = ip;
        this.port = port;
    }

    //Deze methode verzend de gegevens naar de server
    private void sendMessage( String message, Socket serverSocket )
    {
        OutputStreamWriter outputStreamWriter = null;

        try
        {
                    outputStreamWriter = new OutputStreamWriter(serverSocket.getOutputStream());
         }

        catch (IOException e1)
        {
            e1.printStackTrace();
        }

        if( outputStreamWriter != null )
        {

            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

            PrintWriter writer = new PrintWriter( bufferedWriter, true );

            writer.println(message);
        }
    }


    @Override
    protected String doInBackground(Void... params)
    {
        try
        {
            Socket serverSocket = new Socket();
            serverSocket.connect( new InetSocketAddress( this.ip, this.port ), 4000 );

            //verzend een bericht naar de server
            this.sendMessage(message, serverSocket);

            InputStream input;

            //zorgt voor een reactie van de server
            try
            {
                input = serverSocket.getInputStream();
                BufferedReader reactieStreamReader = new BufferedReader(new InputStreamReader(input));
                String line = "";
                StringBuilder stringBouwer = new StringBuilder();

                while ((line = reactieStreamReader.readLine()) != null)
                {
                    stringBouwer.append(line);
                }
                reactieStreamReader.close();

                this.reactie = stringBouwer.toString();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
           }

        catch( UnknownHostException e )
        {
            Log.d("debug", "can't find host");
        }

        catch( SocketTimeoutException e )
        {
            Log.d("debug", "time-out");
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }

        return reactie;
    }


 }
