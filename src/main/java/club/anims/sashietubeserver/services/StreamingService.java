package club.anims.sashietubeserver.services;

import club.anims.sashietubeserver.youtube.YouTube;
import org.springframework.core.io.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class StreamingService {
    public Mono<Resource> getVideo(String videoName) {
        return Mono.just(new FileSystemResource(YouTube.download(videoName)));
    }
}
