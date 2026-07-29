package com.conduit.service;

import com.conduit.entity.Follow;
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
        fillProfile(profile, followedUser);
        return profile;
    }

    private void fillProfile(Profile profile, User followedUser) {
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
    }

    public Profile followProfile(String username) {
        User followedUser = userRepository.findByHandle(username).get();
        User followerUser = authenticationFacade.getCurrentUser().get();

        Follow follow = new Follow(followerUser, followedUser);
        followRepository.save(follow);

        Profile profile = new Profile();
        fillProfile(profile, followedUser);
        return profile;


    }

    public Profile unfollowProfile(String username) {
        User followedUser = userRepository.findByHandle(username).get();
        User followerUser = authenticationFacade.getCurrentUser().get();

        Optional<Follow> follow = followRepository.findByFollowerAndFollowed(followerUser, followedUser);
        followRepository.delete(follow.get());

        Profile profile = new Profile();
        fillProfile(profile, followedUser);
        return profile;

    }
}
