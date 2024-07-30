package com.viettel.vht.services.interfaces;

import com.viettel.vht.dtos.PostRequestDTO;
import com.viettel.vht.entities.PostEntity;

import java.util.List;

public interface PostService {
    List<PostEntity> getPostsByProperties(PostRequestDTO dto);
}
