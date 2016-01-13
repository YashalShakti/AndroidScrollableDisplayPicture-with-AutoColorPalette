package yashalshakti.designdemo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Context context =this;


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText editText = (EditText)findViewById(R.id.url);


        Button mainButton = (Button) findViewById(R.id.mainButton);
        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Changing image", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


                final ImageView imageView = (ImageView) findViewById(R.id.mainImage);

                //Download image using Glide and extract color palette from it using its bitmap
                Glide.with(context)
                        .load(editText.getText().toString())
                        .asBitmap()
                        .into(new BitmapImageViewTarget(imageView) {
                            @Override
                            public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                                super.onResourceReady(bitmap, anim);


                                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                                    public void onGenerated(Palette p) {

                                        // Use generated palette
                                        findViewById(R.id.mainButton).setBackgroundColor(p.getDarkVibrantColor(0));
                                        findViewById(R.id.mainButton2).setBackgroundColor(p.getDarkMutedColor(0));
                                        findViewById(R.id.mainButton3).setBackgroundColor(p.getLightMutedColor(0));

                                        //Change status bar color if on version > 21
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                            Window window = getWindow();
                                            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                            window.setStatusBarColor(p.getDarkMutedColor(0));
                                        }

                                    }
                                });
                            }
                        });
            }
        });


        //Setup some temp listeners for quick demo
        findViewById(R.id.mainButton2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //https://7-themes.com/data_images/out/14/6814492-venice.jpg
                editText.setText("https://cdn.photographylife.com/wp-content/uploads/2014/06/Nikon-D810-Image-Sample-6.jpg");
            }
        });

        findViewById(R.id.mainButton3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("http://memolition.com/wp-content/uploads/2015/10/beautiful-red-fall-65596-960x540.jpg");
            }
        });


        //Set the title that appears on the image.
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Design");

        //Set the height of the appBarLayout based on the screen density; Leaving 75 dip at the bottom for button.
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        ViewGroup.LayoutParams params = appBarLayout.getLayoutParams();
        int height = getWindowHeight(this)-dipToPixel(this, 75);
        params.height = height;

        //Hack toolbar height to set collapsed image height.
        toolbar.getLayoutParams().height = (int)(height/2);

        //Generate color palette from bitmap for default demo photo locally available
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
        if (bitmap != null && !bitmap.isRecycled()) {

            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                public void onGenerated(Palette p) {
                    // Use generated palette
                    findViewById(R.id.mainButton).setBackgroundColor(p.getDarkVibrantColor(0));
                    findViewById(R.id.mainButton2).setBackgroundColor(p.getDarkMutedColor(0));
                    findViewById(R.id.mainButton3).setBackgroundColor(p.getLightMutedColor(0));

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Window window = getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(p.getDarkMutedColor(0));
                    }

                }
            });

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


    public static int dipToPixel(Context context, int dip){

        return (int) (dip * context.getResources().getDisplayMetrics().density + 0.5f);
    }


    public static int getWindowHeight(Activity activity){

        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }

}
