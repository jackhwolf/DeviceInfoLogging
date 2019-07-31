package CUSTOM.PACKAGE.NAME.workers;

import CUSTOM.PACKAGE.NAME.MainActivity;

import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

// class to handle logging battery life every N minutes

public class BatteryWorker extends Worker {

    private static final int UPLOAD_WAIT_TIME = 20;
    private final String ENTRY_TYPE = WorkerCommon.EntryType.BATT;
    private static MainActivity mAct = null;

    public BatteryWorker(@NonNull Context mContext, @NonNull WorkerParameters mWorkParams) {
        super(mContext, mWorkParams);
    }

    // use battery intent to get percentage
    private float GetBatteryPercentage(Intent mBattery) {
        int level = mBattery.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = mBattery.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        float percentage = level / (float)scale;
        return percentage;
    }


    // writes the battery info to file
    private float LogBatteryPercentage() throws IOException{
        Intent mBattery = WorkerCommon.GetBatteryIntent(getApplicationContext());
        Float mBatteryPercentage = GetBatteryPercentage(mBattery);
        Long now = System.currentTimeMillis();
        String line = now + " " + ENTRY_TYPE + " " + mBatteryPercentage;
        try {
            WorkerCommon.LogEntry(line);
        } catch (IOException e) {
            throw e;
        }
        return mBatteryPercentage;
    }

    @Override
    public Result doWork() {
        try {
            float mBatteryPercentage = LogBatteryPercentage();
            if (mBatteryPercentage <= WorkerCommon.LOW_BATTERY_CUTOFF) {
                UploadWorker.TriggerUploadWorker(mAct);
            }
            return Result.success();
        } catch (IOException e) {
            return Result.failure();
        }
    }

    /**
     * static method called from MainActivity through any fragment
     */
    public static void TriggerBatteryWorker(MainActivity mainActivity) {
        mAct = mainActivity;
        WorkManager mWorkManager = WorkManager.getInstance();
        Constraints mConstraints = new Constraints.Builder().build();
        OneTimeWorkRequest mOneTimeReq = new OneTimeWorkRequest.Builder(BatteryWorker.class)
                .setConstraints(mConstraints)
                .build();
        String TAG = "BatteryWorker";
        PeriodicWorkRequest.Builder mPeriodicBuilder = new PeriodicWorkRequest
                .Builder(BatteryWorker.class, UPLOAD_WAIT_TIME, TimeUnit.MINUTES)
                .addTag(TAG)
                .setConstraints(mConstraints);
        PeriodicWorkRequest mPeriodicReq = mPeriodicBuilder.build();
        mWorkManager.enqueueUniquePeriodicWork(TAG,
                                               ExistingPeriodicWorkPolicy.KEEP,
                                               mPeriodicReq);
    }

}
