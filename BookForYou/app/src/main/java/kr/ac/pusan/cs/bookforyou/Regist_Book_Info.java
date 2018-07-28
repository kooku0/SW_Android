package kr.ac.pusan.cs.bookforyou;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;

public class Regist_Book_Info extends AppCompatActivity {
    private String userID = "002";
    FirebaseDatabase database;
    DatabaseReference mRef;
    FirebaseStorage storage;
    Obj_book objbook;
    Bitmap bookImage;
    ImageView image;
    TextView title;
    TextView author;
    TextView price;
    TextView publisher;
    TextView pubdate;
    EditText description;
    Loading_Application loading;
    private static final int REQUEST_IMAGE_CAPTURE1 = 11;
    private static final int REQUEST_IMAGE_CAPTURE2 = 12;
    private static final int REQUEST_IMAGE_CAPTURE3 = 13;
    private static final int GALLERY_CODE1 = 21;
    private static final int GALLERY_CODE2 = 22;
    private static final int GALLERY_CODE3 = 23;
    private StorageReference folderRef, imageRef;
    private UploadTask mUploadTask;
    private String imageFilePath;

    private Uri photoUri;

    private ImageView image1;
    private ImageView image2;
    private ImageView image3;

    ArrayList uriList;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist__book__info);
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference();
        storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        uriList = new ArrayList(3);
        /*권한*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
        }

        Intent intent = getIntent();
        objbook = (Obj_book) intent.getSerializableExtra("book");
        //bookImage = intent.getParcelableExtra("image");
        image = findViewById(R.id.image);
        title = findViewById(R.id.title);
        author = findViewById(R.id.author);
        price = findViewById(R.id.price);
        publisher = findViewById(R.id.publisher);
        pubdate = findViewById(R.id.pubdate);
        description = findViewById(R.id.description);
        title.setText(objbook.title);
        author.setText(objbook.author);
        price.setText(objbook.price);
        publisher.setText(objbook.publisher);
        pubdate.setText(objbook.pubdate);

        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);

        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show(1);
            }
        });
        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show(2);
            }
        });
        findViewById(R.id.btn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show(3);
            }
        });
        findViewById(R.id.rstbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(uriList.size()!=3) Toast.makeText(getApplicationContext(),"사진 3장을 등록하세요.", Toast.LENGTH_LONG).show();
                else{
                    startProgress();
                    fileUpload();

                }
            }
        });
    }

    void show(final int imageNum) {
        DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //sendTakePhotoIntent(imageNum);
            }
        };
        DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loadImagefromGallery(imageNum);
            }
        };
        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };
        new AlertDialog.Builder(this).setTitle("업로드할 이미지 선택")
                .setNegativeButton("사진촬영", cameraListener)
                .setPositiveButton("앨범선택", albumListener)
                .setNeutralButton("취소", cancelListener)
                .show();
    }

    /*private void sendTakePhotoIntent(int imageNum) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }

            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(this, getPackageName(), photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                if(imageNum == 1) startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE1);
                if(imageNum == 2) startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE2);
                if(imageNum == 3) startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE3);
            }
        }
    }*/

/*    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "TEST_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,      *//* prefix *//*
                ".jpg",         *//* suffix *//*
                storageDir          *//* directory *//*
        );
        imageFilePath = image.getAbsolutePath();
        return image;
    }*/
/*    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }*/

/*
    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
*/

    public void loadImagefromGallery(int imageNum) {
        //Intent 생성
        Intent intent = new Intent(Intent.ACTION_PICK); //ACTION_PIC과 차이점?
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);

        if(imageNum==1)startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_CODE1);
        if(imageNum==2)startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_CODE2);
        if(imageNum==3)startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_CODE3);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode/10 == 1) {

            /*Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);
            ExifInterface exif = null;

            try {
                exif = new ExifInterface(imageFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int exifOrientation;
            int exifDegree;

            if (exif != null) {
                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegree = exifOrientationToDegrees(exifOrientation);
            } else {
                exifDegree = 0;
            }
            if(requestCode == REQUEST_IMAGE_CAPTURE1) ((ImageView)findViewById(R.id.image1)).setImageBitmap(rotate(bitmap, exifDegree));
            if(requestCode == REQUEST_IMAGE_CAPTURE2) ((ImageView)findViewById(R.id.image2)).setImageBitmap(rotate(bitmap, exifDegree));
            if(requestCode == REQUEST_IMAGE_CAPTURE3) ((ImageView)findViewById(R.id.image3)).setImageBitmap(rotate(bitmap, exifDegree));*/
        }
        else if (resultCode == RESULT_OK && requestCode/10 == 2){

            super.onActivityResult(requestCode, resultCode, data);
            try {
                //이미지를 하나 골랐을때
                if (resultCode == RESULT_OK && null != data) {
                    //data에서 절대경로로 이미지를 가져옴
                    String uri = getPath(data.getData());
                    uriList.add(uri);
                    //Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),data.getData());
                    //이미지가 한계이상(?) 크면 불러 오지 못하므로 사이즈를 줄여 준다.
                    //int nh = (int) (bitmap.getHeight() * (1024.0 / bitmap.getWidth()));
                    //Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 1024, nh, true);
                    if(requestCode == GALLERY_CODE1){
                        //((ImageView) findViewById(R.id.image1)).setImageBitmap(scaled);
                        image1.setImageURI(Uri.fromFile(new File(uri)));
                    }
                    if(requestCode == GALLERY_CODE2) {
                        //((ImageView) findViewById(R.id.image2)).setImageBitmap(scaled);
                        image2.setImageURI(Uri.fromFile(new File(uri)));
                    }
                    if(requestCode == GALLERY_CODE3) {
                        //((ImageView) findViewById(R.id.image3)).setImageBitmap(scaled);
                        image3.setImageURI(Uri.fromFile(new File(uri)));
                    }


                } else {
                    Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                Toast.makeText(this, "Oops! 로딩에 오류가 있습니다.", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }
    public String getPath(Uri uri){

        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this,uri,proj,null,null,null);

        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(index);
    }

    public void fileUpload() {

        final Obj_book_info registObj = new Obj_book_info();
        registObj.init();

        registObj.book = objbook;
        registObj.userID = userID;
        registObj.description = description.getText().toString();
        final StorageReference storageRef = storage.getReference();
        final ArrayList downloadUriList = new ArrayList();
        for (int i = 0; i < 3; i++) {
            Uri file = Uri.fromFile(new File((String) uriList.get(i)));
            StorageReference riversRef = storageRef.child("book_images/" + userID + "/" + objbook.title + "/" + file.getLastPathSegment());
            UploadTask uploadTask = riversRef.putFile(file);


            final String[] stringuri = new String[1];
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VIsibleForTests")
                    Uri downloadUri = taskSnapshot.getDownloadUrl();
                    System.out.println("uri: "+downloadUri.toString());
                    downloadUriList.add(downloadUri.toString());
                    if(downloadUriList.size()==3) {
                        registObj.imageURI.imageUri1 = downloadUriList.get(0).toString();
                        registObj.imageURI.imageUri2 = downloadUriList.get(1).toString();
                        registObj.imageURI.imageUri3 = downloadUriList.get(2).toString();

                        mRef.child("Book").child(userID).push().setValue(registObj);
                        Toast.makeText(getApplicationContext(),"Success", Toast.LENGTH_LONG).show();
                        loading.progressOFF();
                        finish();
                    }
                }

            });

        }
    }
    private void startProgress() {
        loading = new Loading_Application();
        loading.progressON(this,"Uploading...");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //loading.progressOFF();
            }
        }, 3500);

    }
}