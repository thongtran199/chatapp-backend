package com.springboot.chatapp.mapper;

public interface Mapper<A,B> {
    B mapToResponseDTO(A a);
    A mapToEntity(B b);
}