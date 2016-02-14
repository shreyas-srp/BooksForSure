package booksforsure.galaxyworks.com.booksforsure;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class Welcome_slide_3 extends Fragment {

    Button home;

    public Welcome_slide_3() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_welcome_slide_3, container, false);

        home = (Button) rootview.findViewById(R.id.home_btn);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences user_details = getContext().getSharedPreferences("user_details_sharedpref", getContext().MODE_PRIVATE);
                SharedPreferences.Editor user_details_editor = user_details.edit();
                user_details_editor.putBoolean("new", false);
                user_details_editor.commit();

                Intent mainIntent = new Intent(getContext(), Homepage.class);
                startActivity(mainIntent);
                getActivity().finish();
            }
        });


        return rootview;
    }

}
