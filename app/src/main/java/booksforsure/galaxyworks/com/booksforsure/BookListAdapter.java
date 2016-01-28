package booksforsure.galaxyworks.com.booksforsure;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by shrey on 26-01-2016.
 */
public class BookListAdapter {

    public Context context;
    LinearLayout linearLayout;
    public int book_count = 0,display_book_count = 0;
    EditText name,author;
    ImageView cancel;

    public void create(LinearLayout linearLayout, Context context){
        this.context = context;
        this.linearLayout = linearLayout;

    }

    public void addView(){
        book_count++;

        View v = LayoutInflater.from(context).inflate(R.layout.books_edittext, linearLayout , false);
        name = (EditText) v.findViewById(R.id.bookname_edittxt);
        author = (EditText) v.findViewById(R.id.bookauthor_edittxt);
        cancel = (ImageView) v.findViewById(R.id.cancel_action);

        v.setId(900+book_count);

        name.setId(100+book_count);
        author.setId(200+book_count);
        cancel.setId(300+book_count);

        name.requestFocus();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vi) {

                int id = vi.getId();
                Toast.makeText(context,"cancelled"+id,Toast.LENGTH_SHORT).show();
                View v = linearLayout.findViewById(id - 300 + 900);
                linearLayout.removeView(v);
            }
        });
        Toast.makeText(context,book_count+"",Toast.LENGTH_SHORT).show();

        linearLayout.addView(v);
    }

    public String getOrder(){
        String order = new String();
        JSONArray orderJsonArray = new JSONArray();
        JSONObject orderJson = new JSONObject();
        try {
            orderJson.put("bookname", "sdfjbds");
            orderJson.put("bookauthor", "dsjfbjdsbf");
            orderJsonArray.put(0,orderJson);
            orderJsonArray.put(1,orderJson);
            return orderJsonArray.toString();
        }catch (Exception e){
            Log.e("error",e.toString());
        }

        return order;
    }

}
