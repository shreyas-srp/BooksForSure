package booksforsure.galaxyworks.com.booksforsure;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.digits.sdk.android.Digits;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Homepage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Fragment fragment;
    BookListAdapter books_adapter;
    LinearLayout linear_layout_list;
    CardView image_cardView;
    final int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    Bitmap image;
    ImageView list_image,icon_camera;
    File imagefile;
    Button image_order_btn,text_order_btn;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.home);

        linear_layout_list = (LinearLayout) findViewById(R.id.order_list);
        books_adapter = new BookListAdapter();

        books_adapter.create(linear_layout_list,getApplicationContext());

        image_cardView = (CardView) findViewById(R.id.image_cardview);

        image_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select_order_type();
            }
        });

        list_image = (ImageView) findViewById(R.id.image_list);
        icon_camera = (ImageView) findViewById(R.id.icon_camera);
        image_order_btn = (Button) findViewById(R.id.image_order_button);

        image_order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_order();
            }
        });

        text_order_btn = (Button) findViewById(R.id.text_order_button);

        text_order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_order();
            }
        });


    }

    public void image_order(){
        if(bitmap == null){
            Toast.makeText(getApplicationContext(),"Please choose a image!",Toast.LENGTH_SHORT).show();
            return;
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] data = stream.toByteArray();
        ParseFile file = new ParseFile("image.txt", data);

        String phone = Digits.getSessionManager().getActiveSession().getPhoneNumber();

        ParseObject image_object = new ParseObject("OrderHistory");
        image_object.put("phoneNumber",phone);
        image_object.put("photoOrder",file);
        image_object.put("type",1);
        image_object.put("flag",1);
        image_object.put("totalAmount",0);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Loading..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        image_object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                progressDialog.dismiss();
                if(e == null){
                    ParsePush push = new ParsePush();
                    push.setChannel("NewOrders");
                    push.setMessage("New Order !");
                    push.sendInBackground();
                    list_image.setImageBitmap(null);
                    list_image.setVisibility(View.GONE);
                    icon_camera.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(),"Order Placed!",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Order couldnt be placed!",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void select_order_type(){
        final CharSequence[] items = { "Book", "Stationery" , "Other", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Type!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Book")) {
                    books_adapter.addView(1);
                } else if (items[item].equals("Stationery")) {
                    books_adapter.addView(2);
                } else if (items[item].equals("Other")) {
                    books_adapter.addView(3);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void text_order(){
        String textOrder = books_adapter.getOrder();
        if( textOrder.equals("Empty")) {
            Toast.makeText(getApplicationContext(), "Enter Details", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(getApplicationContext(), textOrder, Toast.LENGTH_SHORT).show();
        String phone = Digits.getSessionManager().getActiveSession().getPhoneNumber();

        ParseObject OrderText = new ParseObject("OrderHistory");
        OrderText.put("textOrder",textOrder);
        OrderText.put("phoneNumber",phone);
        OrderText.put("type",2);
        OrderText.put("flag",1);
        OrderText.put("totalAmount",0+"");

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Loading..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        OrderText.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                progressDialog.dismiss();
                if(e == null){
                    ParsePush push = new ParsePush();
                    push.setChannel("NewOrders");
                    push.setMessage("New Order !");
                    push.sendInBackground();
                    Toast.makeText(getApplicationContext(),"Order Placed!",Toast.LENGTH_SHORT).show();
                    Intent restart = new Intent(getApplicationContext(),Homepage.class);
                    startActivity(restart);
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"Order couldnt be placed!",Toast.LENGTH_SHORT).show();
                    Log.e("order",e.toString());
                }
            }
        });

        return;
    }


    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        imagefile = destination;

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            //Toast.makeText(context,"sdggfdgdf",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            //Toast.makeText(context,"sdggfdgdf",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        //layoutParams = coverpic.getLayoutParams();
        //LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300);
        //coverpic.setLayoutParams(layoutParams);
        icon_camera.setVisibility(View.GONE);
        list_image.setVisibility(View.VISIBLE);
        list_image.setImageBitmap(thumbnail);
        image = thumbnail;
        bitmap = thumbnail;
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Uri selectedImageUri = data.getData();
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = getContentResolver().query(selectedImageUri, projection, null, null,
                null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();

        String selectedImagePath = cursor.getString(column_index);

        Bitmap bm;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);
        final int REQUIRED_SIZE = 200;
        int scale = 1;
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                && options.outHeight / scale / 2 >= REQUIRED_SIZE)
            scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(selectedImagePath, options);

        //LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300);
        //list_image.setLayoutParams(layoutParams);
        icon_camera.setVisibility(View.GONE);
        list_image.setVisibility(View.VISIBLE);
        list_image.setImageBitmap(bm);
        image = bm;
        bitmap = bm;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.homepage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            //nothing
        } else if (id == R.id.history) {
            Intent history = new Intent(getApplicationContext(),History.class);
            startActivity(history);
        } else if (id == R.id.account) {
            Intent account = new Intent(getApplicationContext(),Account.class);
            startActivity(account);
        } else if (id == R.id.share) {


        } else if (id == R.id.logout) {
            Digits.getSessionManager().clearActiveSession();
            Intent logout = new Intent(getApplicationContext(),Login.class);
            startActivity(logout);
            SharedPreferences user_details = getSharedPreferences("user_details_sharedpref",MODE_PRIVATE);
            SharedPreferences.Editor user_details_editor = user_details.edit();
            user_details_editor.clear();
            user_details_editor.commit();
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
