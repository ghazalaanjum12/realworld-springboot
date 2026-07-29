package com.conduit.service;

import com.conduit.entity.User;
import com.conduit.openapi.model.Profile;
import com.conduit.repository.FollowRepository;
import com.conduit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final AuthenticationFacade authenticationFacade;

    public Profile getProfileDetails(String username) {
        User followedUser = userRepository.findByHandle(username).get();
        Profile profile = new Profile();
        profile.setUsername(followedUser.getHandle());
        profile.setBio(JsonNullable.of(followedUser.getBio()));
        profile.setImage(JsonNullable.of(followedUser.getImageUrl()));

        Optional<User> followerUser = authenticationFacade.getCurrentUser();
        if (followerUser.isEmpty()) {
            profile.setFollowing(false);
        } else {
            boolean following = followRepository.existsByFollowerIdAndFollowedId(followerUser.get().getId(), followedUser.getId());
            profile.setFollowing(following);
        }
        return profile;
    }
}
