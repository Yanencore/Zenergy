package iut.dam.powerhome;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;

public class HabitatAdapter extends BaseAdapter {
    private Context context;
    private List<Habitat> habitants;
    private LayoutInflater inflater;

    public HabitatAdapter(Context context, List<Habitat> habitants) {
        this.context = context;
        this.habitants = habitants;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return habitants.size();
    }

    @Override
    public Object getItem(int position) {
        return habitants.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_habitat, parent, false);
        }

        TextView nomTextView = convertView.findViewById(R.id.nomResident);
        TextView equipementsTextView = convertView.findViewById(R.id.nbEquipements);
        TextView etageTextView = convertView.findViewById(R.id.etage);
        LinearLayout equipementsLayout = convertView.findViewById(R.id.equipementsLayout);

        Habitat habitat = habitants.get(position);
        nomTextView.setText(habitat.getNom());
        equipementsTextView.setText(habitat.getNbEquipements() + (habitat.getNbEquipements() > 1 ? " équipements" : " équipement"));
        etageTextView.setText("" + habitat.getEtage());

        equipementsLayout.removeAllViews();
        for (int icon : habitat.getEquipementsIcons()) {
            ImageView imageView = new ImageView(context);
            imageView.setImageResource(icon);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(60, 60));
            equipementsLayout.addView(imageView);
        }

        return convertView;
    }
}
