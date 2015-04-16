package lou.magistrale.quicksms;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.TwoLineListItem;

import java.util.List;


public class AddContact extends ListActivity implements View.OnClickListener {

    private EditText name;
    private EditText number;

    //Mi servono per visualizzare l'elenco dei contatti in rubrica
    private ListView listView;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        name = (EditText) findViewById(R.id.new_name);
        number = (EditText) findViewById(R.id.new_number);

        Button add = (Button) findViewById(R.id.confirm_insert);
        add.setOnClickListener(this);

        listView = getListView();
        //Aggiungiamo i contatti della rubrica alla list view di questa activity
        addContactsToListView(listView);    //TODO: capire se spostare sto metodo in un'altra classe

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO: TwoLineListItem è deprecato !!!
                String name_s = ((TwoLineListItem) view).getText1().getText().toString();
                String number_s = ((TwoLineListItem) view).getText2().getText().toString();

                name.setText(name_s);
                number.setText(number_s);
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.confirm_insert:
                String contactName = name.getText().toString();
                String contactNumber = number.getText().toString();

                //Se manca uno dei due campi non facciamo proseguire con l'inserimento
                if(contactName.equals("") || contactNumber.equals("")){
                    Toast.makeText(AddContact.this, "Missing field !!", Toast.LENGTH_LONG).show();
                }else {
                    //Creiamo una variabile model_contatto estrapolando il nome ed il numero dalle editText
                    Model_contatto model_contatto = new Model_contatto(name.getText().toString(), number.getText().toString());
                    //Aggiungiamo il contatto alla lista di contatti esistenti
                    SingletonModel_contatto.getmIstance().rubrica.add(model_contatto);

                    //Facciamo tornare indietro un intent alla prima activity così che lei possa aggiornare la listView
                    Intent i = new Intent();
                    setResult(RESULT_CANCELED, i);  //Non voglio ritornare dati ma solo far eseguire un task alla prima activity una volta che questa termina
                    finish();
                }
                break;
        }
    }

    private void addContactsToListView(ListView lv){
        //Facciamo la cosa del cursor
        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        startManagingCursor(cursor);    //TODO: il mio cell ha le API 8, non posso usare il CursorLoader

        String[] from = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone._ID};
        int[] to = {android.R.id.text1, android.R.id.text2};

        SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor,from, to);
        setListAdapter(listAdapter);
        lv = getListView();
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }
}
