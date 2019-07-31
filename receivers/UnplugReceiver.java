package CUSTOM.PACKAGE.NAME.receivers;

import CUSTOM.PACKAGE.NAME.workers.PlugWorker;
import CUSTOM.PACKAGE.NAME.workers.WorkerCommon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

// triggered on device unplug in manifest.xml
// requires special permissions in manifest

public class UnplugReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context mContext, Intent mIntent) {
        PlugWorker.TriggerPlugWorker(WorkerCommon.EntryType.PLUG, "unplug");
    }

}
