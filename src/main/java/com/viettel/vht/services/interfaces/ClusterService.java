package com.viettel.vht.services.interfaces;

import com.viettel.vht.dtos.ClusterRequestDTO;
import com.viettel.vht.dtos.ClusterResponseDTO;
import com.viettel.vht.entities.ClusterEntity;

import java.util.List;

public interface ClusterService {

    ClusterResponseDTO getClustersByProperties(ClusterRequestDTO dto);

    ClusterEntity deleteClusterById(String id);

    ClusterEntity deleteClusterDocuments(String id, List<String> texts);

    ClusterEntity deleteClusterEvents(String id, List<String> events);

    ClusterEntity mergeClusters(String id1, String id2);
}
