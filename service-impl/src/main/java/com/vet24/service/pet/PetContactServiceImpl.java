package com.vet24.service.pet;

import com.vet24.dao.pet.PetContactDao;
import com.vet24.models.pet.PetContact;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.xml.bind.DatatypeConverter;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;

@Service
public class PetContactServiceImpl extends ReadWriteServiceImpl<Long, PetContact> implements PetContactService {

    private final PetContactDao petContactDao;

    public PetContactServiceImpl(PetContactDao petContactDao) {
        super(petContactDao);
        this.petContactDao = petContactDao;
    }

    @Override
    public List<String> getAllPetCode() {
        return petContactDao.getAllPetCode();
    }

    @Override
    public boolean isExistByPetCode(String petCode) {
        return petContactDao.isExistByPetCode(petCode);
    }

    @Override
    public PetContact getByPetCode(String petCode) {
        return petContactDao.getByPetCode(petCode);
    }

    @Override
    public int getCountId() {
        return petContactDao.getCountId();
    }

    @Override
    public String randomPetContactUniqueCode(Long id) {

        try {
            SecureRandom random = SecureRandom.getInstanceStrong();
            byte[] rnd = new byte[16];
            random.nextBytes(rnd);
            IvParameterSpec ivSpec = new IvParameterSpec(rnd);
            // Prepare key
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            keygen.init(256);
            SecretKey secretKey = keygen.generateKey();
            //Encrypt key
            String encryptValue = id.toString();
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
            byte[] enc = cipher.doFinal(encryptValue.getBytes());
            // Decrypt key. Just in case.
            /*cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
            String result = new String(cipher.doFinal(enc));
            System.err.println(result);*/
            return DatatypeConverter.printHexBinary(enc);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new IllegalStateException("Fail to generate secret key", e);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            throw new IllegalStateException("Cipher generator init error", e);
        }
    }
}
