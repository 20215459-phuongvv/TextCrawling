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
public class PostEntity {
    String url;
    List<String> tagList;
    String title;
    String author;
    LocalDateTime time;
    String summary;
    String content;
    String source;
}
