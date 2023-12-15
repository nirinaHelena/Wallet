package org.example.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Category {
    private UUID categoryId;
    private String categoryNam;
    private String categoryType;
    // type: debit or credit
}
