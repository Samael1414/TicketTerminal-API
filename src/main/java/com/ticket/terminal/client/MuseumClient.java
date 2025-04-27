package com.ticket.terminal.client;

import com.ticket.terminal.config.FeignClientConfiguration;
import com.ticket.terminal.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;


@FeignClient(
        name = "museumGateClient",
        url  = "${application.clients.tonline-gate.url}",
        configuration = FeignClientConfiguration.class
)
public interface MuseumClient {

    @GetMapping(
            value    = "/Service/Simple",
            produces = MediaType.APPLICATION_JSON_VALUE)
    List<SimpleServiceDto> getSimpleServiceList();

    @PostMapping(
            value    = "/Create",
            produces = MediaType.APPLICATION_JSON_VALUE)
    OrderCreateResponseDto createEditableOrder(@RequestBody EditableOrderRequestDto requestDto);

    @PostMapping(
            value    = "/CreateSimple",
            produces = MediaType.APPLICATION_JSON_VALUE)
    OrderCreateResponseDto createSimpleOrder(@RequestBody SimpleOrderRequestDto requestDto);

    @GetMapping(
            value = "/Service/FullEditable",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    TLMuseumServiceResponseDto getFullEditableService();

}
