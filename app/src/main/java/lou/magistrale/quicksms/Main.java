package lou.magistrale.quicksms;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Calendar;

import static lou.magistrale.quicksms.R.*;
import static lou.magistrale.quicksms.R.integer.*;

//TODO: doppio click sul backbottom per uscire
//TODO: possibilità di cambiare il nome del contatto in rubrica

public class Main extends ActionBarActivity implements View.OnClickListener{
    private CustomAdapterContacts customAdapterContacts;
    BroadcastReceiver smsReciver;

    public String welcome(){
        //Facciamo in modo di avere un messaggio personalizzato a seconda del momento della giornata
        String helloMessage;
        int a = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (a >= 4 && a < 12) {
            helloMessage = "Good Morning";
        } else if (a >= 12 && a < 18) {
            helloMessage = "Good Afternoon";
        } else {
            helloMessage = "Good Evening";
        }
        return helloMessage;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);

        // - - - - - - PENDING INTENT CHE MI FA CAPIRE SE L'SMS è STATO RECAPITATO O MENO - - - -
        final PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(getString(string.intent_filter_sms)), 0);
        smsReciver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1)
            {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent",Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure",Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };
        registerReceiver(smsReciver,new IntentFilter(getString(string.intent_filter_sms)));

        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

        //TOAST DI BENVENUTO
        Toast.makeText(this, welcome(), Toast.LENGTH_LONG).show();

        //Bottone che se cliccato mi fa andare all'activity che mi permette di aggiungere un contatto
        Button insertContact = (Button) findViewById(id.insert_contact);
        insertContact.setOnClickListener(this);

        //Final per essere acceduta all'interno della classe anonima
        final ListView contactList = (ListView) findViewById(id.contacts_list);
        customAdapterContacts = new CustomAdapterContacts(this,
                                                          layout.custom_adapter_contacts,
                                                          SingletonModel_contatto.getmIstance().rubrica);
        contactList.setAdapter(customAdapterContacts);  //Carichiamo la lista di contatti nella listView

        //Facciamo in modo che quando si clicchi su uno dei contatti si apra un menu
        contactList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                registerForContextMenu(contactList);
                return false;
            }
        });

        //Facciamo inviare l'sms se l'utente clicca 'Send' nel Dialog che gli appare
        contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                /* - - - - > STO FACENDO L'APP PER IL MIO CELL CHE è FROYO (API 8). I DIALOG FRAGMENT SONO
                *            SUPPORTATI DALLA 11*/
                //Costruiamo il dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(Main.this);
                builder .setMessage(getString(string.message))
                        .setPositiveButton(getString(string.positive_button), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Recuperiamo il numero di telefono e il testo del messaggio
                                String name = SingletonModel_contatto.getmIstance().rubrica.get(position).name();
                                String number = (SingletonModel_contatto.getmIstance().rubrica.get(position).number());
                                String smsText = Singleton_SharedPref.getmIstance().read(name, Main.this);

                                //Inviamo il messaggio
                                try {
                                    /*Facciamo inviare un intent al BroadcastReciver registrato deliveredPI
                                    * così che ci possa dire se l'sms è stato inviato o meno*/
                                    SmsManager smsManager = SmsManager.getDefault();
                                    smsManager.sendTextMessage(number, null, smsText, deliveredPI, null);
                                } catch (Exception e) {
                                    Toast.makeText(Main.this, "SMS FAILED: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    Log.e("mydbg-error", e.getMessage());
                                }
                            }
                        })
                        .setNegativeButton(getString(string.negative_button), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //DO NOTHING
                            }
                        });
                //Creiamo e mostriamo il dialog
                builder.create().show();
            }
        });
    }






    //Creiamo il menu che verrà aperto quando cliccheremo a lungo su un elemento della listview
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_contact, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if(info == null) Log.e("mydbge", "diodiuncaneporco");
        Log.e("mydbge", "7");
        switch (item.getItemId()){
            //Per inserire il testo del sms che verrà inviato
            case id.menu_add_sms:
                Intent i = new Intent(this, InsertSms.class);
                i.putExtra(getString(string.key_contact_position), (int) info.id);
                startActivity(i);

                break;

            //Per eliminare un contatto dalla rubrica
            case id.menu_delete_contact:
                Log.d("mydbg", "R.id.menu_delete_contact");

                //Recuperiamo il nome dell'elemento cliccato
                String name = SingletonModel_contatto.getmIstance().rubrica.get((int) info.id).name();

                //Rimuoviamo il contatto dal singleton ed aggiorniamo la lista a video.
                SingletonModel_contatto.getmIstance().rubrica.remove((int) info.id);

                //Cancelliamo l'sms dalla sharedPreferences
                Singleton_SharedPref.getmIstance().delete(name, Main.this);

                Toast.makeText(Main.this, "Contatto '" + name + "' eliminato", Toast.LENGTH_LONG).show();

                //Se sta riga non è l'ultima quando clicco poi il menu mi riappare subito dopo, non ho capito perchè
                customAdapterContacts.notifyDataSetChanged();   //Notifichiamo il cambiamento
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void finish() {
        super.finish();
        unregisterReceiver(smsReciver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //Bottone che mi permette di passare all'activity nella quale inserirò un nuovo contatto
            case id.insert_contact:
                Intent i = new Intent(this, AddContact.class);
                startActivityForResult(i, getResources().getInteger(activity_add_contact));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*If che gestisce quando l'activity che mi aggiunge il contatto nella nostra rubrica termina
        * in modo tale da poter mantenere aggiornata la listView del main*/
        if(requestCode == getResources().getInteger(integer.activity_add_contact)) {
            Log.d("mydbg", "onResume Main: aggiorniamo la listView");
            customAdapterContacts.notifyDataSetChanged();   //Aggiorniamo la listView
        }

    }
}
