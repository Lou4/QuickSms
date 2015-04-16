package lou.magistrale.quicksms;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lou on 02/04/15.
 */
public class SingletonModel_contatto {
    private static SingletonModel_contatto mIstance = null;
    List<Model_contatto> rubrica;

    private SingletonModel_contatto(){
       rubrica = new ArrayList<Model_contatto>();
    }

    public static SingletonModel_contatto getmIstance(){
        Log.d("mydbg", "SingletonModel_contatto getmIstance()");

        if(mIstance == null){
            Log.d("mydbg", "sto creando l'istanza singleton di rubrica");
            mIstance = new SingletonModel_contatto();
        }
        return mIstance;
    }

    @Override
    public String toString() {
        StringBuilder tmp = new StringBuilder();
        for(Model_contatto m: rubrica){
            tmp.append( m.name() + " " + m.number() + "\n");
        }
        return tmp.toString();
    }
}

