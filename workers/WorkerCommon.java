package CUSTOM.PACKAGE.NAME.workers;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class WorkerCommon {

    public static final String DEVICE_INFO_FILEPATH = "/sdcard/device_info.txt";
    private static final String HEADER = "EPOCH_TIME ENTRY_TYPE DATA";
    public static final float LOW_BATTERY_CUTOFF = (float) 0.2;

    // supported entry types
    public static class EntryType {
        public static final String BATT       = "phone_battery_level";
        public static final String PLUG       = "plug";
        public static final String LOG        = "log_upload";
        public static final String SESSION    = "session";
    }

    // supported data types for video - kinda unneccessary but makes
    // it easy to control. Only used for video activity as of now
    public static class DataType {
        public static final String VID_START  = "start";
        public static final String VID_END    = "end";
        public static final String VID_DEL    = "delete";
        public static final String VID_UPLOAD = "upload";
    }

    // create file we want to write to
    private static void EnsureFileExists() throws IOException {
        File device_info = new File(DEVICE_INFO_FILEPATH);
        try {
            if (!device_info.exists()) {
                device_info.createNewFile();
                WorkerCommon.WriteLineToFile(HEADER);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    // write line to our file
    private static void WriteLineToFile(String data) {
        File device_info = new File(DEVICE_INFO_FILEPATH);
        try {
            FileWriter fw = new FileWriter(device_info, true);
            fw.write(data);
            fw.write(System.lineSeparator());
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // facilitate writing to file
    public static void LogEntry(String data) throws IOException {
        try {
            EnsureFileExists();
            WriteLineToFile(data);
        } catch (IOException e) {
            throw e;
        }
    }

    // get battery intent
    public static Intent GetBatteryIntent(Context mCont) {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent battery = mCont.registerReceiver(null, ifilter);
        return battery;
    }

}
