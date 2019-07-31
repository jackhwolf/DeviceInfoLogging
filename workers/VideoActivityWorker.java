package CUSTOM.PACKAGE.NAME.workers;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import java.io.IOException;

// class to handle loggin video activity 

public class VideoActivityWorker extends Worker {

    private static String ENTRY_TYPE = "video";
    private static String ACTIVITY;


    public VideoActivityWorker(@NonNull Context mContext, @NonNull WorkerParameters mWP) {
        super(mContext, mWP);
    }

    private void LogVideoActivity() {
        String log = System.currentTimeMillis() + " " + ENTRY_TYPE + " " + ACTIVITY;
        try {
            WorkerCommon.LogEntry(log);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Worker.Result doWork() {
        try {
            LogVideoActivity();
            return Worker.Result.success();
        } catch (Throwable t) {
            return Worker.Result.failure();
        }
    }

    public static void TriggerVideoWorker(String act) {
        ACTIVITY = act;
        WorkManager mWorkManager = WorkManager.getInstance();
        Constraints mConstraints = new Constraints.Builder()
                .build();
        OneTimeWorkRequest mOneTimeReq = new OneTimeWorkRequest.Builder(VideoActivityWorker.class)
                .setConstraints(mConstraints)
                .build();
        mWorkManager.enqueue(mOneTimeReq.from(VideoActivityWorker.class));
    }
}
