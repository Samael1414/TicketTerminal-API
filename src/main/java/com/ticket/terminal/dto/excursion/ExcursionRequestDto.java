package com.ticket.terminal.dto.excursion;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ticket.terminal.dto.category.CategoryVisitorCountDto;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class ExcursionRequestDto {

    @JsonProperty("OrgName")
    private String orgName;

    @JsonProperty("ContactPersonName")
    private String contactPersonName;

    @JsonProperty("ContactPersonPhone")
    private String contactPersonPhone;

    @JsonProperty("ContactPersonMail")
    private String contactPersonMail;

    @JsonProperty("ServiceId")
    private Long serviceId;

    @JsonProperty("Visitor")
    private List<CategoryVisitorCountDto> visitor;

    @JsonProperty("VisitObject")
    private List<Long> visitObject;

    private LocalDateTime dtDate;

    @JsonProperty("Comment")
    private String comment;
}
