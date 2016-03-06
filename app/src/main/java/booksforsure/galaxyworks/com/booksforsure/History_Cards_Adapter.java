package booksforsure.galaxyworks.com.booksforsure;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import booksforsure.galaxyworks.com.booksforsure.Holders.History_holder;

import static android.graphics.Color.GREEN;

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

        if(hist.status==1){

           // viewHolder.confirm.setVisibility(View.GONE);
            viewHolder.confirm.setEnabled(false);

        }
        else if(hist.status==2){
            //viewHolder.cancel.setVisibility(View.GONE);
            viewHolder.cancel.setEnabled(false);
        }

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
                        String isbn = orderJson.getString("isbn");
                        viewHolder.order_txt.setText( viewHolder.order_txt.getText() + " \nBook     :" + book_name + "\nAuthor     :" + author + "\nISBN:" + isbn );
                    }else if( type == 2){
                        String item = orderJson.getString("stationary_title");
                        String description = orderJson.getString("description");
                        Log.e("book",item+ " " + description);
                        viewHolder.order_txt.setText( viewHolder.order_txt.getText() + " \nItem     :"+ item + "\nDescription     :" + description);
                    }else if( type == 3 ){
                        String description = orderJson.getString("description");
                        viewHolder.order_txt.setText( viewHolder.order_txt.getText() + " \nDescription     :" + description);
                    }
                }

            }catch (Exception e){
                //viewHolder.order_txt.setText(e.toString());
                Log.e("book",e.toString());
            }
        }
        viewHolder.order_time.setText(hist.time);
        viewHolder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phNumber = "+919916371851";
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phNumber));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

        viewHolder.confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("OrderHistory");
                    query.whereEqualTo("objectId",hist.orderID);
                    query.getFirstInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject object, ParseException e) {
                            object.put("status", 1);
                            object.saveInBackground();
                        }
                    });
                            ParsePush push = new ParsePush();
                    push.setChannel("NewOrders");
                    JSONObject data = null;
                    data = new JSONObject("{\"alert\": \"Order Confirmation Recieved !\",\"title\": \"Books For Sure Admin\",\"orderid\":\"...\",\"time\":\"...\"}");
                    data.put("orderid",hist.orderID);
                    data.put("time",hist.time);
                    push.setData(data);
                    push.sendInBackground();
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }

        });

        viewHolder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("OrderHistory");
                    query.whereEqualTo("objectId",hist.orderID);
                    query.getFirstInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject object, ParseException e) {
                            object.put("status",2);
                            object.saveInBackground();
                        }
                    });
                    ParsePush push = new ParsePush();
                    push.setChannel("NewOrders");
                    JSONObject data = null;
                    data = new JSONObject("{\"alert\": \"Order Confirmation Recieved !\",\"title\": \"Books For Sure Admin\",\"orderid\":\"...\",\"time\":\"...\"}");
                    data.put("orderid",hist.orderID);
                    data.put("time",hist.time);
                    push.setData(data);
                    push.sendInBackground();
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return history_list == null ? 0 : history_list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView order_txt,order_time,order_price;
        Button confirm,cancel,call;
        ImageView order_image;

        public ViewHolder(View itemView) {
            super(itemView);
            order_txt = (TextView) itemView.findViewById(R.id.history_text);
            order_image = (ImageView) itemView.findViewById(R.id.history_image);
            order_time = (TextView) itemView.findViewById(R.id.history_time);
            order_price = (TextView) itemView.findViewById(R.id.price_txtview);
            confirm = (Button) itemView.findViewById(R.id.confirm);
            cancel = (Button) itemView.findViewById(R.id.cancel);
            call= (Button) itemView.findViewById(R.id.call);

        }
    }
}
