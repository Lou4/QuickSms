package lou.magistrale.quicksms;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by lou on 03/04/15.
 */
public class Singleton_SharedPref{
    private static Singleton_SharedPref mIstance = null;

    //Variabili riguardanti le SHARED_PREFERENCES
    private SharedPreferences xmlFile;    //Qua andro a salvare i miei sms
    private SharedPreferences.Editor editor;

    //Costruttore vuoto, al momento non mi serve
    private Singleton_SharedPref(){
    }

    public static Singleton_SharedPref getmIstance(){
        Log.d("mydbg", "Singleton_SharedPref getmIstance()");
        if(mIstance == null){
            Log.d("mydbg", "Singleton_SharedPref getmIstance() - if");
            mIstance = new Singleton_SharedPref();
        }
        Log.d("mydbg", "Singleton_SharedPref getmIstance() - before return");
        return mIstance;
    }
    public String read(String key, Activity activity){
        //Estraiamo l'sms contenuto nel file (se esiste, se no mi ritorna il valore di default
        SharedPreferences sharedPreferences = activity.getSharedPreferences("foo", Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, activity.getString(R.string.default_value_sms));
    }

    public void write(String key, String value, Activity activity){
        SharedPreferences sharedPreferences = activity.getSharedPreferences("foo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
        Log.e("mydbg", "write");
    }

    public void delete(String key, Activity activity){
        SharedPreferences sharedPreferences = activity.getSharedPreferences("foo", Context.MODE_PRIVATE);
        sharedPreferences.edit().remove(key).commit();
    }
}
