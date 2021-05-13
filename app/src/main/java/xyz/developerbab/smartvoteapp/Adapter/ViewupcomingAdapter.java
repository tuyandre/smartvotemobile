package xyz.developerbab.smartvoteapp.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import xyz.developerbab.smartvoteapp.Model.Candidate;
import xyz.developerbab.smartvoteapp.R;

public class ViewupcomingAdapter extends RecyclerView.Adapter<ViewupcomingAdapter.ViewupcomingAdadpterViewHolder> {

    private Context mcontext;
    private List<Candidate> muploads;


    private OnItemClickListener mListener;

    public ViewupcomingAdapter(Context context, List<Candidate> uploads) {

        this.mcontext = context;
        this.muploads = uploads;
    }

    @NonNull
    @Override
    public ViewupcomingAdadpterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_candidate, parent, false);

        return new ViewupcomingAdadpterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewupcomingAdadpterViewHolder holder, int position) {

        Candidate uploadCurrent = muploads.get(position);

        if (uploadCurrent != null) {

            holder.tvcandidatename.setText(uploadCurrent.getCandidate_name());
            holder.tvdob.setText(uploadCurrent.getDob());
            holder.tvparty.setText(uploadCurrent.getParty());
            holder.tvprovincename.setText(uploadCurrent.getProvince_name());
            holder.tvdistrictname.setText(uploadCurrent.getDistrict_name());
            holder.tvstrength.setText(uploadCurrent.getStrength());
            holder.tvtestids.setText(uploadCurrent.getProvince_id() + uploadCurrent.getDistrict_id() + uploadCurrent.getSeason_id());


            Picasso.with(mcontext)
                    .load("http://vote.developerbab.xyz/backend/logos/" + uploadCurrent.getProfile())
                    .placeholder(R.drawable.profile)
                    .fit()
                    .into(holder.imgprofile);


            Picasso.with(mcontext)
                    .load("http://vote.developerbab.xyz/backend/candidates/" + uploadCurrent.getLogo())
                    .placeholder(R.drawable.profile)
                    .fit()
                    .centerInside()
                    .into(holder.imglogo);

        }


    }


    @Override
    public int getItemCount() {
        return muploads.size();
    }

    public class ViewupcomingAdadpterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvcandidatename, tvdob, tvparty, tvprovincename, tvdistrictname, tvstrength, tvtestids;
        public CircleImageView imgprofile;
        public ImageView imglogo;

        public ViewupcomingAdadpterViewHolder(@NonNull View itemView) {

            super(itemView);
            tvtestids = itemView.findViewById(R.id.tvtestids);
            tvcandidatename = itemView.findViewById(R.id.tvcandidatename);
            tvdob = itemView.findViewById(R.id.tvdob);
            tvparty = itemView.findViewById(R.id.tvpartyname);
            tvprovincename = itemView.findViewById(R.id.tvprovincename);
            tvdistrictname = itemView.findViewById(R.id.tvdistrictname);
            tvstrength = itemView.findViewById(R.id.tvstrength);

            imglogo = itemView.findViewById(R.id.imglogo);
            imgprofile = itemView.findViewById(R.id.imgprofile);


            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }
            }

        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;

    }


}
