package com.viettel.vht.services;

import com.viettel.vht.dtos.ClusterRequestDTO;
import com.viettel.vht.dtos.ClusterResponseDTO;
import com.viettel.vht.entities.ClusterEntity;
import com.viettel.vht.repositories.ClusterRepository;
import com.viettel.vht.services.interfaces.ClusterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClusterServiceImpl implements ClusterService {
    @Autowired
    private ClusterRepository clusterRepository;
    @Override
    public ClusterResponseDTO getClustersByProperties(ClusterRequestDTO dto) {
        return clusterRepository.findClusters(dto);
    }

    @Override
    public ClusterEntity deleteClusterById(String id) {
        return clusterRepository.deleteClusterById(id);
    }

    @Override
    public ClusterEntity deleteClusterDocuments(String id, List<String> texts) {
        return clusterRepository.deleteClusterDocuments(id, texts);
    }

    @Override
    public ClusterEntity deleteClusterEvents(String id, List<String> events) {
        return clusterRepository.deleteClusterEvents(id, events);
    }

    @Override
    public ClusterEntity mergeClusters(String id1, String id2) {
        return clusterRepository.mergeClusters(id1, id2);
    }
}
