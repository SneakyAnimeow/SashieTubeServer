package club.anims.sashietubeserver.controllers;


import club.anims.sashietubeserver.services.StreamingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class StreamingController {
    @Autowired
    private StreamingService streamingService;

    @GetMapping(value = "/stream", produces = "video/mp4")
    public Mono<Resource> getVideo(@RequestParam String v) {
        return streamingService.getVideo(v);
    }
}