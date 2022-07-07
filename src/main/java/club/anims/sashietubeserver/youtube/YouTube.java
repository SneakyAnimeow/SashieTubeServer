package club.anims.sashietubeserver.youtube;

import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.downloader.request.RequestVideoInfo;
import com.github.kiulian.downloader.downloader.request.RequestVideoStreamDownload;
import com.github.kiulian.downloader.model.videos.VideoInfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public abstract class YouTube {
    private static final YoutubeDownloader DOWNLOADER = new YoutubeDownloader();

    private static final Timer TIMER = new Timer();

    public static VideoInfo getInfo(String videoId){
        var infoRequest = new RequestVideoInfo(videoId);
        var response = DOWNLOADER.getVideoInfo(infoRequest);
        return response.data();
    }

    public static ByteArrayOutputStream download(String videoId){
        var info = getInfo(videoId);

        var format = info.bestVideoWithAudioFormat();

        var videoDataStream = new ByteArrayOutputStream();
        var request = new RequestVideoStreamDownload(format, videoDataStream);
        DOWNLOADER.downloadVideoStream(request);

        TIMER.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    videoDataStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }, 3600000L);

        return videoDataStream;
    }
}
