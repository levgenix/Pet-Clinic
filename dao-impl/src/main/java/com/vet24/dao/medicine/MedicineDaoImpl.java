package com.vet24.dao.medicine;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.medicine.Medicine;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MedicineDaoImpl extends ReadWriteDaoImpl<Long, Medicine> implements MedicineDao {

    @Override
    public List<Medicine> searchFull(String manufactureName, String name, String searchText) {
        return manager.createNativeQuery("SELECT * from medicine WHERE "
                + "manufacture_name LIKE :manufactureName "
                + "AND name LIKE :name "
                + "AND setweight(to_tsvector(name), 'A') || setweight(to_tsvector(description), 'B')  @@ to_tsquery(:searchText)", Medicine.class)
                .setParameter("manufactureName", "%" + manufactureName + "%")
                .setParameter("name", "%" + name + "%")
                .setParameter("searchText", searchText)
                .getResultList();
    }

    @Override
    public List<Medicine> search(String manufactureName, String name) {
        return manager.createNativeQuery("SELECT * from medicine WHERE "
                + "manufacture_name LIKE :manufactureName "
                + "AND name LIKE :name "
                , Medicine.class)
                .setParameter("manufactureName", "%" + manufactureName + "%")
                .setParameter("name", "%" + name + "%")
                .getResultList();
    }
}
