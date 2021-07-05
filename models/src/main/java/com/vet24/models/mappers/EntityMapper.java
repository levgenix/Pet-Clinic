package com.vet24.models.mappers;

import java.util.List;

public interface EntityMapper<D, E>{
    D toDto (E entity);
    List<D> toDto (List<E> entities);
}
