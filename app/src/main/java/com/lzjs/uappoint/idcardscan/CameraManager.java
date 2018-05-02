package com.lzjs.uappoint.idcardscan;

import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;

/**
 * User:lizhangqu(513163535@qq.com)
 * Date:2015-09-05
 * Time: 10:56
 */
public class CameraManager {
    private static final String TAG = CameraManager.class.getName();
    private Camera camera;
    private Camera.Parameters parameters;
    private AutoFocusManager autoFocusManager;
    private int requestedCameraId = -1;

    private boolean initialized;
    private boolean previewing;

    /**
     * 打开摄像头
     *
     * @param cameraId 摄像头id
     * @return Camera
     */
    @SuppressWarnings("deprecation")
    public Camera open(int cameraId) {
        int numCameras = Camera.getNumberOfCameras();
        if (numCameras == 0) {
            Log.e(TAG, "No cameras!");
            return null;
        }
        boolean explicitRequest = cameraId >= 0;
        if (!explicitRequest) {
            // Select a camera if no explicit camera requested
            int index = 0;
            while (index < numCameras) {
                Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
                Camera.getCameraInfo(index, cameraInfo);
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    break;
                }
                index++;
            }
            cameraId = index;
        }
        Camera camera;
        if (cameraId < numCameras) {
            Log.e(TAG, "Opening camera #" + cameraId);
            camera = Camera.open(cameraId);
        } else {
            if (explicitRequest) {
                Log.e(TAG, "Requested camera does not exist: " + cameraId);
                camera = null;
            } else {
                Log.e(TAG, "No camera facing back; returning camera #0");
                camera = Camera.open(0);
            }
        }
        return camera;
    }

    /**
     * 打开camera
     *
     * @param holder SurfaceHolder
     * @throws IOException IOException
     */
    public synchronized void openDriver(SurfaceHolder holder)
            throws IOException {
        Log.e(TAG, "openDriver");

        if (camera == null) {
            camera = open(requestedCameraId);
            if (camera == null) {
                throw new IOException();
            }
        }
        camera.setPreviewDisplay(holder);
        camera.setDisplayOrientation(90);
        if (!initialized) {
            initialized = true;
            parameters = camera.getParameters();
//            parameters.setPreviewSize(800, 600);
            parameters.setPictureFormat(ImageFormat.JPEG);
//            parameters.setJpegQuality(100);
//            parameters.setPictureSize(1024, 768);
            camera.setParameters(parameters);
        }
    }

    /**
     * camera是否打开
     *
     * @return camera是否打开
     */
    public synchronized boolean isOpen() {
        return camera != null;
    }

    /**
     * 关闭camera
     */
    public synchronized void closeDriver() {
        Log.e(TAG, "closeDriver");
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    /**
     * 开始预览
     */
    public synchronized void startPreview() {
        Log.e(TAG, "startPreview");
        Camera theCamera = camera;
        if (theCamera != null && !previewing) {
            theCamera.startPreview();
            previewing = true;
            autoFocusManager = new AutoFocusManager(camera);
        }
    }
    public synchronized void takeGo(){
        camera.startPreview();
    }

    /**
     * 关闭预览
     */
    public synchronized void stopPreview() {
        Log.e(TAG, "stopPreview");
        if (autoFocusManager != null) {
            autoFocusManager.stop();
            autoFocusManager = null;
        }
        if (camera != null && previewing) {
            camera.stopPreview();
            previewing = false;
        }
    }

    /**
     * 打开闪光灯
     */
    public synchronized void openLight() {
        Log.e(TAG, "openLight");
        if (camera != null) {
            parameters = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(parameters);
        }
    }

    /**
     * 关闭闪光灯
     */
    public synchronized void offLight() {
        Log.e(TAG, "offLight");
        if (camera != null) {
            parameters = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(parameters);
        }
    }

    /**
     * 拍照
     *
     * @param shutter ShutterCallback
     * @param raw     PictureCallback
     * @param jpeg    PictureCallback
     */
    public synchronized void takePicture(final Camera.ShutterCallback shutter, final Camera.PictureCallback raw,
                                         final Camera.PictureCallback jpeg) {

        camera.takePicture(shutter, raw, jpeg);


    }

    /*private int FindFrontCamera(){
        int cameraCount = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras(); // get cameras number

        for ( int camIdx = 0; camIdx < cameraCount;camIdx++ ) {
            Camera.getCameraInfo( camIdx, cameraInfo ); // get camerainfo
            if ( cameraInfo.facing ==Camera.CameraInfo.CAMERA_FACING_FRONT ) {
                // 代表摄像头的方位，目前有定义值两个分别为CAMERA_FACING_FRONT前置和CAMERA_FACING_BACK后置
                return camIdx;
            }
        }
        return -1;
    }

    private int FindBackCamera(){
        int cameraCount = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras(); // get cameras number

        for ( int camIdx = 0; camIdx < cameraCount;camIdx++ ) {
            Camera.getCameraInfo( camIdx, cameraInfo ); // get camerainfo
            if ( cameraInfo.facing ==Camera.CameraInfo.CAMERA_FACING_BACK ) {
                // 代表摄像头的方位，目前有定义值两个分别为CAMERA_FACING_FRONT前置和CAMERA_FACING_BACK后置
                return camIdx;
            }
        }
        return -1;
    }
    public void surfaceCreated(SurfaceHolder holder) {
        int CammeraIndex=FindBackCamera();
        if(CammeraIndex==-1){
            CammeraIndex=FindFrontCamera();
        }
        mCamera = Camera.open(CammeraIndex);
    }*/
}
