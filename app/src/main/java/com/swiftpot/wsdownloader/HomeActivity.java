package com.swiftpot.wsdownloader;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import com.swiftpot.wsdownloader.adapter.RecyclerViewMediaAdapter;

import java.io.File;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private static final String WHATSAPP_STATUSES_LOCATION="/storage/emulated/legacy/WhatsApp/Media/.Statuses";
    private TextView mTextMessage;
    private RecyclerView mRecyclerViewMediaList;
    private LinearLayoutManager mLinearLayoutManager;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_pics);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_videos);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mRecyclerViewMediaList = (RecyclerView) findViewById(R.id.recyclerViewMedia);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerViewMediaList.setLayoutManager(mLinearLayoutManager);
        RecyclerViewMediaAdapter recyclerViewMediaAdapter = new RecyclerViewMediaAdapter(this.getListFiles(new File(WHATSAPP_STATUSES_LOCATION)),HomeActivity.this);
        mRecyclerViewMediaList.setAdapter(recyclerViewMediaAdapter);


        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    /**
     * get all the files in specified directory
     * @param parentDir
     * @return
     */
    private ArrayList<File> getListFiles(File parentDir) {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files = parentDir.listFiles();
        for (File file : files) {

                if(file.getName().endsWith(".jpg")||
                        file.getName().endsWith(".gif")||
                        file.getName().endsWith(".mp4")){
                    if(!inFiles.contains(file))
                    inFiles.add(file);
                }
        }
        return inFiles;
    }
}
