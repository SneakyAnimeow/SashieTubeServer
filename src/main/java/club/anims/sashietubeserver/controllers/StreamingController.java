package club.anims.sashietubeserver.controllers;


import club.anims.sashietubeserver.services.StreamingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class StreamingController {
    private Logger logger = LoggerFactory.getLogger(StreamingController.class);

    @Autowired
    private StreamingService streamingService;

    @GetMapping(value = "/stream", produces = "video/mp4")
    public Mono<Resource> getVideo(@RequestParam String v, @RequestHeader(value = "Range", required = false) String range) {
        logger.info("Requested video: {}", v);
        return streamingService.getVideo(v);
    }
}