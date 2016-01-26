package booksforsure.galaxyworks.com.booksforsure;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by shrey on 26-01-2016.
 */
public class BookListAdapter {

    public Context context;
    LinearLayout linearLayout;
    public int book_count = 0;
    EditText name,author;
    TextView number;
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
        number = (TextView) v.findViewById(R.id.book_no_txtview);
        cancel = (ImageView) v.findViewById(R.id.cancel_action);
        //name.setId();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vi) {
                Toast.makeText(context,"cancelled",Toast.LENGTH_SHORT).show();
            }
        });
        Toast.makeText(context,book_count+"",Toast.LENGTH_SHORT).show();



        linearLayout.addView(v);
    }

}
