package com.vet24.models.mappers;

import java.util.List;

public interface DtoMapper<E, D> {
    E toEntity (D dto);
    List<E> toEntity (List<D> dto);
}
