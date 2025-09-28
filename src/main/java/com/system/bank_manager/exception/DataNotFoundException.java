package com.system.bank_manager.exception;

import lombok.Getter;

@Getter
public class DataNotFoundException extends RuntimeException {
    private final Long id;
    private final String entity;


    public DataNotFoundException(Long id, String entity) {
        super(String.format("%s con ID %d no se encontro", entity, id));
        this.id = id;
        this.entity = entity;
    }

}
