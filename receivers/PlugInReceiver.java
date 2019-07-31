package CUSTOM.PACKAGE.NAME.Receivers;

import CUSTOM.PACKAGE.NAME.workers.PlugWorker;
import CUSTOM.PACKAGE.NAME.workers.WorkerCommon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

// triggered on device plugin in manifest.xml
// requires special permissions in manifest

public class PlugInReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context mContext, Intent mIntent) {
        PlugWorker.TriggerPlugWorker(WorkerCommon.EntryType.PLUG, "plugin");
    }

}
