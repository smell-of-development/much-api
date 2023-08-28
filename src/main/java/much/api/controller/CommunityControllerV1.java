package much.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import much.api.controller.swagger.CommunityApiV1;
import much.api.dto.request.CommunityPostCreation;
import much.api.dto.request.CommunityPostModification;
import much.api.dto.response.CommunityPostDetail;
import much.api.dto.response.Envelope;
import much.api.service.CommunityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CommunityControllerV1 implements CommunityApiV1 {

    private final CommunityService communityService;

    @Override
    @PostMapping("/community")
    public ResponseEntity<Envelope<CommunityPostDetail>> createCommunityPost(@RequestBody @Valid CommunityPostCreation request) {

        return ok(
                Envelope.ok(communityService.createPost(request))
        );
    }


    @Override
    @PutMapping("/community")
    public ResponseEntity<Envelope<CommunityPostDetail>> modifyCommunityPost(CommunityPostModification request) {

        return ok(
                Envelope.ok(communityService.modifyPost(request))
        );
    }

}
