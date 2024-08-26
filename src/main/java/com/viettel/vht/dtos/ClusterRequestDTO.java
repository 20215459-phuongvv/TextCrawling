package com.viettel.vht.dtos;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClusterRequestDTO {
    int page = 0;
    int size = 10;
    String text;
    LocalDateTime from;
    LocalDateTime to;
}
