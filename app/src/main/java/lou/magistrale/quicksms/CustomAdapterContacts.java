package lou.magistrale.quicksms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by lou on 03/04/15.
 */
public class CustomAdapterContacts extends ArrayAdapter<Model_contatto> {
    public CustomAdapterContacts(Context context, int resource, List<Model_contatto> objects){
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.custom_adapter_contacts, null);
        TextView name = (TextView) convertView.findViewById(R.id.name_in_listView);
        Model_contatto c =  getItem(position);
        name.setText(c.name());

        return convertView;
    }
}
