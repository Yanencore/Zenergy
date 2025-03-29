package iut.dam.powerhome;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ApplianceAdapter extends BaseAdapter {
    private Context context;
    private List<Appliance> applianceList;
    private LayoutInflater inflater;

    public ApplianceAdapter(Context context, List<Appliance> applianceList) {
        this.context = context;
        this.applianceList = applianceList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return applianceList.size();
    }

    @Override
    public Object getItem(int position) {
        return applianceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_appliences, parent, false);
        }

        // Get the appliance object
        Appliance appliance = applianceList.get(position);

        // Initialize views
        TextView applianceName = convertView.findViewById(R.id.applianceName);
        TextView applianceWattage = convertView.findViewById(R.id.applianceWattage);
        ImageView applianceIcon = convertView.findViewById(R.id.applianceIcon);

        // Set the appliance name and wattage
        applianceName.setText(appliance.getName());
        applianceWattage.setText(appliance.getWattage() + "W");

        // Set the corresponding icon based on appliance name
        switch (appliance.getName()) {
            case "Machine a laver":
                applianceIcon.setImageResource(R.drawable.ic_laundry);
                break;
            case "Aspirateur":
                applianceIcon.setImageResource(R.drawable.ic_cleaner);
                break;
            case "Climatiseur":
                applianceIcon.setImageResource(R.drawable.ic_clim);
                break;
            case "Fer a repasser":
                applianceIcon.setImageResource(R.drawable.ic_iron);
                break;
        }

        return convertView;
    }
}
