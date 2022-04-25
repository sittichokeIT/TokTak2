package th.ac.kmutnb.toktak2;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterVideo extends RecyclerView.Adapter<AdapterVideo.HolderVideo>{

    //context
    private Context context;
    //array list
    private ArrayList<ModelVideo> videoArrayList;

    @NonNull
    @Override
    public HolderVideo onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout row_video.xml
        View view = LayoutInflater.from(context).inflate(R.layout.row_video,parent,false);
        return new HolderVideo(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderVideo holder, int position) {

        //Get data
        ModelVideo modelVideo = videoArrayList.get(position);

//        String id = modelVideo.getId();
        String title = modelVideo.getTitle();
        String description = modelVideo.getDescription();
//        String timestamp = modelVideo.getTimestamp();
//        String videoUrl = modelVideo.getVideoUrl();

        holder.titleTv.setText(title);
        holder.desTv.setText(description);
        setVideoUrl(modelVideo, holder);
    }

    private void setVideoUrl(ModelVideo modelVideo, HolderVideo holder) {
        //show progress
        holder.progressBar.setVisibility(View.VISIBLE);

        //get video url
        String videoUrl = modelVideo.getVideoUrl();

        //Media controller for play, pause, seekbar, timer etc
        MediaController mediaController = new MediaController(context);
        mediaController.setAnchorView(holder.videoView);

        Uri videoUri = Uri.parse(videoUrl);
        holder.videoView.setMediaController(mediaController);
        holder.videoView.setVideoURI(videoUri);

        holder.videoView.requestFocus();
        holder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //video is ready to play
                holder.progressBar.setVisibility(View.GONE);
                mp.start();
                float videoRate = mp.getVideoWidth() / (float) mp.getVideoHeight();
                float screenRatio  = holder.videoView.getWidth() / (float) holder.videoView.getHeight();
                float scale = videoRate / screenRatio;
                if(scale >= 1f){
                    holder.videoView.setScaleX(scale);
                }else{
                    holder.videoView.setScaleY(1f/scale);
                }
            }
        });

//        holder.videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
//            @Override
//            public boolean onInfo(MediaPlayer mp, int what, int extra) {
//                //to check if buffering, rendering etc
//                switch (what){
//                    case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:{
//                        //rendering started
//                        holder.progressBar.setVisibility(View.VISIBLE);
//                        return true;
//                    }
//                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:{
//                        //buffering started
//                        return true;
//                    }
//                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:{
//                        //buffering ended
//                        holder.progressBar.setVisibility(View.GONE);
//                    }
//                }
//                return false;
//            }
//        });

        holder.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.start(); // restart video if completed
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoArrayList.size(); //return size of list
    }

    //constructor
    public AdapterVideo(Context context, ArrayList<ModelVideo> videoArrayList) {
        this.context = context;
        this.videoArrayList = videoArrayList;
    }

    //View holder class, holds, inits the UI views
    class HolderVideo extends RecyclerView.ViewHolder{

        //UI Views of row_Video.xml
        VideoView videoView;
        TextView titleTv,desTv;
        ProgressBar progressBar;

        public HolderVideo(@NonNull View itemView) {
            super(itemView);

            //init Ui Views of row_video.xml
            videoView = itemView.findViewById(R.id.videoView);
            titleTv = itemView.findViewById(R.id.titleTv);
            desTv = itemView.findViewById(R.id.desTv);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }
}
