package jiyeon.app.payments.memberProfile.controller;

import jiyeon.app.payments.memberProfile.dto.MemberProfileListResponse;
import jiyeon.app.payments.memberProfile.dto.SortType;
import jiyeon.app.payments.memberProfile.service.MemberProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class MemberProfileController {

    private final MemberProfileService memberProfileService;

    @GetMapping
    public ResponseEntity<List<MemberProfileListResponse>> getProfiles(
            @RequestParam(defaultValue = "NAME_ASC") SortType sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<MemberProfileListResponse> profiles = memberProfileService.getProfiles(sort, page, size);
        return ResponseEntity.ok(profiles);
    }

    @PostMapping("/{id}/view")
    public ResponseEntity<Void> increaseViewCount(@PathVariable Long id) {
        memberProfileService.increaseViewCount(id);
        return ResponseEntity.noContent().build();
    }
}
