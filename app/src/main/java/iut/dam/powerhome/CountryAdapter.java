package iut.dam.powerhome;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class CountryAdapter extends BaseAdapter {
    private Context context;
    private List<Country> countryList;
    private LayoutInflater inflater;

    public CountryAdapter(Context context, int resource, List<Country> countryList) {
        this.context = context;
        this.countryList = countryList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return countryList.size();
    }

    @Override
    public Object getItem(int position) {
        return countryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    private View createView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_country, parent, false);
        }

        Country country = (Country) getItem(position);

        ImageView imageView = convertView.findViewById(R.id.countryFlag);
        TextView textView = convertView.findViewById(R.id.countryName);

        if (country != null) {
            imageView.setImageResource(country.getFlagResource());
            textView.setText(country.getName());
        }

        return convertView;
    }
}
