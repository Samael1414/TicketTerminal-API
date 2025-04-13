package com.ticket.terminal.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcursionRequestDto {

    private String orgName;
    private String contactPersonName;
    private String contactPersonPhone;
    private String contactPersonMail;
    private Long serviceId;
    private List<CategoryVisitorCountDto> visitor;
    private List<Long> visitObject;
    private LocalDateTime dtDate;
    private String comment;
}
