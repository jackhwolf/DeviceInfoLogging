package CUSTOM.PACKAGE.NAME.workers;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import java.io.IOException;

// class to handle logging plug-in/unplug events

public class PlugWorker extends Worker {

    private static String ENTRY_TYPE;
    private static String mData;

    public PlugWorker(@NonNull Context mContext, @NonNull WorkerParameters mWorkParams) {
        super(mContext, mWorkParams);
    }

    private void LogPlugEvent() throws IOException {
        Long now = System.currentTimeMillis();
        String line = now + " " + ENTRY_TYPE + " " + mData;
        try {
            WorkerCommon.LogEntry(line);
        } catch (IOException e) {
            throw e;
        }
    }

    @Override
    public Worker.Result doWork() {
        try {
            LogPlugEvent();
            return Worker.Result.success();
        } catch (IOException e) {
            e.printStackTrace();
            return Worker.Result.failure();
        }
    }

    public static void TriggerPlugWorker(String etype, String data) {
        ENTRY_TYPE = etype;
        mData = data;
        Constraints mConstraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        OneTimeWorkRequest mOneTimeReq = new OneTimeWorkRequest.Builder(PlugWorker.class)
                .setConstraints(mConstraints)
                .build();
        WorkManager mWorkMngr = WorkManager.getInstance();
        mWorkMngr.enqueue(mOneTimeReq);
    }
}
