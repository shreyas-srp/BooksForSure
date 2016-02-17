package booksforsure.galaxyworks.com.booksforsure;

import android.content.Context;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by shrey on 26-01-2016.
 */
public class BookListAdapter {

    public Context context;
    LinearLayout linearLayout;
    public int book_count = 0,act_count = 0;
    public int set_flag[] = new int[1001];

    public void create(LinearLayout linearLayout, Context context){
        this.context = context;
        this.linearLayout = linearLayout;

        for(int i = 0;i<1001;i++)
            set_flag[i] = 0;

    }

    public void addView(int type){
        book_count++;
        act_count++;
        set_flag[book_count] = type;

        final EditText name,author,quantity,edition,isbn;
        ImageView cancel;
        RadioGroup radioGroup;
        Button plus_btn,minus_btn;
        RadioButton radio_new,radio_old;



        View v = LayoutInflater.from(context).inflate(R.layout.books_edittext, linearLayout , false);
        name = (EditText) v.findViewById(R.id.bookname_edittxt);
        isbn = (EditText) v.findViewById(R.id.isbn_edittxt);
        author = (EditText) v.findViewById(R.id.bookauthor_edittxt);
        cancel = (ImageView) v.findViewById(R.id.cancel_action);
        radioGroup = (RadioGroup) v.findViewById(R.id.radio_group);
        quantity = ( EditText) v.findViewById(R.id.quantity_edittext);
        edition = ( EditText) v.findViewById(R.id.bookedition_edittxt);
        plus_btn = (Button) v.findViewById(R.id.plus_button);
        minus_btn = (Button) v.findViewById(R.id.minus_button);
        radio_new = (RadioButton) v.findViewById(R.id.radio_btn_new);
        radio_old = (RadioButton) v.findViewById(R.id.radio_btn_old);

        if( type == 1){
            radioGroup.setVisibility(View.VISIBLE);
        }else if( type == 2){
            name.setHint("Stationary Title");
            author.setHint("Stationary Description");
            author.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            edition.setVisibility(View.GONE);
            isbn.setVisibility(View.GONE);
        }else if( type == 3){
            name.setHint("Description..");
            name.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            author.setVisibility(View.GONE);
            edition.setVisibility(View.GONE);
            isbn.setVisibility(View.GONE);
        }

        v.setId(9000+book_count);

        name.setId(1000+book_count);
        author.setId(2000+book_count);
        cancel.setId(3000+book_count);
        quantity.setId(4000+book_count);
        radio_new.setId(5000 + book_count);
        radio_old.setId(6000 + book_count);
        edition.setId(7000+book_count);
        isbn.setId(8000+book_count);

        name.requestFocus();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vi) {

                int id = vi.getId();
                //Toast.makeText(context,"cancelled"+id,Toast.LENGTH_SHORT).show();
                View v = linearLayout.findViewById(id - 3000 + 9000);
                linearLayout.removeView(v);
                set_flag[id - 3000] = 0;
                act_count--;
            }
        });


        plus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quantity.getText().toString().length() == 0){
                    quantity.setText("1");
                    return;
                }
                int x = Integer.parseInt(quantity.getText().toString());
                x++;
                quantity.setText(x+"");
            }
        });

        minus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quantity.getText().toString().length() == 0){
                    quantity.setText("1");
                    return;
                }
                int x = Integer.parseInt(quantity.getText().toString());
                x--;
                if( x < 1) {
                    quantity.setText("1");
                    return;
                }
                quantity.setText(x+"");
            }
        });

        linearLayout.addView(v);
    }

    public String getOrder(){

        JSONArray orderJsonArray = new JSONArray();

        if(act_count > 0) {

            try {

                for (int i = 1; i <= book_count; i++) {
                    if (set_flag[i] != 0) {
                        int type = set_flag[i];
                        EditText name_edit = (EditText) linearLayout.findViewById(i + 1000);
                        EditText author_edit = (EditText) linearLayout.findViewById(i + 2000);
                        EditText quantity_edit = (EditText) linearLayout.findViewById(i + 4000);
                        RadioButton new_radio = (RadioButton) linearLayout.findViewById(i+5000);
                        RadioButton old_radio = (RadioButton) linearLayout.findViewById(i+6000);
                        EditText edition_edit = (EditText) linearLayout.findViewById(i + 7000);
                        EditText isbn_edit = (EditText) linearLayout.findViewById(i + 8000);

                        String book_name = name_edit.getText().toString();
                        String book_author = author_edit.getText().toString();
                        String book_edition = edition_edit.getText().toString();
                        String isbn = isbn_edit.getText().toString();
                        int quantity;
                        if(quantity_edit.getText().toString().length() == 0) quantity = 1;
                        else quantity = Integer.parseInt(quantity_edit.getText().toString());

                        if(book_name.equals("")){
                            book_name = "nil";
                        }
                        if(book_author.equals("")){
                            book_author = "nil";
                        }
                        if(book_edition.equals("")){
                            book_edition = "nil";
                        }
                        if(isbn.equals("")){
                            isbn = "nil";
                        }

                        JSONObject orderJson = new JSONObject();


                        if( type == 1) {
                            orderJson.put("type",type);
                            orderJson.put("bookname", book_name);
                            orderJson.put("bookauthor", book_author);
                            orderJson.put("quantity",quantity);
                            orderJson.put("edition",book_edition);
                            orderJson.put("isbn",isbn);
                            if( new_radio.isChecked()) orderJson.put("old_new","new");
                            else if( old_radio.isChecked()) orderJson.put("old_new","old");
                            else orderJson.put("old_new","new (default)");
                            orderJsonArray.put(i - 1, orderJson);
                        }else if( type == 2){
                            orderJson.put("type",type);
                            orderJson.put("stationary_title", book_name);
                            orderJson.put("description", book_author);
                            orderJson.put("quantity",quantity);
                            orderJsonArray.put(i - 1, orderJson);
                        }else if( type == 3){
                            orderJson.put("type",type);
                            orderJson.put("description", book_name);
                            orderJson.put("quantity",quantity);
                            orderJsonArray.put(i - 1, orderJson);
                        }

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
