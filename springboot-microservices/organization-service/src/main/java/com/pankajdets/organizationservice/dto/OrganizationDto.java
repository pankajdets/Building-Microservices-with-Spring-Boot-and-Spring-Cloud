package com.pankajdets.organizationservice.dto;


import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(
    description = "OrganizationDto Model Information"
)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationDto {
    
    
    private Long id;
    @Schema(description = "Organiation Name")
    private String organizationName;
    @Schema(description = "Organiation Description")
    private String organizationDescription;
    @Schema(description = "Organiation Code")
    private String organizationCode;
    @Schema(description = "Organiation Creation date")
    private LocalDateTime createdDate;
}
