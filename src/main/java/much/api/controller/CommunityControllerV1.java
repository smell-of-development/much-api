package much.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import much.api.controller.swagger.CommunityApiV1;
import much.api.dto.request.CommunityPostForm;
import much.api.dto.request.CommunitySearch;
import much.api.dto.response.CommunityPostDetail;
import much.api.dto.response.CommunityPostSummary;
import much.api.dto.response.Envelope;
import much.api.dto.response.PagedResult;
import much.api.service.CommunityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/communities")
public class CommunityControllerV1 implements CommunityApiV1 {

    private final CommunityService communityService;


    @Override
    @GetMapping("/{postId}")
    public ResponseEntity<Envelope<CommunityPostDetail>> getPost(@PathVariable Long postId) {

        return ok(
                Envelope.ok(communityService.getPost(postId))
        );
    }

    @Override
    @GetMapping
    public ResponseEntity<Envelope<PagedResult<CommunityPostSummary>>> getPosts(@ModelAttribute @Valid CommunitySearch searchCondition) {

        return ok(
                Envelope.ok(communityService.getPosts(searchCondition))
        );
    }


    @Override
    @PostMapping
    public ResponseEntity<Envelope<Long>> createCommunityPost(@RequestBody @Valid CommunityPostForm request) {

        return ok(
                Envelope.ok(communityService.createPost(request).getId())
        );
    }


    @Override
    @PutMapping("/{postId}")
    public ResponseEntity<Envelope<Long>> modifyCommunityPost(@PathVariable Long postId,
                                                              @RequestBody @Valid CommunityPostForm request) {

        return ok(
                Envelope.ok(communityService.modifyPost(postId, request).getId())
        );
    }


    @Override
    @DeleteMapping("/{postId}")
    public ResponseEntity<Envelope<Void>> deleteCommunityPost(@PathVariable Long postId) {

        communityService.deletePost(postId);
        return ok(
                Envelope.empty()
        );
    }

}
