package com.vet24.dao.medicine;

import com.vet24.dao.ReadWriteDao;
import com.vet24.models.medicine.Medicine;

import java.util.List;


public interface MedicineDao extends ReadWriteDao<Long, Medicine> {

    List<Medicine> searchFull(String manufactureName, String name, String searchText);
    List<Medicine> search(String manufactureName, String name);
}
