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

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CommunityControllerV1 implements CommunityApiV1 {

    private final CommunityService communityService;

    @Override
    @PostMapping("/communities")
    public ResponseEntity<Envelope<CommunityPostDetail>> createCommunityPost(@RequestBody @Valid CommunityPostCreation request) {

        return ok(
                Envelope.ok(communityService.createPost(request))
        );
    }


    @Override
    @PutMapping("/communities/{postId}")
    public ResponseEntity<Envelope<CommunityPostDetail>> modifyCommunityPost(@PathVariable Long postId,
                                                                             @RequestBody @Valid CommunityPostModification request) {

        return ok(
                Envelope.ok(communityService.modifyPost(postId, request))
        );
    }


    @Override
    @DeleteMapping("/communities/{postId}")
    public ResponseEntity<Envelope<Void>> deleteCommunityPost(@PathVariable Long postId) {

        communityService.deletePost(postId);
        return ok(
                Envelope.empty()
        );
    }

}
