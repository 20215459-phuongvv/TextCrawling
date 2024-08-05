package com.viettel.vht.services.interfaces;

import com.viettel.vht.dtos.PostRequestDTO;
import com.viettel.vht.dtos.PostResponseDTO;
import com.viettel.vht.entities.PostEntity;

import java.util.List;
import java.util.Set;

public interface PostService {
    PostResponseDTO getPostsByProperties(PostRequestDTO dto);

    Set<String> getAllTags();
}
