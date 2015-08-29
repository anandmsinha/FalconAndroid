package com.example.anand.falconproduction.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.anand.falconproduction.R;

public class EditImageActivity extends Activity {
  Bitmap originalBitmap,image;
  ImageView iv_ttx;
  EditText et_sample;
  Spinner color_method, shape_method;
  Paint paint;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit_image2);
    //image view
    iv_ttx = (ImageView) findViewById(R.id.iv_ttx);
    //to get screen width and hight
    DisplayMetrics displaymetrics = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
    //dimentions x,y of device to create a scaled bitmap having similar dimentions to screen size
    int height1 = displaymetrics.heightPixels;
    int width1 = displaymetrics.widthPixels;
    //paint object to define paint properties

    //loading bitmap from intent
    Bundle bundle = getIntent().getExtras();
    String imageBitmap = bundle.getString("resourceImage");
    String photoPath = Environment.getExternalStorageDirectory()+"/"+imageBitmap;
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inSampleSize = 8;
    originalBitmap = BitmapFactory.decodeFile(photoPath, options);
    iv_ttx.setImageBitmap(originalBitmap);

    //  originalBitmap = BitmapFactory.decodeResource(getResources(), activity.intent_file_image);
//scaling of bitmap
    originalBitmap =Bitmap.createScaledBitmap(originalBitmap, width1, height1, false);
//creating anoter copy of bitmap to be used for editing
    image = originalBitmap.copy(Bitmap.Config.RGB_565, true);
    et_sample =(EditText) findViewById(R.id.et_txt);
    Button btn_save_img = (Button) findViewById(R.id.btn_save_image);
    Button btn_clr_all = (Button) findViewById(R.id.btn_clr_all);
    //CLEAR_ALL BUTTON
    btn_clr_all.setOnClickListener(new OnClickListener() {

      @Override            public void onClick(View v) {
        // TODO Auto-generated method stub//loading original bitmap again (undoing all editing)
        image = originalBitmap.copy(Bitmap.Config.RGB_565, true);
        iv_ttx.setImageBitmap(image);            }
    });
    //SAVE BUTTON
    btn_save_img.setOnClickListener(new OnClickListener() {

      @Override            public void onClick(View arg0) {
        // TODO Auto-generated method stub//funtion save image is called with bitmap image as parameter
        saveImage(image);
      }
    });


    iv_ttx.setOnTouchListener(new OnTouchListener() {

      @Override
      public boolean onTouch(View arg0, MotionEvent arg1) {
        // TODO Auto-generated method stub
        shape_method = (Spinner) findViewById(R.id.spinner_shape);
        color_method = (Spinner) findViewById(R.id.spinner_color);
//            Toast.makeText(getApplicationContext(),
//                    "OnClickListener : " +
//            "\nSpinner_SHAPE : " + String.valueOf(shape_method.getSelectedItem()) +
//                    "\nSpinner_COLOR : " + String.valueOf(color_method.getSelectedItem()),   Toast.LENGTH_SHORT).show();

        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor(String.valueOf(color_method.getSelectedItem())));
        paint.setTextSize(25);



        String user_text = et_sample.getText().toString();
        //gettin x,y cordinates on screen touch
        float scr_x=arg1.getRawX();
        float scr_y=arg1.getRawY();
        //  canvas.onDrawPic(canvas,image, scr_x,scr_y);
        // funtion called to perform drawing
        createImage(scr_x,scr_y,user_text);
        return true;            }
    });
  }

  public String picChooser(){
    shape_method = (Spinner) findViewById(R.id.spinner_shape);
    return String.valueOf(shape_method.getSelectedItem());
  }




  void saveImage(Bitmap img) {
    String RootDir = Environment.getExternalStorageDirectory()
        + File.separator + "txt_imgs";
    File myDir=new File(RootDir);
    myDir.mkdirs();
//        Random generator = new Random();
//        int n = 10000;
//        n = generator.nextInt(n);
    Calendar c = Calendar.getInstance();
    Date date = new Date();
    String timeStamp = date.toString().trim() + c.getTime().toString().trim();
    String fname = "Image_"+ timeStamp +".jpg";
    File file = new File (myDir, fname);
    if (file.exists ()) file.delete ();        try {
      FileOutputStream out = new FileOutputStream(file);
      img.compress(Bitmap.CompressFormat.JPEG, 90, out);
      out.flush();
      out.close();
    } catch (Exception e) {
      e.printStackTrace();        }

    Toast.makeText(EditImageActivity.this, "Image saved to 'txt_imgs' folder",Toast.LENGTH_LONG).show();

  }



  public Bitmap createImage(float scr_x,float scr_y,String user_text){
    //canvas object with bitmap image as constructor
    Canvas canvas = new Canvas(image);
    int viewTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
    //removing title bar hight
    scr_y=scr_y- viewTop;//
    // fuction to draw text on image. you can try more drawing funtions like oval,point,rect,etc...
    canvas.drawText(""+user_text, scr_x, scr_y, paint);
    iv_ttx.setImageBitmap(image);
    return image;
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_edit_image, menu);
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
}
