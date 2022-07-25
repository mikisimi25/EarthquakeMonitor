package com.example.earthquakemonitor.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.earthquakemonitor.Earthquake;
import com.example.earthquakemonitor.R;
import com.example.earthquakemonitor.databinding.EqListItemBinding;

//Adapter -> adapta un objeto a un recicler para poder mostrarlo
//Earthquake -> objeto que vamos a adaptar
//ViewHolder -> se encarga de reciclar los views cuando hacemos scroll en el recycler view
public class EqAdapter extends ListAdapter<Earthquake, EqAdapter.EqViewHolder> {

    private Context context;

    //Dice al adapter si los items son iguales o diferentes
    //Ayuda al recycler view a reordenar la lista, o cuando editamos items de la lista o eliminamos
    public static final DiffUtil.ItemCallback<Earthquake> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Earthquake>() {
                @Override
                public boolean areItemsTheSame(@NonNull Earthquake oldItem, @NonNull Earthquake newItem) {
                    return oldItem.getId().equals(newItem.getId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull Earthquake oldItem, @NonNull Earthquake newItem) {
                    return oldItem.equals(newItem);
                }
            };

    protected EqAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }


    interface OnItemClickListener {
        void onItemClick(Earthquake earthquake);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    @NonNull
    @Override
    public EqAdapter.EqViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate -> convertir un layout en un view
        //R.layout.eq_list_item -> recurso del layout
        EqListItemBinding binding = EqListItemBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new EqViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EqAdapter.EqViewHolder holder, int position) {
        Earthquake earthquake = getItem(position);

        holder.bind(earthquake);
    }

    class EqViewHolder extends RecyclerView.ViewHolder {

        private final EqListItemBinding binding;

        //itemView -> cada uno de los elementos que vamos a pintar
        public EqViewHolder(@NonNull EqListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Earthquake earthquake) {
//            binding.magnitudeText.setText(String.valueOf(earthquake.getMagnitude()));
            binding.magnitudeText.setText(context.getString(R.string.magnitude_format, earthquake.getMagnitude()));
            binding.placeText.setText(earthquake.getPlace());

            //Nos permite que al dar scroll rapidamente se pinte todo de manera inmediata
            //On click listener para cualquier punto de eq_list_item
            binding.getRoot().setOnClickListener( v -> {
                onItemClickListener.onItemClick(earthquake);
            });

            binding.executePendingBindings();
        }
    }
}
