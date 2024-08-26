package com.viettel.vht.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClusterEntity {
    String id;
    String title;
    List<DocumentInfo> events;
    List<DocumentInfo> documents;
    LocalDateTime lastUpdated;
}