package com.ticket.terminal.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "category_visitors")
public class CategoryVisitorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_name", nullable = false)
    private String categoryVisitorName;

    // ➕ Метод-заглушка для getGroupCategoryVisitorId()
    public Long getGroupCategoryVisitorId() {
        return this.id; // временно вернем id как группу
    }
}
