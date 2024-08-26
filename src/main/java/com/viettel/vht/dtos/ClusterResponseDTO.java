package com.viettel.vht.dtos;

import com.viettel.vht.entities.ClusterEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClusterResponseDTO {
    List<ClusterEntity> clusterEntityList;
    long total;
}
