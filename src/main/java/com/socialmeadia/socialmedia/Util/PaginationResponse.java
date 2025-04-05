package com.socialmeadia.socialmedia.Util;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class PaginationResponse {
    private Object data;
    private String lastEvaluatedKey;
    private int limit;
    private boolean hasMore;
}