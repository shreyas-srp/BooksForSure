package booksforsure.galaxyworks.com.booksforsure;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by shrey on 26-01-2016.
 */
public class BookListAdapter {

    public Context context;
    LinearLayout linearLayout;
    public int book_count = 0,act_count = 0;
    public int set_flag[] = new int[101];
    EditText name,author;
    ImageView cancel;

    public void create(LinearLayout linearLayout, Context context){
        this.context = context;
        this.linearLayout = linearLayout;

        for(int i = 0;i<101;i++)
            set_flag[i] = 0;

    }

    public void addView(int type){
        book_count++;
        act_count++;
        set_flag[book_count] = 1;

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
                //Toast.makeText(context,"cancelled"+id,Toast.LENGTH_SHORT).show();
                View v = linearLayout.findViewById(id - 300 + 900);
                linearLayout.removeView(v);
                set_flag[id - 300] = 0;
                act_count--;
            }
        });
        //Toast.makeText(context,book_count+"",Toast.LENGTH_SHORT).show();

        linearLayout.addView(v);
    }

    public String getOrder(){

        JSONArray orderJsonArray = new JSONArray();

        if(act_count > 0) {

            try {

                for (int i = 1; i <= book_count; i++) {
                    if (set_flag[i] == 1) {
                        EditText name_edit = (EditText) linearLayout.findViewById(i + 100);
                        EditText author_edit = (EditText) linearLayout.findViewById(i + 200);

                        String book_name = name_edit.getText().toString();
                        String book_author = author_edit.getText().toString();

                        JSONObject orderJson = new JSONObject();
                        orderJson.put("bookname", book_name);
                        orderJson.put("bookauthor", book_author);
                        orderJsonArray.put(i - 1, orderJson);
                    }
                }

            } catch (Exception e) {
                Log.e("error", e.toString());
            }
        }
        if( orderJsonArray.length() == 0)   return "Empty";
        else
            return orderJsonArray.toString();
    }

}
