package com.example.newswebsite.mapper;

import com.example.newswebsite.config.LocalVariable;
import com.example.newswebsite.dto.post.PostDTO;
import com.example.newswebsite.entity.Interactions;
import com.example.newswebsite.entity.Post;
import com.example.newswebsite.entity.UserEntity;
import com.example.newswebsite.security.principal.UserDetailService;
import com.example.newswebsite.service.impl.InteractionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostMapper {
    @Autowired
    InteractionServiceImpl interactionService;
    @Autowired
    UserDetailService userDetailService;

    public PostDTO mapperPostToDTO(Post post){
        UserEntity userEntity= post.getUserEntity();
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setTitle(post.getTitle());
        postDTO.setThumbnail(post.getThumbnail());
        postDTO.setContent(post.getContent());
        postDTO.setStatus(post.getStatus());
        postDTO.setParentId(post.getParentId());
        postDTO.setSlug(post.getSlug());
        postDTO.setTotalView(post.getTotalView());
        postDTO.setTotalComment(post.getTotalComment());
        postDTO.setUserId(userEntity.getId());
        postDTO.setUserName(userEntity.getFullname());
        postDTO.setAvatar(userEntity.getAvatar());
        postDTO.setPostDate(post.getCreateAt());
        postDTO.setNumberVote(interactionService.countUpVoteByPost(post.getId()));
        postDTO.setNumberDislike(interactionService.countDownVoteByPost(post.getId()));
        postDTO.setCategoryId(post.getCategory().getId());
        postDTO.setCategoryName(post.getCategory().getTitle());
        // check tuong tac cua user hien tai
        if (userDetailService.getCurrentUser() == null){
            postDTO.setIsVoted(LocalVariable.isFalse);
        }
        else if (interactionService.findByUserAndPost(userDetailService.getCurrentUser().getId(), post.getId()) != null){
            Interactions interactions = interactionService.findByUserAndPost(userDetailService.getCurrentUser().getId(), post.getId());
            if (interactions.getUpVote() == LocalVariable.isTrue)
            {
                postDTO.setIsVoted(LocalVariable.isTrue);
            }
            else postDTO.setIsVoted(LocalVariable.isFalse);

            if(interactions.getDownVote() == LocalVariable.isTrue){
                postDTO.setIsDisliked(LocalVariable.isTrue);
            }else {
                postDTO.setIsDisliked(LocalVariable.isFalse);
            }
        }else {
            postDTO.setIsVoted(LocalVariable.isFalse);
            postDTO.setIsDisliked(LocalVariable.isFalse);
        }

        return postDTO;
    }
}
