package com.ticket.terminal.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
