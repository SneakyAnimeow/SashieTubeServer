package club.anims.sashietubeserver.services;

import club.anims.sashietubeserver.youtube.YouTube;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.ByteArrayInputStream;
@Service
public class StreamingService {
    public Mono<Resource> getVideo(String videoName) {
        var videoData = YouTube.download(videoName);

        var inputStream = new ByteArrayInputStream(videoData.toByteArray());

        var inputStreamResource = new InputStreamResource(inputStream);

        return Mono.just(inputStreamResource);
    }
}
