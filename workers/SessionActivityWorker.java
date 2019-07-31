package CUSTOM.PACKAGE.NAME.workers;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkContinuation;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import java.io.IOException;

// class to handle loggin game start & end

public class SessionActivityWorker extends Worker {

    private static final String ENTRY_TYPE = WorkerCommon.EntryType.SESSION;
    private static String mData;

    public SessionActivityWorker(@NonNull Context mContext, @NonNull WorkerParameters mWP) {
        super(mContext, mWP);
    }

    @Override
    public Worker.Result doWork() {
        String log = System.currentTimeMillis() + " " + ENTRY_TYPE + " " + mData;
        try {
            WorkerCommon.LogEntry(log);
            return Worker.Result.success();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Worker.Result.failure();
    }

    public static void TriggerSessionActivityWorker(String data) {
        mData = data;
        WorkManager mWorkManager = WorkManager.getInstance();
        Constraints mConstraints = new Constraints.Builder()
                .build();
        OneTimeWorkRequest mSessReq = new OneTimeWorkRequest.Builder(SessionActivityWorker.class)
                .setConstraints(mConstraints)
                .build();
        mWorkManager.enqueue(mSessReq.from(SessionActivityWorker.class));
    }
}
