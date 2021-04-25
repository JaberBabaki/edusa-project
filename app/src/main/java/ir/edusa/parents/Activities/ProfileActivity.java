package ir.edusa.parents.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.squareup.picasso.Picasso;
import com.wooplr.spotlight.SpotlightView;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import fontdroid.EditText;
import fontdroid.TextView;
import ir.edusa.parents.Dialogs.ConfirmDialog;
import ir.edusa.parents.Dialogs.ErrorDialog;
import ir.edusa.parents.Helpers.HashHelper;
import ir.edusa.parents.Helpers.IntentHelper;
import ir.edusa.parents.Helpers.Toast;
import ir.edusa.parents.Helpers.ValidatorHelper;
import ir.edusa.parents.Managers.UserManager;
import ir.edusa.parents.Models.Passenger;
import ir.edusa.parents.Models.SchoolService;
import ir.edusa.parents.MyApplication;
import ir.edusa.parents.Network.ApiCallerClass;
import ir.edusa.parents.Network.Requests.PassengerInfoRequest;
import ir.edusa.parents.Network.Requests.UploadImageRequest;
import ir.edusa.parents.Network.Responses.AbstractResponse;
import ir.edusa.parents.Network.Responses.UserInfoResponse;
import ir.edusa.parents.R;
import ir.edusa.parents.Utils.Log;
import ir.edusa.parents.Utils.SquareImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class ProfileActivity extends BaseActivity {

    private static final MediaType MEDIA_TYPE_JPEG = MediaType.parse("image/jpeg");

    private EditText edtName;
    private EditText edtLastName;
    private EditText codeStudent;
    private EditText edtOrganizationName;
    private EditText edtAddress;
    private TextView txtTitle;
    private ViewGroup lytUpload;
    private ViewGroup lytLogout;
    private ViewGroup lytSetLocation;
    private ViewGroup lytDriversParent;
    private SquareImageView image;
    private LayoutInflater inflater;
    private ScrollView scrollView;
    private boolean hiddenMenu = false;
    private int hiddenMenuCount = 0;


    @Override
    protected String getPageTitle() {
        return "حساب کاربری";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        scrollView =(ScrollView) findViewById(R.id.scrollView);
        edtName =(EditText) findViewById(R.id.edtName);
        edtLastName =(EditText) findViewById(R.id.edtLastName);
        codeStudent =(EditText) findViewById(R.id.edtCodeStudent);
        edtOrganizationName =(EditText) findViewById(R.id.edtOrganizationName);
        edtAddress =(EditText) findViewById(R.id.edtAddress);
        lytSetLocation =(ViewGroup) findViewById(R.id.lytSetLocation);
        image =(SquareImageView) findViewById(R.id.image);
        lytDriversParent =(ViewGroup) findViewById(R.id.lytDriversParent);
        lytUpload =(ViewGroup) findViewById(R.id.lytUpload);
        txtTitle =(TextView) findViewById(R.id.txtTitle);

        new SpotlightView.Builder(this)
          .introAnimationDuration(400)
          .enableRevealAnimation(true)
          .performClick(true)
          .fadeinTextDuration(400)
          .headingTvColor(Color.parseColor("#eb273f"))
          .headingTvSize(15)
          .headingTvText("لطفا موقعیت مکانی فرزندتان را مشخص کنید")
          .subHeadingTvColor(Color.parseColor("#ffffff"))
          .subHeadingTvSize(16)
          .subHeadingTvText(" ")
          .maskColor(Color.parseColor("#dc000000"))
          .target(lytSetLocation)
          .setTypeface(BaseActivity.font1)
          .lineAnimDuration(400)
          .lineAndArcColor(Color.parseColor("#eb273f"))
          .dismissOnTouch(true)
          .dismissOnBackPress(true)
          .enableDismissAfterShown(true)
          .usageId("b5") //UNIQUE ID
          .show();


        txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!hiddenMenu) {
                    hiddenMenu = true;
                    hiddenMenuCount = 0;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            hiddenMenu = false;
                            hiddenMenuCount = 0;
                        }
                    }, 1000);
                } else {
                    hiddenMenuCount++;
                    if (hiddenMenuCount == 7) {

                        String dialogText = "App Version Name= " + MyApplication.VersionName +
                                "\n" + "App Version Code = " + MyApplication.VersionCode +
                                "\n" + "Device_Code = " + UserManager.getDeviceCode() +
                                "\n" + "Passenger_code = " + UserManager.getPassenger().getCode() +
                                "\n" + "UDID = " + MyApplication.UDID +
                                "\n" + "Model= " + MyApplication.Model;

                        ErrorDialog errorDialog = new ErrorDialog(ProfileActivity.this, dialogText);
                        errorDialog.show();
                    }
                }
            }
        });

        inflater = getLayoutInflater();
        lytLogout =(ViewGroup) findViewById(R.id.lytLogout);
        lytLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmDialog confirmDialog = new ConfirmDialog(ProfileActivity.this, "بله", "خیر",
                        " آیا اطمینان دارید که می خواهید از حساب کاربری خود خارج شوید؟",
                        new ConfirmDialog.Function() {
                            @Override
                            public void onYes() {
                                UserManager.setCellphone("");
                                UserManager.setDeviceCode("");
                                UserManager.setPassenger(new Passenger());
                                new Passenger().unsubscribeFromTopics();
                                UserManager.setToken("");

                                IntentHelper.startActivityAndFinishThisByClearTop(ProfileActivity.this, LoginActivity.class);
                                ActivityCompat.finishAffinity(ProfileActivity.this);

                            }

                            @Override
                            public void onNo() {

                            }
                        });
                confirmDialog.show();
            }
        });
        getUserInfo();
    }


    public void getUserInfo() {
        final String deviceCode = UserManager.getDeviceCode();
        ApiCallerClass.with(this).hasLoading(true).call(new PassengerInfoRequest(deviceCode,
                        HashHelper.Calculate_Password(UserManager.getPrivateApiKey(), UserManager.getToken()))
                , new ApiCallerClass.CallerOnResponse() {
                    @Override
                    public void onResponse(AbstractResponse result, JSONObject rawResponse) {
                        final UserInfoResponse userInfoResponse = (UserInfoResponse) result;
                        edtName.setText(userInfoResponse.getData().getName());
                        edtLastName.setText(userInfoResponse.getData().getFamily());
                        codeStudent.setText(userInfoResponse.getData().getCode());

                        edtOrganizationName.setText(userInfoResponse.getData().getOrganization().getDescript()
                                +" کلاس "+ userInfoResponse.getData().getClassCode());
                        edtAddress.setText(userInfoResponse.getData().getAddress());
                        for (final SchoolService service : userInfoResponse.getData().getServices()) {
                            View view = inflater.inflate(R.layout.item_driver_info, null);
                            EditText edtDriverCellPhoneNumber =(EditText) view.findViewById(R.id.edtDriverCellPhoneNumber);
                            EditText edtDriverName =(EditText) view.findViewById(R.id.edtDriverName);
                            EditText edtDriverLastName =(EditText) view.findViewById(R.id.edtDriverLastName);
                            ViewGroup lytDriverPhone =(ViewGroup) view.findViewById(R.id.lytDriverPhone);
                            SquareImageView image =(SquareImageView) view.findViewById(R.id.image);
                            if (ValidatorHelper.isValidString(service.getDriver_Image_Url())) {
                                Picasso.with(ProfileActivity.this).load(service.getDriver_Image_Url()).into(image);
                            }
                            edtDriverName.setText(service.getDriver_Name());
                            edtDriverLastName.setText(service.getDriver_Family());
                            edtDriverCellPhoneNumber.setText(service.getDriver_Mobile_Number());
                            lytDriverPhone.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    IntentHelper.call(ProfileActivity.this, service.getDriver_Mobile_Number());
                                }
                            });
                            lytDriversParent.addView(view);
                        }

                        lytSetLocation.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                IntentHelper.startActivity(ProfileActivity.this, SelectLocationActivity.class);
                            }
                        });
                        if (ValidatorHelper.isValidString(userInfoResponse.getData().getImage_Url())) {
                            Picasso.with(ProfileActivity.this).load(userInfoResponse.getData().getImage_Url()).into(image);
                        }
                        lytUpload.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                EasyImage.openChooserWithGallery(ProfileActivity.this, "انتخاب عکس", 0);
                            }
                        });

                        /*scrollView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                            }
                        }, 700);*/
                    }

                    @Override
                    public void onError(String message, int code) {
                        ErrorDialog errorDialog = new ErrorDialog(ProfileActivity.this, message);
                        errorDialog.show();
                        errorDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                finishWithAnimation();
                            }
                        });
                    }
                }, "");
    }

    public void uploadImage(String password, String device_Code, String device_Type, String device_Owner_Code, String image_File) {
        UploadImageRequest uploadImageRequest = new UploadImageRequest(password, device_Code, device_Type, device_Owner_Code);

        String url = uploadImageRequest.getApiUrl() + Uri.encode("?" + uploadImageRequest.getParametersJsonFormat(), "@#&=*+-_.,:!?()/~'%");

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("Image_File", "image_File" + device_Owner_Code + ".jpeg",
                        RequestBody.create(MEDIA_TYPE_JPEG, new File(image_File)))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .method("POST", requestBody).build();
        ApiCallerClass.with(this).hasLoading(true).call(request, new ApiCallerClass.CallerOnResponse() {
            @Override
            public void onResponse(AbstractResponse result, JSONObject rawResponse) {

            }

            @Override
            public void onError(String message, int code) {
                ErrorDialog errorDialog = new ErrorDialog(ProfileActivity.this, message);
                errorDialog.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                Toast.makeText(ProfileActivity.this, "خطا در دریافت تصویر", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                uploadImage(HashHelper.Calculate_Password(UserManager.getPrivateApiKey(), UserManager.getToken()),
                        UserManager.getDeviceCode(), "1", UserManager.getPassenger().getCode(),
                        resizeAndCompressImageBeforeSend(ProfileActivity.this, imageFile.getAbsolutePath(), "temp_name"));
                Picasso.with(ProfileActivity.this).load(imageFile).into(image);

            }
        });
    }

    public static String resizeAndCompressImageBeforeSend(Context context, String filePath, String fileName) {
        final int MAX_IMAGE_SIZE = 150 * 1024; // max final file size in kilobytes

        // First decode with inJustDecodeBounds=true to check dimensions of image
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize(First we are going to resize the image to 800x800 image, in order to not have a big but very low quality image.
        //resizing the image will already reduce the file size, but after resizing we will check the file size and start to compress image
        options.inSampleSize = calculateInSampleSize(options, 720, 720);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        Bitmap bmpPic = BitmapFactory.decodeFile(filePath, options);


        int compressQuality = 100; // quality decreasing by 5 every loop.
        int streamLength;
        do {
            ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
            Log.e("compressBitmap", "Quality: " + compressQuality);
            bmpPic.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream);
            byte[] bmpPicByteArray = bmpStream.toByteArray();
            streamLength = bmpPicByteArray.length;
            compressQuality -= 5;
            Log.d("compressBitmap", "Size: " + streamLength / 1024 + " kb");
        } while (streamLength >= MAX_IMAGE_SIZE);

        try {
            //save the resized and compressed file to disk cache
            Log.d("compressBitmap", "cacheDir: " + context.getCacheDir());
            FileOutputStream bmpFile = new FileOutputStream(context.getCacheDir() + fileName);
            bmpPic.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpFile);
            bmpFile.flush();
            bmpFile.close();
        } catch (Exception e) {
            Log.e("compressBitmap", "Error on saving file");
        }
        //return the path of resized and compressed file
        return context.getCacheDir() + fileName;
    }


    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        String debugTag = "MemoryInformation";
        // Image nin islenmeden onceki genislik ve yuksekligi
        final int height = options.outHeight;
        final int width = options.outWidth;
        Log.e(debugTag, "image height: " + height + "---image width: " + width);
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        Log.d(debugTag, "inSampleSize: " + inSampleSize);
        return inSampleSize;
    }
}
