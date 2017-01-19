package cn.ft.calorie.util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by TT on 2017/1/18.
 */
public class TakePictureUtils {
    public static File tempPicFile = new File(Utils.sysRootDir+"/卡路里/temp.jpg");//// TODO: 2017/1/18 文件位置
    public static Uri tempPicUri = Uri.fromFile(tempPicFile);
    public static final int REQUEST_PIC_FROM_CAMERA = 1;
    public static final int REQUEST_PIC_FROM_ALBUM = 2;
    public static final int REQUEST_PIC_AFTER_CROP = 3;
    //相机
    public static void takePhoto(Activity activity){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempPicUri);
        activity.startActivityForResult(intent, REQUEST_PIC_FROM_CAMERA);
    }
    //相册
    public static void takeAblum(Activity activity){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
//                      intent.addCategory(Intent.CATEGORY_OPENABLE);
//                      startActivityForResult(Intent.createChooser(intent, "选择图片"), Utils.REQUEST_PIC_FROM_ALBUM);
        activity.startActivityForResult(intent, REQUEST_PIC_FROM_ALBUM);
    }
    //处理
    public static Observable<File> handlerPicture(Activity activity, int requestCode, int resultCode, Intent data) {
        return Observable.create(new Observable.OnSubscribe<File>() {
            @Override
            public void call(Subscriber<? super File> subscriber) {
                if(resultCode == Activity.RESULT_OK) {
                    switch (requestCode) {
                        //相机
                        case REQUEST_PIC_FROM_CAMERA:
                            Intent intent = new Intent("com.android.camera.action.CROP");
                            intent.setDataAndType(tempPicUri,"image/*");
                            intent.putExtra("crop", "true");
                            // aspectX aspectY 是宽高的比例
                            intent.putExtra("aspectX", 1);
                            intent.putExtra("aspectY", 1);
                            // outputX outputY 是裁剪图片宽高
                            intent.putExtra("outputX",1000);
                            intent.putExtra("outputY",1000);
                            intent.putExtra("noFaceDetection", true);
                            activity.startActivityForResult(intent, REQUEST_PIC_AFTER_CROP);
                            break;
                        //相册
                        case REQUEST_PIC_FROM_ALBUM:
                            Uri uri =  data.getData();
                            try {
                                Bitmap picFromAlbum = MediaStore.Images.Media.getBitmap(activity.getContentResolver(),uri);
                                FileOutputStream fos = new FileOutputStream(tempPicFile);
                                picFromAlbum.compress(Bitmap.CompressFormat.JPEG,100,fos);
                                fos.close();

                                Intent intent2 = new Intent("com.android.camera.action.CROP");
                                intent2.setDataAndType(tempPicUri,"image/*");
                                intent2.putExtra("crop", "true");
                                // aspectX aspectY 是宽高的比例
                                intent2.putExtra("aspectX", 1);
                                intent2.putExtra("aspectY", 1);
                                // outputX outputY 是裁剪图片宽高
                                intent2.putExtra("outputX",1000);
                                intent2.putExtra("outputY",1000);
                                intent2.putExtra("noFaceDetection", true);
                                activity.startActivityForResult(intent2, REQUEST_PIC_AFTER_CROP);
                            } catch (Exception e) {
                                e.printStackTrace();
                                subscriber.onError(e);
                            }
                            break;
                        case REQUEST_PIC_AFTER_CROP:
                            try {
                                Bitmap picAfterCrop = MediaStore.Images.Media.getBitmap(activity.getContentResolver(),tempPicUri);
                                FileOutputStream fos = new FileOutputStream(tempPicFile);
                                picAfterCrop.compress(Bitmap.CompressFormat.JPEG,100,fos);
                                fos.close();
                                subscriber.onNext(tempPicFile);
                            } catch (IOException e) {
                                e.printStackTrace();
                                subscriber.onError(e);
                            }
                            break;
                    }
                }else
                    subscriber.onError(new RuntimeException("取消操作"));
            }
        });
    }
}
