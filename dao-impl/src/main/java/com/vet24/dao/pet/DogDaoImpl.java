package com.vet24.dao.pet;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.pet.Dog;
import org.springframework.stereotype.Repository;

@Repository
public class DogDaoImpl extends ReadWriteDaoImpl<Long, Dog> implements DogDao {

}
