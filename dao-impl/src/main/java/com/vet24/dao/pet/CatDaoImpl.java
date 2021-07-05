package com.vet24.dao.pet;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.pet.Cat;
import org.springframework.stereotype.Repository;

@Repository
public class CatDaoImpl extends ReadWriteDaoImpl<Long, Cat> implements CatDao {

}
