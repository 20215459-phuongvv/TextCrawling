package com.viettel.vht.services;

import com.viettel.vht.dtos.PostRequestDTO;
import com.viettel.vht.dtos.PostResponseDTO;
import com.viettel.vht.entities.PostEntity;
import com.viettel.vht.repositories.PostRepository;
import com.viettel.vht.services.interfaces.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;
    @Override
    public PostResponseDTO getPostsByProperties(PostRequestDTO dto) {
        return postRepository.findPosts(dto);
    }
}
