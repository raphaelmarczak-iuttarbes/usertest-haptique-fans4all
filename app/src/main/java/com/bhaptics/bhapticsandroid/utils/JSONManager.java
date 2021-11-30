package com.bhaptics.bhapticsandroid.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class JSONManager {

    public ArrayList<ExperimentData> experimentDatas = new ArrayList();

    public void AddExperiment(ExperimentData experimentData){
        boolean canAdd = true;
        for (ExperimentData data:experimentDatas) {
            Log.d( "Yova" ,  " List : " + data.date + " / " + data.fileName );
            if(experimentData.date == data.date){
                canAdd = false;
            }
        }
        if(canAdd){
            experimentDatas.add(experimentData);
        }
        //Log.d( "Yova" ,  experimentData.date + " / " + experimentData.fileName );

    }

    public void ExportJson(Context context){
        if (experimentDatas.size() != 0){

            //Text of the Document
            String textToWrite = CreateJsonObject().toString();

            //Checking the availability state of the External Storage.
            String state = Environment.getExternalStorageState();
            if (!Environment.MEDIA_MOUNTED.equals(state)) {

                //If it isn't mounted - we can't write into it.
                return;
            }

            //Create a new file that points to the root directory, with the given name:
            File file = new File(context.getExternalFilesDir(null), "Fans4all"+ experimentDatas.get(0).date + ".json" );

            //This point and below is responsible for the write operation
            FileOutputStream outputStream = null;
            try {
                file.createNewFile();
                //second argument of FileOutputStream constructor indicates whether
                //to append or create new file if one exists
                outputStream = new FileOutputStream(file, true);

                outputStream.write(textToWrite.getBytes());
                outputStream.flush();
                outputStream.close();
            }
            catch (IOException e) {
                Log.e("Yova", e.toString());
            }
        }
    }

    public JSONObject CreateJsonObject(){
        JSONObject object = new JSONObject();
        JSONArray experimentArray = new JSONArray();

        try {
            for ( ExperimentData data : experimentDatas ) {
                experimentArray.put(CreateJsonExperimentData(data));
            }
            object.put("ExperimentsData", experimentArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("Yova", object.toString());

        return object;
    }

    public void PurgeJSON(){
        experimentDatas = new ArrayList<>();
    }

    private JSONObject CreateJsonExperimentData(ExperimentData experimentData) throws JSONException {
        JSONObject result = new JSONObject();
        result.put("Date", experimentData.date.toString());
        result.put("File Name", experimentData.fileName);
        return  result;
    }

}
