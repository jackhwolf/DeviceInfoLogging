package CUSTOM.PACKAGE.NAME.workers;

import CUSTOM.PACKAGE.NAME.MainActivity;
import CUSTOM.PACKAGE.NAME.backend.S3Util;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import net.gotev.uploadservice.BinaryUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;
import java.io.IOException;
import java.net.URL;

// uploads device_info file to S3
// requires access to app's MainActivity/class which extends AppCompatActivity

public class UploadWorker extends Worker {

    private static MainActivity mAct;
    private static final String ENTRY_TYPE = WorkerCommon.EntryType.LOG;
    private final static String DATA = "upload";

    public UploadWorker(@NonNull Context mContext, @NonNull WorkerParameters mWorkParams) {
        super(mContext, mWorkParams);
    }

    private void UploadDeviceInfo() {
        S3Util s3 = new S3Util(mAct);
        URL uploadURL = s3.GenerateURL(WorkerCommon.DEVICE_INFO_FILEPATH);
        try {
            UploadNotificationConfig mConfig = new UploadNotificationConfig();
            mConfig = mConfig.setRingToneEnabled(false);
            BinaryUploadRequest mBinaryReq = new BinaryUploadRequest(mAct, uploadURL.toString())
                    .setNotificationConfig(mConfig)
                    .setMethod("PUT")
                    .setDelegate(new UploadStatus())
                    .setMaxRetries(2);
            mBinaryReq.setFileToUpload(WorkerCommon.DEVICE_INFO_FILEPATH);
            mBinaryReq.setAutoDeleteFilesAfterSuccessfulUpload(false);
            mBinaryReq.startUpload();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void LogUpload(String log) {
        try {
            WorkerCommon.LogEntry(log);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Worker.Result doWork() {
        String log = System.currentTimeMillis() + " " + ENTRY_TYPE + " " + DATA;
        try {
            LogUpload(log);
            UploadDeviceInfo();
            return Worker.Result.success();
        } catch (Throwable t) {
            return Worker.Result.failure();
        }
    }

    public static void TriggerUploadWorker(MainActivity mMainActivity) {
        mAct = mMainActivity;
        WorkManager mWorkManager = WorkManager.getInstance();
        Constraints mConstraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        OneTimeWorkRequest mOneTimeReq = new OneTimeWorkRequest.Builder(UploadWorker.class)
                .setConstraints(mConstraints)
                .build();
        mWorkManager.enqueue(mOneTimeReq.from(UploadWorker.class));
    }

    public class UploadStatus implements UploadStatusDelegate {

        @Override
        public void onProgress(Context mCont, UploadInfo mUploadInfo) {
        }

        @Override
        public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
        }

        @Override
        public void onCancelled(Context context, UploadInfo uploadInfo) {
        }

        @Override
        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
        }
    }
}
