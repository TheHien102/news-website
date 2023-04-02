package com.example.newswebsite.dto.post;

import lombok.Data;

@Data
public class PostDTO {
    private Long id;
    private String title;
    private String content;
    private String slug;
    private String status;
    private Long parentId;
    private Long userId;
}