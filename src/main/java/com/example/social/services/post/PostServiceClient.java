package com.example.social.services.post;

import com.example.social.dto.PostData;
import com.example.social.dto.PostLikes;
import com.example.social.entities.Friends;
import com.example.social.entities.Post;
import com.example.social.entities.User;
import com.example.social.mapper.PostMapper;
import com.example.social.repo.sm_repos.PostRepository;
import com.example.social.services.friend.FriendService;
import com.example.social.services.user.UserService;
import com.example.social.utils.ImageProcessor;
import com.example.social.utils.VideoProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class PostServiceClient implements PostService{

    private final Logger logger = LoggerFactory.getLogger(PostServiceClient.class);

    @Autowired
    private PostRepository repo;

    @Autowired
    private PostMapper mapper;

    @Autowired
    private UserService userService;

    @Autowired
    private FriendService friendService;

    @Autowired
    private VideoProcessor videoProcessor;


    @Override
    public void savePost(PostData postData, MultipartFile file) throws IOException {
        boolean isVideoFile = false;
        User user = userService.getUserByEmail(postData.getUserEmail());
        Post post = mapper.mapToEntity(postData);
        post.setUser(user);
        String fileName = file.getContentType();
        String[] fileWords = fileName.split("/");
        for(String fileWord : fileWords)
        {
            if(fileWord.equals("mp4"))
            {
                isVideoFile = true;
            }
        }
        if(isVideoFile)
        {
            String videoPath = videoProcessor.uploadVideo(file);
            post.setVideo(videoPath);
        }
        else if (!file.isEmpty())
        {
            post.setImage(ImageProcessor.uploadImage(file));
        }
        repo.save(post);
    }



//    @Override
//    public PostsOfUserWithFriends getPostsOfUser(String email) throws ExecutionException, InterruptedException {
//        List<List<PostData>> friendsPosts = new ArrayList<>();
//
//        CompletableFuture<List<Friends>> friends = friendService.getFriendsOfUser(email);
//        CompletableFuture<List<PostData>> userPosts = mapper.mapToPostDataList(repo.findPostsByUserEmail(friends.get().isEmpty() ? null : friends.get().get(0).getUser().getEmail()));
//
//        friends.thenApply(friendsData ->{
//            for (Friends friend : friendsData) {
//                CompletableFuture<List<PostData>> friendPostFuture = mapper.mapToPostDataList(repo.findPostsByUserEmail(friend.getUserFriends().getEmail()));
//                friendPostFuture.thenAccept(friendsPosts::add);
//            }
//            return friendsPosts;
//        });
//        return mapper.mapToPostUserWithFriends(userPosts.get(),friendsPosts);
//    }



    @Override
    public List<PostData> getPosts(String email) {

        List<Friends> friends = friendService.getFriendsOfUser(email);
        List<PostData> userPosts = mapper.mapToPostDataList(repo.findPostsByUserEmail(email));

        List<PostData> postDataList = new ArrayList<>(userPosts);

           for(int i=0; i < friends.size(); i++){
               List<PostData> friendPostFuture = mapper.mapToPostDataList(repo.findPostsByUserEmail(friends.get(i).getUserFriends().getEmail()));
               postDataList.addAll(friendPostFuture);
            }
        return postDataList;
    }


    @Override
    public String updateLikes(PostLikes postLikes, Integer postId) {
        Post post = repo.findByPostId(postId);
        post.setLikes(postLikes.getLikes());
        repo.save(post);
        return "Likes updated successfully";
    }
}

