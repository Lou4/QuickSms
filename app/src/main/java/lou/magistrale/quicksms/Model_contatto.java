package lou.magistrale.quicksms;


import android.app.Activity;
import android.util.Log;

/**
 * Created by lou on 02/04/15.
 */
public class Model_contatto {
    /*Il nome e il numero di telefono li insersco da un activity, l'sms dopo un long click sul nome del contatto
    * quindi implementeremo il setter per quel campo.*/
    private String contactName;
    private String telNumber;

    //Activity mi serve per accedere alle sharedPreference
    public Model_contatto(String name, String number){
        this.contactName = name;
        this.telNumber = number;
    }

    public String name(){
        return this.contactName;
    }

    public String number(){
        return this.telNumber;
    }

    //Recuperiamo l'sms all'interno delle sharedPreferences
    public String getSms(String key, Activity activity){
        return Singleton_SharedPref.getmIstance().read(key, activity);

    }

    //Scriviamo un sms all'interno delle sharedPreferences
    public void setSms(String sms, Activity activity){
        Log.d("mydbg", "setSms: " + sms);
        if(Singleton_SharedPref.getmIstance() == null) Log.e("mydbg", "sharedpreference null");
        Singleton_SharedPref.getmIstance().write(contactName, sms, activity);
    }
}
