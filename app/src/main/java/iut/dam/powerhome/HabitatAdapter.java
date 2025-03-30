package iut.dam.powerhome;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

public class HabitatAdapter extends BaseAdapter {
    private Context context;
    private List<Habitat> habitatList;
    private LayoutInflater inflater;


    public HabitatAdapter(Context context, List<Habitat> habitatList) {
        this.context = context;
        this.habitatList = habitatList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return habitatList.size();
    }

    @Override
    public Object getItem(int position) {
        return habitatList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_habitat, parent, false);
        }

        Habitat habitat = habitatList.get(position);

        TextView nomResident = convertView.findViewById(R.id.nomResident);
        TextView nbEquipements = convertView.findViewById(R.id.nbEquipements);
        TextView etage = convertView.findViewById(R.id.etage);

        nomResident.setText(habitat.getResidentName());

        nbEquipements.setText(habitat.getAppliances().size() + " appareils");

        etage.setText(String.valueOf(habitat.getEtage()));

        ImageView icone1 = convertView.findViewById(R.id.icone1);
        ImageView icone2 = convertView.findViewById(R.id.icone2);
        ImageView icone3 = convertView.findViewById(R.id.icone3);
        ImageView icone4 = convertView.findViewById(R.id.icone4);
        ImageView[] icons = new ImageView[]{icone1, icone2, icone3, icone4};


        for (int i = 0; i < icons.length; i++) {
            if (i >= habitat.getAppliances().size()) {
                icons[i].setVisibility(View.GONE);
            } else {
                icons[i].setVisibility(View.VISIBLE);
                String applianceName = habitat.getAppliances().get(i).getName();

                if (Objects.equals(applianceName, "Machine a laver")) {
                    icons[i].setImageResource(R.drawable.ic_laundry);
                } else if (Objects.equals(applianceName, "Aspirateur")) {
                    icons[i].setImageResource(R.drawable.ic_cleaner);
                } else if (Objects.equals(applianceName, "climatiseur")) {
                    icons[i].setImageResource(R.drawable.ic_clim);
                } else if (Objects.equals(applianceName, "Fer a repasser")) {
                    icons[i].setImageResource(R.drawable.ic_iron);
                }
            }
        }
        return convertView;
    }
}
