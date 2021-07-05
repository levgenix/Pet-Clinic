package com.vet24.dao.pet;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.pet.Pet;
import org.springframework.stereotype.Repository;

@Repository
public class PetDaoImpl extends ReadWriteDaoImpl<Long, Pet> implements PetDao {
}
