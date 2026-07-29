package com.conduit.api;

import com.conduit.openapi.api.ProfilesApiDelegate;
import com.conduit.openapi.model.ProfileResponse;
import com.conduit.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfilesApiDelegateImpl implements ProfilesApiDelegate {

    private final ProfileService profileService;

    @Override
    public ResponseEntity<ProfileResponse> getProfileByUsername(String username) {
        ProfileResponse profileResponse = new ProfileResponse(profileService.getProfileDetails(username));
        return ResponseEntity.ok(profileResponse);
    }
}
