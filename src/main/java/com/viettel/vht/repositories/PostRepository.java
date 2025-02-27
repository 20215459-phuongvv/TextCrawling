package com.viettel.vht.repositories;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.viettel.vht.dtos.PostRequestDTO;
import com.viettel.vht.dtos.PostResponseDTO;
import com.viettel.vht.entities.PostEntity;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.ZoneOffset;
import java.util.*;

@Repository
public class PostRepository {
    public final MongoCollection<Document> postCollection;

    @Autowired
    public PostRepository(MongoDatabase database) {
        this.postCollection = database.getCollection("test");
    }

    public void save(PostEntity postEntity) {
        Document doc = postCollection.find(Filters.eq("url", postEntity.getUrl())).first();
        if (doc == null) {
            Document document = new Document("source", postEntity.getSource())
                    .append("url", postEntity.getUrl())
                    .append("title", postEntity.getTitle())
                    .append("author", postEntity.getAuthor())
                    .append("time", postEntity.getTime())
                    .append("tagList", postEntity.getTagList())
                    .append("summary", postEntity.getSummary())
                    .append("content", postEntity.getContent());
            postCollection.insertOne(document);
        }
    }
    public PostResponseDTO findPosts(PostRequestDTO postRequestDTO) {
        List<Bson> filters = new ArrayList<>();

        if (postRequestDTO.getTitle() != null && !postRequestDTO.getTitle().isEmpty()) {
            filters.add(Filters.regex("title", postRequestDTO.getTitle(), "i"));
        }

        if (postRequestDTO.getTagList() != null && !postRequestDTO.getTagList().isEmpty()) {
            filters.add(Filters.in("tagList", postRequestDTO.getTagList()));
        }

        if (postRequestDTO.getFrom() != null) {
            filters.add(Filters.gte("time", postRequestDTO.getFrom()));
        }

        if (postRequestDTO.getTo() != null) {
            filters.add(Filters.lte("time", postRequestDTO.getTo()));
        }

        Bson query = filters.isEmpty() ? new Document() : Filters.and(filters);
        long total = postCollection.countDocuments(query);
        List<Document> documents = postCollection.find(query)
                .sort(Sorts.descending("time"))
                .skip(postRequestDTO.getPage() * postRequestDTO.getSize())
                .limit(postRequestDTO.getSize())
                .into(new ArrayList<>());
        List<PostEntity> postEntities = new ArrayList<>();
        if (!documents.isEmpty()) {
            for (Document document : documents) {
                PostEntity postEntity = new PostEntity();
                postEntity.setSource(document.getString("source"));
                postEntity.setUrl(document.getString("url"));
                postEntity.setTitle(document.getString("title"));
                postEntity.setAuthor(document.getString("author"));
                if (document.getDate("time") != null)
                    postEntity.setTime(document.getDate("time").toInstant().atOffset(ZoneOffset.UTC).toLocalDateTime());
                postEntity.setTagList((List<String>) document.get("tagList"));
                postEntity.setSummary(document.getString("summary"));
                postEntity.setContent(document.getString("content"));
                postEntities.add(postEntity);
            }
        }
        return PostResponseDTO.builder()
                .postEntityList(postEntities)
                .total(total)
                .build();
    }

    public Set<String> getAllTags() {
        List<Document> pipeline = Arrays.asList(
                new Document("$unwind", "$tagList"),
                new Document("$group", new Document("_id", null)
                        .append("uniqueTags", new Document("$addToSet", "$tagList"))
                )
        );
        AggregateIterable<Document> result = postCollection.aggregate(pipeline);
        Set<String> uniqueTags = new HashSet<>();
        if (result.first() != null) {
            List<String> tags = (List<String>) result.first().get("uniqueTags");
            if (tags != null) {
                uniqueTags.addAll(tags);
            }
        }
        return uniqueTags;
    }
}
