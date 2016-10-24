package com.xcode.lockcapture.fragment;


import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.xcode.lockcapture.MainActivity;
import com.xcode.lockcapture.R;
import com.xcode.lockcapture.capture.CameraPreview;
import com.xcode.lockcapture.capture.ICaptureTakenEvent;
import com.xcode.lockcapture.capture.SavePictureTask;
import com.xcode.lockcapture.common.GlobalConfig;
import com.xcode.lockcapture.common.IFragment;
import com.xcode.lockcapture.common.Utils;
import com.xcode.lockcapture.media.BGMusicService;
import com.xcode.lockcapture.observer.VolumeChangedObserver;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CaptureStatus extends Fragment implements ICaptureTakenEvent, IFragment {
    Camera _camera;
    boolean _isReadToGo = false;

    FrameLayout _previewContainer;
    CameraPreview _cameraPreview;
    MainActivity mMainActivity;
    int _front_camera_index = -1;
    int _back_camera_index = -1;
    int _currentCameraIndex = -1;
    ObjectAnimator _colorAnimation;

    TextView _tvStatus;
    Switch _shChangeStatus;
    Switch _shUseFrontCamera;


    FrameLayout _statusContainer;
    VolumeChangedObserver _volumeChanged;
    Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            new SavePictureTask(new SavePictureTask.SavePicListener() {

                @Override
                public void saveFinish() {
                    //这句是最重要的,必须执行startPreview后,才能再次拍照
                    _camera.startPreview();
                    _isReadToGo = true;
                }
            }).execute(data);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainActivity = (MainActivity) getActivity();
        _volumeChanged = new VolumeChangedObserver(new Handler(), CaptureStatus.this); // ==============> this 即为 ICaptureTakenEvent接口对象(匿名类 or implements方式实现了)
        GlobalConfig.RawImageStoreUrl = Utils.getWritePath(getActivity()) + "/imgs/";
        initCamera();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_capture_status, container, false);
        initControl(view);
        readyToGo();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        relax();  // ============= relax 释放
    }

    public void GoWithAnimate() {
        _tvStatus.setText(R.string.capture_status_on);
        _shUseFrontCamera.setEnabled(true);
        _colorAnimation.reverse();
        readyToGo();  // =============readyToGo
        Toast.makeText(mMainActivity, R.string.alert_capture_ready, Toast.LENGTH_SHORT).show();//服务已经开启，使用音量键加或减拍照吧
    }

    public void RelaxWithAnimate() {
        _tvStatus.setText(R.string.capture_status_off);
        _shUseFrontCamera.setEnabled(false);
        _colorAnimation.start();
        relax();  // ============= relax 释放
        Toast.makeText(mMainActivity, R.string.alert_capture_off, Toast.LENGTH_SHORT).show(); //服务已经关闭，现在系统可以使用拍照程序了
    }

    private void readyToGo() {
        if (_isReadToGo)
            return;

        if (_camera == null) {
            try {
                _camera = Camera.open(_currentCameraIndex);
            } catch (Exception e) {
                Toast.makeText(mMainActivity, "摄像头被其他程序占用，请关闭之后，再启动本程序", Toast.LENGTH_SHORT).show();
                _isReadToGo = false;
                return;
            }
        }

        setCameraParams(); //
        _cameraPreview = new CameraPreview(getActivity(), _camera);
        _previewContainer.addView(_cameraPreview); // ============ >//宽度和高度 设置成1dp，所以看不见 预览
        mMainActivity.getContentResolver().registerContentObserver(android.provider.Settings.System.CONTENT_URI, true, _volumeChanged);
        mMainActivity.startService(new Intent(getActivity(), BGMusicService.class)); // 播放背景音乐

        _isReadToGo = true;
    }

    private void setCameraParams() {
        Camera.Parameters parameters = _camera.getParameters();

        int cameraPictureRotation;

        if (_currentCameraIndex == _back_camera_index) {
            cameraPictureRotation = 90; // 后摄像头

            //set preview to right orientation
            // _camera.setDisplayOrientation(90);
        } else {
            cameraPictureRotation = 270;  // 前摄像头
        }

        List<String> focusModesList = parameters.getSupportedFocusModes();

        //增加对聚焦模式的判断
        if (focusModesList.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        } else if (focusModesList.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        } else if (focusModesList.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }

        parameters.setRotation(cameraPictureRotation);
        _camera.setParameters(parameters);
    }

    private void relax() {

        if (_isReadToGo == false)
            return;

        if (_camera != null) {
            _camera.stopPreview();
            _camera.release();
            _camera = null;
        }

        _previewContainer.removeView(_cameraPreview);
        mMainActivity.getContentResolver().unregisterContentObserver(_volumeChanged); // 解除注册ContentObserver
        mMainActivity.stopService(new Intent(mMainActivity, BGMusicService.class));
        _isReadToGo = false;
    }


    @Override
    public Context GetContext() {
        return mMainActivity.getApplicationContext();
    }

    @Override
    public void TakenPicture() {
        if (_isReadToGo)
            _camera.takePicture(null, null, pictureCallback);

        _isReadToGo = false;
    }

    void initCamera() {
        int cameraCount = Camera.getNumberOfCameras();
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();

        for (int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK)
                _back_camera_index = i;
            else if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT)
                _front_camera_index = i;
        }
        _currentCameraIndex = _back_camera_index;
    }

    void initControl(View view) {
        _previewContainer = (FrameLayout) view.findViewById(R.id.flCameraContainer); //宽度和高度 设置成1dp，所以看不见 预览
        _shChangeStatus = (Switch) view.findViewById(R.id.status_change_state);
        _statusContainer = (FrameLayout) view.findViewById(R.id.status_container);
        _tvStatus = (TextView) view.findViewById(R.id.status_capture_status);
        _shUseFrontCamera = (Switch) view.findViewById(R.id.status_change_camera);

        _shChangeStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    GoWithAnimate();
                else
                    RelaxWithAnimate();
            }
        });

        _shUseFrontCamera.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                _currentCameraIndex = isChecked ? _front_camera_index : _back_camera_index;
                relax();
                readyToGo();
            }
        });

        _colorAnimation = Utils.GenerateColorAnimator(mMainActivity, R.animator.status_color_change, _statusContainer);//对 某个容器使用 ObjectAnimator



    }

    @Override
    public void OnEnter() {

    }


}
