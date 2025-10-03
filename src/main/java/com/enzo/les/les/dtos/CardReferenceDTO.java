package com.enzo.les.les.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardReferenceDTO {
    @NotNull
    private Long cartaoId;
}
