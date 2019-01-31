package com.example.alirahal.androidcourse;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class LoadingTask extends AsyncTask<Integer,Integer, Void> {

    TextView loadingTextView;
    Activity activity;

    public LoadingTask(Activity activity) {
        this.activity = activity;

        loadingTextView = activity.findViewById(R.id.loadingTextView);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        System.out.println("On Pre Execute");

        loadingTextView.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        loadingTextView.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        loadingTextView.setText("" + values[0]);
    }

    @Override
    protected Void doInBackground(Integer... integers) {
        System.out.println("Do In Background");
        for (int i = 0; i < integers[0]; i++) {
            publishProgress(i);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
