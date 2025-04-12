package com.ticket.terminal.util;

import com.ticket.terminal.entity.UsersEntity;
import org.springframework.stereotype.Component;

@Component
public class ActionLogFactory {

    public UsersEntity map(Integer id) {
        if (id == null) return null;
        UsersEntity user = new UsersEntity();
        user.setId(Long.valueOf(id));
        return user;
    }
}
