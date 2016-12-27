package net.karlitos.supersimpleimageviewer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;

import org.apache.commons.io.FilenameUtils;

public class ImageViewerMainActivity extends Activity implements OnClickListener {

    final String CameraFolderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + "/Camera";

    private ArrayList<String> imagePathList;
    private int image_index = 0;
    private int MAX_IMAGE_COUNT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer_main);

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_VIEW.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                Uri imageUri =  intent.getData();
                if (imageUri != null) {

                    String imageFullPath = imageUri.getPath();
                    String imageFlename = FilenameUtils.getName(imageFullPath);
                    String imageDirectory = FilenameUtils.getPath(imageFullPath);
                    fillFileList("/" + imageDirectory);
                    image_index = this.imagePathList.indexOf(imageFullPath);
                }
            }
        }
        else {
            fillFileList(CameraFolderPath);
        }

        Button btnPrevious = (Button)findViewById(R.id.previous_btn);
        btnPrevious.setOnClickListener(this);
        Button btnNext = (Button)findViewById(R.id.next_btn);
        btnNext.setOnClickListener(this);

        showImage();
    }

    private void fillFileList (String path){
        File sourceFolder = new File(path);

        if (sourceFolder.isDirectory())
        {
            File[] fileList = sourceFolder.listFiles();
            this.imagePathList = new ArrayList<>();

            for (int i = 0; i < fileList.length; i++)
            {
                String fileExtension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(fileList[i]).toString());
                String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
                //String mimeType = URLConnection.guessContentTypeFromName(fileList[i].getAbsolutePath());
                if (mimeType != null && mimeType.startsWith("image")) { // .contains("image/")
                    this.imagePathList.add(fileList[i].getAbsolutePath());
                    this.MAX_IMAGE_COUNT ++;
                }
            }
        }
    }

    private void showImage() {
        ImageView imgView = (ImageView) findViewById(R.id.image_view);
        imgView.setImageBitmap(BitmapFactory.decodeFile(this.imagePathList.get(image_index)));

    }

    public void onClick(View v) {

        switch (v.getId()) {

            case (R.id.previous_btn):

                image_index--;

                if (image_index == -1) {
                    image_index = MAX_IMAGE_COUNT - 1;
                }

                showImage();

                break;

            case (R.id.next_btn):

                image_index++;

                if (image_index == MAX_IMAGE_COUNT) {
                    image_index = 0;
                }

                showImage();

                break;

        }

    }
}
