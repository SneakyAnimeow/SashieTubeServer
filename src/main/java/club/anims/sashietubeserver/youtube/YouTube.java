package club.anims.sashietubeserver.youtube;

import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.downloader.request.RequestVideoFileDownload;
import com.github.kiulian.downloader.downloader.request.RequestVideoInfo;
import com.github.kiulian.downloader.model.videos.VideoInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;

public abstract class YouTube {
    private static final Logger LOGGER = LoggerFactory.getLogger(YouTube.class);

    private static final YoutubeDownloader DOWNLOADER = new YoutubeDownloader();

    private static final Timer TIMER = new Timer();

    private static final HashMap<String, Long> VIDEO_EXPIRATION_TIMES = new HashMap<>();

    private static final long PERIOD = 1000 * 60;

    private static final long EXPIRATION_TIME = PERIOD * 60 * 12;

    private static final File VIDEOS_DIR = new File("videos");

    static {
        if(!VIDEOS_DIR.exists()) {
            VIDEOS_DIR.mkdir();
        }else{
            Arrays.stream(VIDEOS_DIR.listFiles()).forEach(File::delete);
        }

        TIMER.schedule(new TimerTask() {
            @Override
            public void run() {
                VIDEO_EXPIRATION_TIMES.entrySet().forEach(videoEntry -> videoEntry.setValue(videoEntry.getValue() - PERIOD));

                var expiredVideos = VIDEO_EXPIRATION_TIMES.entrySet().stream().filter(entry -> entry.getValue() <= 0).map(Map.Entry::getKey).toList();
                expiredVideos.forEach(video -> {
                    var videoFile = new File(VIDEOS_DIR, video+".mp4");
                    if(videoFile.exists()) {
                        videoFile.delete();
                        LOGGER.info("Deleted video due to expiration date: {}", video+".mp4");
                    }

                    VIDEO_EXPIRATION_TIMES.remove(video);
                });
            }
        }, 0L, PERIOD);
    }

    public static VideoInfo getInfo(String videoId){
        var infoRequest = new RequestVideoInfo(videoId);
        var response = DOWNLOADER.getVideoInfo(infoRequest);
        return response.data();
    }

    public static File download(String videoId){
        if(Arrays.stream(VIDEOS_DIR.listFiles()).anyMatch(file -> file.getName().contains(videoId))){
            return new File(VIDEOS_DIR, videoId+".mp4");
        }

        var info = getInfo(videoId);
        var format = info.bestVideoWithAudioFormat();

        var request = new RequestVideoFileDownload(format)
                .saveTo(VIDEOS_DIR)
                .renameTo(videoId)
                .overwriteIfExists(true);
        var response = DOWNLOADER.downloadVideoFile(request);
        var video = response.data();

        VIDEO_EXPIRATION_TIMES.put(videoId, EXPIRATION_TIME);

        return video;
    }
}
