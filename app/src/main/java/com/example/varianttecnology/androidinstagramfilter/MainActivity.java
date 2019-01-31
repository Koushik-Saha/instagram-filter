package com.example.varianttecnology.androidinstagramfilter;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.varianttecnology.androidinstagramfilter.Adapter.ViewPagerAdapter;
import com.example.varianttecnology.androidinstagramfilter.Interface.AddTextFragmentListener;
import com.example.varianttecnology.androidinstagramfilter.Interface.BrushFragmentListener;
import com.example.varianttecnology.androidinstagramfilter.Interface.EditImageFragmentListener;
import com.example.varianttecnology.androidinstagramfilter.Interface.EmojiFragmentListener;
import com.example.varianttecnology.androidinstagramfilter.Interface.FiltersListFragmentListener;
import com.example.varianttecnology.androidinstagramfilter.Utils.BitmapUtils;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter;

import java.io.IOException;
import java.util.List;

import ja.burhanrashid52.photoeditor.OnSaveBitmap;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;

public class MainActivity extends AppCompatActivity implements FiltersListFragmentListener,EditImageFragmentListener, BrushFragmentListener, EmojiFragmentListener, AddTextFragmentListener {

    public static final String pictureName = "flash.jpeg";
    public static final int PERMISSION_PACK_IMAGE = 1000;

    PhotoEditorView photoEditorView;
    PhotoEditor photoEditor;

    CoordinatorLayout coordinatorLayout;

    Bitmap originalBitmap,filteredBitmap,finalBitmap;

    FiltersListFragment filtersListFragment;
    EditImageFragment editImageFragment;


    CardView btn_filters_list,btn_edit,btn_brush,btn_emoji,btn_add_text;

    int brightrnessFinal = 0;
    float saturationFinal = 1.0f;
    float constrantFinal = 1.0f;


    //Load native image filters lip
    static {
        System.loadLibrary("NativeImageProcessor");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Image Filter");

        //View
        photoEditorView = (PhotoEditorView) findViewById(R.id.image_preview);
        photoEditor = new PhotoEditor.Builder(this,photoEditorView)
                .setPinchTextScalable(true)
                .setDefaultEmojiTypeface(Typeface.createFromAsset(getAssets(),"emojione-android.ttf"))
                .build();

        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinator);

        btn_edit = (CardView)findViewById(R.id.btn_edit);
        btn_filters_list = (CardView)findViewById(R.id.btn_filters_list);
        btn_brush = (CardView)findViewById(R.id.btn_brush);
        btn_emoji = (CardView)findViewById(R.id.btn_emoji);
        btn_add_text = (CardView)findViewById(R.id.btn_add_text);

