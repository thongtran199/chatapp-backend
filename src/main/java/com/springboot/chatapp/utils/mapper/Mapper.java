package com.springboot.chatapp.utils.mapper;

public interface Mapper<A,B> {
    B mapToResponseDTO(A a);
    A mapToEntity(B b);
}