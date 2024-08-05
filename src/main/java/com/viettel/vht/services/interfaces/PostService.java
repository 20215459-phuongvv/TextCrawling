package com.viettel.vht.services.interfaces;

import com.viettel.vht.dtos.PostRequestDTO;
import com.viettel.vht.dtos.PostResponseDTO;
import com.viettel.vht.entities.PostEntity;

import java.util.List;

public interface PostService {
    PostResponseDTO getPostsByProperties(PostRequestDTO dto);
}