        btn_filters_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FiltersListFragment filtersListFragment = FiltersListFragment.getInstance();
                filtersListFragment.setListener(MainActivity.this);
                filtersListFragment.show(getSupportFragmentManager(),filtersListFragment.getTag());
            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditImageFragment editImageFragment = EditImageFragment.getInstance();
                editImageFragment.setListener(MainActivity.this);
                editImageFragment.show(getSupportFragmentManager(),editImageFragment.getTag());
            }
        });

        btn_brush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Enable brush mode
                photoEditor.setBrushDrawingMode(true);

                BrushFragment brushFragment = BrushFragment.getInstance();
                brushFragment.setListener(MainActivity.this);
                brushFragment.show(getSupportFragmentManager(),brushFragment.getTag());
            }
        });

        btn_emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EmojiFragment emojiFragment = EmojiFragment.getInstance();
                emojiFragment.setListener(MainActivity.this);
                emojiFragment.show(getSupportFragmentManager(),emojiFragment.getTag());
            }
        });


        btn_add_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddTextFragment addTextFragment = AddTextFragment.getInstance();
                addTextFragment.setListener(MainActivity.this);
                addTextFragment.show(getSupportFragmentManager(),addTextFragment.getTag());
            }
        });




        loadImage();

    }

    private void loadImage() {
        originalBitmap = BitmapUtils.getBitmapFromAssets(this,pictureName,300,300);
        filteredBitmap = originalBitmap.copy(Bitmap.Config.ARGB_4444,true);
        finalBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888,true);
        photoEditorView.getSource().setImageBitmap(originalBitmap);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        filtersListFragment = new FiltersListFragment();
        filtersListFragment.setListener(this);

        editImageFragment = new EditImageFragment();
        editImageFragment.setListener(this);

        adapter.addFragment(filtersListFragment, "FILTERS");
        adapter.addFragment(editImageFragment,"EDIT");

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBrightnessChanged(int brightness) {
        brightrnessFinal = brightness;
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new BrightnessSubFilter(brightness));
        photoEditorView.getSource().setImageBitmap(myFilter.processFilter(finalBitmap.copy(Bitmap.Config.ARGB_8888,true)));

    }

    @Override
    public void onSaturationChanged(float saturation) {
        saturationFinal = saturation;
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new SaturationSubfilter(saturation));
        photoEditorView.getSource().setImageBitmap(myFilter.processFilter(finalBitmap.copy(Bitmap.Config.ARGB_8888,true)));

    }

    @Override
    public void onConstrantChanged(float constrant) {
        constrantFinal = constrant;
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new SaturationSubfilter(constrant));
        photoEditorView.getSource().setImageBitmap(myFilter.processFilter(finalBitmap.copy(Bitmap.Config.ARGB_8888,true)));
    }

    @Override
    public void onEditStarted() {

    }

    @Override
    public void onEditCompleted() {

        Bitmap bitmap = filteredBitmap.copy(Bitmap.Config.ARGB_8888,true);

        Filter myFilter = new Filter();
        myFilter.addSubFilter(new BrightnessSubFilter(brightrnessFinal));
        myFilter.addSubFilter(new SaturationSubfilter(saturationFinal));
        myFilter.addSubFilter(new ContrastSubFilter(constrantFinal));

        finalBitmap = myFilter.processFilter(bitmap);

    }

    @Override
    public void onFilterSelected(Filter filter) {
        resetControl();
        filteredBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888,true);
        photoEditorView.getSource().setImageBitmap(filter.processFilter(filteredBitmap));
        finalBitmap = filteredBitmap.copy(Bitmap.Config.ARGB_8888,true);


    }

    private void resetControl() {
        if (editImageFragment != null)
            editImageFragment.resetControls();
        brightrnessFinal = 0;
        saturationFinal = 1.0f;
        constrantFinal = 1.0f;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_open)
        {
            openImageFromGallery();
            return true;
        }
        if (id == R.id.action_save)
        {
            saveImageToGallery();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveImageToGallery() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted())
                        {

                            photoEditor.saveAsBitmap(new OnSaveBitmap() {
                                @Override
                                public void onBitmapReady(Bitmap saveBitmap) {
                                    try {

                                        photoEditorView.getSource().setImageBitmap(saveBitmap);

                                        final String path = BitmapUtils.insertImage(getContentResolver(),
                                                saveBitmap,
                                                System.currentTimeMillis() + "_profile.jpg"
                                                , null);

                                        if (!TextUtils.isEmpty(path))
                                        {
                                            Snackbar snackbar = Snackbar.make(coordinatorLayout,
                                                    "Image save to gallery!",
                                                    Snackbar.LENGTH_LONG)
                                                    .setAction("OPEN", new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            openImage(path);
                                                        }
                                                    });
                                            snackbar.show();
                                        }
                                        else
                                        {
                                            Snackbar snackbar = Snackbar.make(coordinatorLayout,
                                                    "Unable to save image",
                                                    Snackbar.LENGTH_LONG);
                                            snackbar.show();
                                        }
                                    } catch (IOException e){
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(Exception e) {

                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })

                .check();


    }

    private void openImage(String path) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(path),"image/*");
        startActivity(intent);
    }

    private void openImageFromGallery() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted())
                        {
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            startActivityForResult(intent,PERMISSION_PACK_IMAGE);
                        }
                        else

                        {
                            Toast.makeText(MainActivity.this, "Permission denied!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
        .check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == PERMISSION_PACK_IMAGE)
        {
            Bitmap bitmap = BitmapUtils.getBitmapFromGallery(this,data.getData(),800,800);


            //clear bitmap memory
            originalBitmap.recycle();
            finalBitmap.recycle();
            filteredBitmap.recycle();

            originalBitmap = bitmap.copy(Bitmap.Config.ARGB_8888,true);
            finalBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888,true);
            filteredBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888,true);
            photoEditorView.getSource().setImageBitmap(originalBitmap);
            bitmap.recycle();


            //Render selected img thumbnail
            filtersListFragment.displayThumbnail(originalBitmap);
        }
    }

    @Override
    public void onBrushSizeChangedListener(float size) {
        photoEditor.setBrushSize(size);
    }

    @Override
    public void onBrushOpacityChangedListener(int opacity) {
        photoEditor.setOpacity(opacity);
    }

    @Override
    public void onBrushColorChangedListener(int color) {
        photoEditor.setBrushColor(color);
    }

    @Override
    public void onBrushStateChangedListener(boolean isEraser) {
        if (isEraser)
            photoEditor.brushEraser();
        else
            photoEditor.setBrushDrawingMode(true);
    }

    @Override
    public void onEmojiSelected(String emoji) {
        photoEditor.addEmoji(emoji);
    }

    @Override
    public void onAddTextButtonClick(String text, int color) {
        photoEditor.addText(text,color);
    }
}
