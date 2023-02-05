package com.example.social.services.friend;

import com.example.social.entities.Friends;
import jakarta.persistence.Tuple;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface GetFriend {

    CompletableFuture<List<Friends>> getFriendsOfUser(String userEmail);
    Tuple getCountsOfFriendOfUser(String email);

    boolean isFriendOfUser(String byUser,String onUser);
}