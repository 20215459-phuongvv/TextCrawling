package com.viettel.vht.controllers;

import com.viettel.vht.dtos.PostRequestDTO;
import com.viettel.vht.dtos.PostResponseDTO;
import com.viettel.vht.entities.PostEntity;
import com.viettel.vht.services.interfaces.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/posts")
public class PostController {
    @Autowired
    private PostService postService;
    @GetMapping("")
    public PostResponseDTO getPost(PostRequestDTO dto) {
        return postService.getPostsByProperties(dto);
    }
}
