package com.conduit.service;

import com.conduit.entity.Follow;
import com.conduit.entity.User;
import com.conduit.exception.ProfileNotFoundException;
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
        Optional<User> followedUser = userRepository.findByHandle(username);
        if (followedUser.isEmpty()) {
            throw new ProfileNotFoundException(username + " not found!");
        }
        Profile profile = new Profile();
        fillProfile(profile, followedUser.get());
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
        Optional<User> followedUser = userRepository.findByHandle(username);
        if (followedUser.isEmpty()) {
            throw new ProfileNotFoundException(username + " not found!");
        }
        Optional<User> followerUser = authenticationFacade.getCurrentUser();

        Follow follow = new Follow(followerUser.get(), followedUser.get());
        followRepository.save(follow);

        Profile profile = new Profile();
        fillProfile(profile, followedUser.get());
        return profile;


    }

    public Profile unfollowProfile(String username) {
        Optional<User> followedUser = userRepository.findByHandle(username);
        if (followedUser.isEmpty()) {
            throw new ProfileNotFoundException(username + " not found!");
        }
        Optional<User> followerUser = authenticationFacade.getCurrentUser();

        Optional<Follow> follow = followRepository.findByFollowerAndFollowed(followerUser.get(), followedUser.get());
        followRepository.delete(follow.get());

        Profile profile = new Profile();
        fillProfile(profile, followedUser.get());
        return profile;

    }
}
