package com.example.social.services.post;

import com.example.social.dto.PostData;
import com.example.social.dto.PostLikes;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface SavePost {

    void savePost(PostData postData, MultipartFile file) throws IOException;

    String updateLikes(PostLikes like, Integer postId);
}
