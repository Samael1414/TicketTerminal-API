package com.ticket.terminal.security.annotation;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для проверки права на управление объектами посещения
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("@userPermissionEvaluator.canManageVisitObjects(authentication) or @userPermissionEvaluator.isRoot(authentication)")
public @interface RequireManageVisitObjects {
}
