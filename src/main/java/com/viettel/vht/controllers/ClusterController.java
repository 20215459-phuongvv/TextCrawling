package com.viettel.vht.controllers;

import com.viettel.vht.dtos.ClusterRequestDTO;
import com.viettel.vht.dtos.ClusterResponseDTO;
import com.viettel.vht.entities.ClusterEntity;
import com.viettel.vht.services.interfaces.ClusterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clusters")
public class ClusterController {
    @Autowired
    private ClusterService clusterService;
    @GetMapping("")
    public ClusterResponseDTO getClustersByProperties(ClusterRequestDTO dto) {
        return clusterService.getClustersByProperties(dto);
    }
    @PostMapping("/merge-clusters")
    public ClusterEntity mergeClusters(@RequestParam("id1") String id1,
                                       @RequestParam("id2") String id2) {
        return clusterService.mergeClusters(id1, id2);
    }
    @DeleteMapping("/{id}/documents")
    public ClusterEntity deleteClusterDocuments(@PathVariable String id,
                                                @RequestBody List<String> texts) {
        return clusterService.deleteClusterDocuments(id, texts);
    }
    @DeleteMapping("/{id}/events")
    public ClusterEntity deleteClusterEvents(@PathVariable String id,
                                             @RequestBody List<String> events) {
        return clusterService.deleteClusterEvents(id, events);
    }
    @DeleteMapping("/{id}")
    public ClusterEntity deleteClusterById(@PathVariable String id) {
        return clusterService.deleteClusterById(id);
    }
}
