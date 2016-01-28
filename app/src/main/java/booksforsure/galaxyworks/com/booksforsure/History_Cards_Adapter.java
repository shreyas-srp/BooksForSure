package booksforsure.galaxyworks.com.booksforsure;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import booksforsure.galaxyworks.com.booksforsure.Holders.History_holder;

/**
 * Created by shrey on 22-10-2015.
 */
public class History_Cards_Adapter extends RecyclerView.Adapter<History_Cards_Adapter.ViewHolder> {

    public static List<History_holder> history_list;
    private int rowLayout;
    private Context mContext;

    public History_Cards_Adapter(List<History_holder> history, Context context) {
        this.history_list = history;
        //this.rowLayout = rowLayout;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.history_cardview_layout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        History_holder hist = history_list.get(i);

        if(hist.type == 1){
            viewHolder.order_image.setVisibility(View.VISIBLE);
            viewHolder.order_txt.setVisibility(View.GONE);
            viewHolder.order_image.setImageBitmap(hist.image);
        }
        else if(hist.type == 2){
            viewHolder.order_txt.setText(hist.order);
        }
        viewHolder.order_time.setText(hist.time);

    }

    @Override
    public int getItemCount() {
        return history_list == null ? 0 : history_list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView order_txt,order_time;
        ImageView order_image;

        public ViewHolder(View itemView) {
            super(itemView);
            order_txt = (TextView) itemView.findViewById(R.id.history_text);
            order_image = (ImageView) itemView.findViewById(R.id.history_image);
            order_time = (TextView) itemView.findViewById(R.id.history_time);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {

                }
            });


        }

    }
}
