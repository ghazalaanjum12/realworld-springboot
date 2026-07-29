package com.conduit.repository;

import com.conduit.entity.Follow;
import com.conduit.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FollowRepository extends CrudRepository<Follow, UUID> {

    boolean existsByFollowerIdAndFollowedId(UUID followerId, UUID followedId);

    Optional<Follow> findByFollowerAndFollowed(User follower, User followed);

}
