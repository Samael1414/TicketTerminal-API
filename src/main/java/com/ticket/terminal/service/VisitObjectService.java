package com.ticket.terminal.service;


import com.ticket.terminal.dto.VisitObjectDto;
import com.ticket.terminal.mapper.VisitObjectMapper;
import com.ticket.terminal.repository.VisitObjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VisitObjectService {


    private final VisitObjectRepository repository;
    private final VisitObjectMapper mapper;

    public List<VisitObjectDto> getAllVisitObjects() {
        return mapper.toDtoList(repository.findAll());
    }
}
