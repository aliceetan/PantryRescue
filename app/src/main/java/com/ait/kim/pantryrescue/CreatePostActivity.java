package com.ait.kim.pantryrescue;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ait.kim.pantryrescue.data.Post;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CreatePostActivity extends AppCompatActivity {

    @BindView(R.id.etTitle)
    EditText etTitle;
    @BindView(R.id.etBody)
    EditText etBody;
    @BindView(R.id.btnAttachPic)
    Button btnAttachPic;
    @BindView(R.id.imgAttach)
    ImageView imgAttach;
    @BindView(R.id.ivWritePostPic)
    ImageView ivWritePostPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        ButterKnife.bind(this);

        btnAttachPic.setVisibility(View.GONE);


        if(getIntent().getSerializableExtra(RecipeDetailsActivity.RECIPE_TITLE) != null){
            initEdit();
        }
        String imageUrl = getIntent().getStringExtra(RecipeDetailsActivity.IMAGE_URL);
        Glide.with(CreatePostActivity.this).load(imageUrl).into(ivWritePostPic);

        requestNeededPermission();

    }

    private void initEdit() {
        String recipeTitle = getIntent().getStringExtra(RecipeDetailsActivity.RECIPE_TITLE);
        etTitle.setText(recipeTitle);
    }

    private void requestNeededPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.CAMERA)) {
                // Toast...
            }

            ActivityCompat.requestPermissions(this, new String[]{
                            android.Manifest.permission.CAMERA},
                    101);
        } else {
            // we have the permission
            btnAttachPic.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 101) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, R.string.jupeee,
                        Toast.LENGTH_SHORT).show();

                // we have the permission

                btnAttachPic.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(this, R.string.notjupeee,
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @OnClick(R.id.btnSend)
    void sendClick(){

        if (imgAttach.getVisibility() == View.GONE) {
            uploadPost();
        } else {
            try {
                uploadPostWithImage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadPost(String... imageUrl) {
        String key = FirebaseDatabase.getInstance().getReference().child("posts").push().getKey();
        Post newPost = new Post(
                FirebaseAuth.getInstance().getCurrentUser().getUid(),
                FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), etTitle.getText().toString(),
                etBody.getText().toString());

        if (imageUrl != null && imageUrl.length>0) {
            newPost.setImgUrl(imageUrl[0]);
        }

        // add value to proper element of the tree
        FirebaseDatabase.getInstance().getReference().child("posts").child(key).setValue(newPost).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(CreatePostActivity.this, R.string.postcreated, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }


    public void uploadPostWithImage() throws Exception {
        imgAttach.setDrawingCacheEnabled(true);
        imgAttach.buildDrawingCache();
        Bitmap bitmap = imgAttach.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageInBytes = baos.toByteArray();

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String newImage = URLEncoder.encode(UUID.randomUUID().toString(), "UTF-8")+".jpg";
        StorageReference newImageRef = storageRef.child(newImage);
        StorageReference newImageImagesRef = storageRef.child("images/"+newImage);
        newImageRef.getName().equals(newImageImagesRef.getName());    // true
        newImageRef.getPath().equals(newImageImagesRef.getPath());    // false

        UploadTask uploadTask = newImageImagesRef.putBytes(imageInBytes);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(CreatePostActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                uploadPost(taskSnapshot.getDownloadUrl().toString());
            }
        });
    }

    @OnClick(R.id.btnAttachPic)
    public void attachClicked() {
        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // expect to get back a result
        startActivityForResult(intentCamera, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 101 && resultCode == RESULT_OK){
            Bitmap img = (Bitmap) data.getExtras().get("data");
            imgAttach.setImageBitmap(img);
            imgAttach.setVisibility(View.VISIBLE);
        }
    }

}