package com.swiftpot.wsdownloader.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;

import com.swiftpot.wsdownloader.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import de.mateware.snacky.Snacky;

/**
 * Created by Ace Programmer Rbk<rodney@swiftpot.com> on 06-May-17
 * 8:24 AM
 */
public class RecyclerViewMediaAdapter extends RecyclerView.Adapter<RecyclerViewMediaAdapter.FileHolder> {

    private static String DIRECTORY_TO_SAVE_MEDIA_NOW = "/WSDownloader/";
    private ArrayList<File> filesList;
    private Activity activity;

    public RecyclerViewMediaAdapter(ArrayList<File> filesList, Activity activity) {
        this.filesList = filesList;
        this.activity = activity;
        setHasStableIds(true);
    }

    @Override
    public RecyclerViewMediaAdapter.FileHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_media_row_item, parent, false);
        return new FileHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewMediaAdapter.FileHolder holder, int position) {
        File currentFile = filesList.get(position);

        holder.buttonImageDownload.setOnClickListener(this.downloadMediaItem(currentFile));
        holder.buttonVideoDownload.setOnClickListener(this.downloadMediaItem(currentFile));

        if (currentFile.getAbsolutePath().endsWith(".mp4")) {
            holder.cardViewImageMedia.setVisibility(View.GONE);
            holder.cardViewVideoMedia.setVisibility(View.VISIBLE);
            Uri video = Uri.parse(currentFile.getAbsolutePath());
            holder.videoViewVideoMedia.setVideoURI(video);
            holder.videoViewVideoMedia.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                    holder.videoViewVideoMedia.start();
                }
            });
        } else {
            Bitmap myBitmap = BitmapFactory.decodeFile(currentFile.getAbsolutePath());
            holder.imageViewImageMedia.setImageBitmap(myBitmap);
        }

    }

    @Override
    public int getItemCount() {
        return filesList.size();
    }

    public View.OnClickListener downloadMediaItem(final File sourceFile) {

        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new Runnable(){

                    @Override
                    public void run() {
                        try {
                            copyFile(sourceFile, new File(Environment.getExternalStorageDirectory().toString() + DIRECTORY_TO_SAVE_MEDIA_NOW + sourceFile.getName()));
                            Snacky.builder().
                                    setActivty(activity).
                                    setText(R.string.save_successful_message).
                                    success().
                                    show();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("RecyclerV", "onClick: Error:"+e.getMessage() );

                            Snacky.builder().
                                    setActivty(activity).
                                    setText(R.string.save_error_message).
                                    error().
                                    show();
                        }
                    }
                }.run();
            }
        };
    }

    /**
     * copy file to destination.
     *
     * @param sourceFile
     * @param destFile
     * @throws IOException
     */
    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }


    public static class FileHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageViewImageMedia;
        VideoView videoViewVideoMedia;
        CardView cardViewVideoMedia;
        CardView cardViewImageMedia;
        Button buttonVideoDownload;
        Button buttonImageDownload;

        public FileHolder(View itemView) {
            super(itemView);
            imageViewImageMedia = (ImageView) itemView.findViewById(R.id.imageViewImageMedia);
            videoViewVideoMedia = (VideoView) itemView.findViewById(R.id.videoViewVideoMedia);
            cardViewVideoMedia = (CardView) itemView.findViewById(R.id.cardViewVideoMedia);
            cardViewImageMedia = (CardView) itemView.findViewById(R.id.cardViewImageMedia);
            buttonImageDownload = (Button) itemView.findViewById(R.id.buttonImageDownload);
            buttonVideoDownload = (Button) itemView.findViewById(R.id.buttonVideoDownload);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }

    }
}
