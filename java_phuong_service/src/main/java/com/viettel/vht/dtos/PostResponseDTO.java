package com.viettel.vht.dtos;

import com.viettel.vht.entities.PostEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponseDTO {
    List<PostEntity> postEntityList;
    Long total;
}
