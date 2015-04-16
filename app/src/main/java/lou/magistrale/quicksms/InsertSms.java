package lou.magistrale.quicksms;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class InsertSms extends ActionBarActivity implements View.OnClickListener{
    private EditText sms;
    private Button updateSms;
    private int contactClickedPos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_sms);
        contactClickedPos = getIntent().getIntExtra(getString(R.string.key_contact_position), -1);
        /*STA RIGA SOTTO NON è LEGGIBILE MAI !!!! Cmq, Voglio leggere l'sms del contatto in posizione 'contactClickedPos
        * in modo da caricarlo nella edit text così che l'untente possa vedere qual'era il messaggio precedentemente
        * caricato. Il primo argomento lunghissimo di read() semplicemente recupera il nome del contatto cliccato nella
        * listView nell'activity precedente*/
        String oldSms = Singleton_SharedPref.getmIstance().read(SingletonModel_contatto.getmIstance().rubrica.get(contactClickedPos).name(), this);

        sms = (EditText) findViewById(R.id.insert_sms);
        sms.setText(oldSms);

        updateSms = (Button) findViewById(R.id.button_upload_sms);
        updateSms.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_upload_sms:
                //Recuperiamo la posizione del contatto all'interno della rubrica
                Log.d("mydbg", "Il contatto selezionato è in posizione: " + Integer.toString(contactClickedPos));

                String smsWrited = sms.getText().toString();
                //L'sms inserito non può essere vuoto
                if(smsWrited.equals("")){
                    Toast.makeText(InsertSms.this, "Sms can't be empty", Toast.LENGTH_LONG).show();
                }else {
                    //Salviamo l'sms all'interno dellle informazioni riguardanti quel contatto
                    //La rubrica è una lista di contatti, recuperiamo il contatto in posizione contactClickedPos e ci salviamo l'sms
                    SingletonModel_contatto.getmIstance().rubrica.get(contactClickedPos).setSms(sms.getText().toString(), this);
                    finish();
                }

                break;
        }
    }
}
