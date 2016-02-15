package booksforsure.galaxyworks.com.booksforsure;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetDataCallback;
import com.parse.ParseException;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import booksforsure.galaxyworks.com.booksforsure.Holders.History_holder;

/**
 * Created by shrey on 22-10-2015.
 */
public class History_Cards_Adapter extends RecyclerView.Adapter<History_Cards_Adapter.ViewHolder> {

    public List<History_holder> history_list;
    private Context mContext;

    public History_Cards_Adapter(List<History_holder> history, Context context) {
        this.history_list = history;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.history_cardview_layout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        final History_holder hist = history_list.get(i);
        viewHolder.order_price.setText("Price :     " + hist.price);
        if(hist.type == 1){
            viewHolder.order_image.setVisibility(View.VISIBLE);
            viewHolder.order_txt.setVisibility(View.GONE);
            hist.image.getDataInBackground(new GetDataCallback() {
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        Bitmap bMap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        viewHolder.order_image.setImageBitmap(bMap);
                    } else {
                        Toast.makeText(mContext, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else if(hist.type == 2) {
            try {
                Log.e("book",hist.order);
                JSONArray OrderJsonArray = new JSONArray(hist.order);
                for(int j = 0;j<OrderJsonArray.length();j++){
                    JSONObject orderJson = OrderJsonArray.getJSONObject(j);
                    int type = orderJson.getInt("type");
                    if( type == 1){
                        String book_name = orderJson.getString("bookname");
                        String author = orderJson.getString("bookauthor");
                        viewHolder.order_txt.setText( viewHolder.order_txt.getText() + " \n " + book_name + " " + author);
                    }else if( type == 2){
                        String item = orderJson.getString("stationary_title");
                        String description = orderJson.getString("description");
                        Log.e("book",item+ " " + description);
                        viewHolder.order_txt.setText( viewHolder.order_txt.getText() + " \n " + item + " " + description);
                    }else if( type == 3 ){
                        String description = orderJson.getString("description");
                        viewHolder.order_txt.setText( viewHolder.order_txt.getText() + " \n " + description);
                    }
                }

            }catch (Exception e){
                //viewHolder.order_txt.setText(e.toString());
                Log.e("book",e.toString());
            }
        }
        viewHolder.order_time.setText(hist.time);

    }

    @Override
    public int getItemCount() {
        return history_list == null ? 0 : history_list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView order_txt,order_time,order_price;
        ImageView order_image;

        public ViewHolder(View itemView) {
            super(itemView);
            order_txt = (TextView) itemView.findViewById(R.id.history_text);
            order_image = (ImageView) itemView.findViewById(R.id.history_image);
            order_time = (TextView) itemView.findViewById(R.id.history_time);
            order_price = (TextView) itemView.findViewById(R.id.price_txtview);

        }
    }
}
