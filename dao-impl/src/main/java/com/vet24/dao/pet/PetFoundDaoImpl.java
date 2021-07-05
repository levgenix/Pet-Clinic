package com.vet24.dao.pet;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.pet.PetContact;
import com.vet24.models.pet.PetFound;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class PetFoundDaoImpl extends ReadWriteDaoImpl<Long, PetFound> implements PetFoundDao {

}
