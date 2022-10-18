package com.advertising.service.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ManageListing {

    @Schema(description = "The state request ")
    @NotEmpty
    State state;

    @Schema(description = "Boolean show exception or not when the listings are managed")
    @NotEmpty
    boolean showException;
}
