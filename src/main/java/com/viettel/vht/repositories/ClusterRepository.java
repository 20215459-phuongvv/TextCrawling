package com.viettel.vht.repositories;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.viettel.vht.dtos.ClusterRequestDTO;
import com.viettel.vht.dtos.ClusterResponseDTO;
import com.viettel.vht.entities.ClusterEntity;
import com.viettel.vht.entities.DocumentInfo;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import org.bson.Document;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ClusterRepository {
    public final MongoCollection<Document> clusterCollection;
    @Autowired
    public ClusterRepository(MongoDatabase database) {
        this.clusterCollection = database.getCollection("clusters");
    }
    public ClusterResponseDTO findClusters(ClusterRequestDTO clusterRequestDTO) {
        List<Bson> filters = new ArrayList<>();

        if (clusterRequestDTO.getTitle() != null && !clusterRequestDTO.getTitle().isEmpty()) {
            filters.add(Filters.regex("title", clusterRequestDTO.getTitle(), "i"));
        }

        if (clusterRequestDTO.getFrom() != null) {
            filters.add(Filters.gte("last_updated", clusterRequestDTO.getFrom()));
        }

        if (clusterRequestDTO.getTo() != null) {
            filters.add(Filters.lte("last_updated", clusterRequestDTO.getTo()));
        }

        Bson query = filters.isEmpty() ? new Document() : Filters.and(filters);
        long total = clusterCollection.countDocuments(query);
        List<Document> documents = clusterCollection.find(query)
                .sort(Sorts.descending("last_updated"))
                .skip(clusterRequestDTO.getPage() * clusterRequestDTO.getSize())
                .limit(clusterRequestDTO.getSize())
                .into(new ArrayList<>());
        List<ClusterEntity> clusterEntities = new ArrayList<>();
        if (!documents.isEmpty()) {
            for (Document document : documents) {
                clusterEntities.add(convertToClusterEntity(document));
            }
        }
        return ClusterResponseDTO.builder()
                .clusterEntityList(clusterEntities)
                .total(total)
                .build();
    }

    public ClusterEntity deleteClusterById(String id) {
        Document query = new Document("_id", new ObjectId(id));
        Document deletedDocument = clusterCollection.findOneAndDelete(query);
        if (deletedDocument != null) {
            return convertToClusterEntity(deletedDocument);
        }
        return null;
    }

    public ClusterEntity deleteClusterDocuments(String id, List<String> texts) {
        Document query = new Document("_id", new ObjectId(id));
        Document clusterDoc = clusterCollection.find(query).first();
        if (clusterDoc == null) {
            return null;
        }
        List<Document> documentsList = clusterDoc.getList("documents", Document.class);

        List<Document> filteredDocuments = documentsList.stream()
                .filter(doc -> !texts.contains(doc.getString("text")))
                .collect(Collectors.toList());

        Document update = new Document("$set", new Document("documents", filteredDocuments));
        clusterCollection.updateOne(query, update);
        Document updatedCluster = clusterCollection.find(query).first();
        if (updatedCluster != null) {
            return convertToClusterEntity(updatedCluster);
        }
        return null;
    }

    public ClusterEntity deleteClusterEvents(String id, List<String> events) {
        Document query = new Document("_id", new ObjectId(id));
        Document clusterDoc = clusterCollection.find(query).first();
        if (clusterDoc == null) {
            return null;
        }
        List<Document> documentsList = clusterDoc.getList("summarized_events", Document.class);

        List<Document> filteredDocuments = documentsList.stream()
                .filter(doc -> !events.contains(doc.getString("text")))
                .collect(Collectors.toList());

        Document update = new Document("$set", new Document("summarized_events", filteredDocuments));
        clusterCollection.updateOne(query, update);
        Document updatedCluster = clusterCollection.find(query).first();
        if (updatedCluster != null) {
            return convertToClusterEntity(updatedCluster);
        }
        return null;
    }

    public ClusterEntity mergeClusters(String id1, String id2) {
        Document clusterDoc1 = clusterCollection.find(new Document("_id", new ObjectId(id1))).first();
        Document clusterDoc2 = clusterCollection.find(new Document("_id", new ObjectId(id2))).first();

        if (clusterDoc1 == null || clusterDoc2 == null) {
            return null;
        }

        List<Document> documents1 = clusterDoc1.getList("documents", Document.class);
        List<Document> documents2 = clusterDoc2.getList("documents", Document.class);

        List<Document> mergedDocuments = new ArrayList<>(documents1);
        mergedDocuments.addAll(documents2);

        mergedDocuments.sort((doc1, doc2) -> doc2.getDate("time").compareTo(doc1.getDate("time")));

        Document update = new Document("$set", new Document("documents", mergedDocuments));
        clusterCollection.updateOne(new Document("_id", new ObjectId(id1)), update);
        clusterCollection.deleteOne(new Document("_id", new ObjectId(id2)));
        Document mergedCluster = clusterCollection.find(new Document("_id", new ObjectId(id1))).first();
        return convertToClusterEntity(mergedCluster);
    }

    private ClusterEntity convertToClusterEntity(Document document) {
        ClusterEntity clusterEntity = new ClusterEntity();
        clusterEntity.setId(document.getObjectId("_id").toString());
        clusterEntity.setTitle(document.getString("title"));
        clusterEntity.setLastUpdated(document.getDate("last_updated").toInstant().atOffset(ZoneOffset.UTC).toLocalDateTime());
        List<DocumentInfo> events = new ArrayList<>();
        List<Map<String, Object>> eventMaps = (List<Map<String, Object>>) document.get("summarized_events");
        for (Map<String, Object> eventMap : eventMaps) {
            DocumentInfo event = DocumentInfo.builder()
                    .time(((Date) eventMap.get("time")).toInstant().atOffset(ZoneOffset.UTC).toLocalDateTime())
                    .text((String) eventMap.get("text"))
                    .build();
            events.add(event);
        }

        List<DocumentInfo> documentsList = new ArrayList<>();
        List<Map<String, Object>> documentMaps = (List<Map<String, Object>>) document.get("documents");
        for (Map<String, Object> docMap : documentMaps) {
            DocumentInfo doc = DocumentInfo.builder()
                    .time(((Date) docMap.get("time")).toInstant().atOffset(ZoneOffset.UTC).toLocalDateTime())
                    .text((String) docMap.get("text"))
                    .build();
            documentsList.add(doc);
        }

        clusterEntity.setEvents(events);
        clusterEntity.setDocuments(documentsList);
        return clusterEntity;
    }
}
