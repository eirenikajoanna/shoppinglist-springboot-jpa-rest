/*
 * ShoppingListService.java
 *
 * Created on Mar 22, 2021, 01.20
 */
package shoppinglist.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shoppinglist.entity.DaftarBelanja;
import shoppinglist.entity.DaftarBelanjaDetil;
import shoppinglist.repository.DaftarBelanjaDetilRepo;
import shoppinglist.repository.DaftarBelanjaRepo;

import java.util.List;

/**
 * @author irfin
 */
@Service
public class ShoppingListService
{
    @Autowired
    private DaftarBelanjaRepo repo;
    private DaftarBelanjaDetilRepo repoDetil;

    public Iterable<DaftarBelanja> getAllData()
    {
        return repo.findAll();
    }

    public boolean create(DaftarBelanja entity, DaftarBelanjaDetil[] arrDetil)
    {
        try {
            // Pertama simpan dahulu objek DaftarBelanja tanpa mengandung detil apapun.
            repo.save(entity);

            // Setelah berhasil tersimpan, baca primary key auto-generate lalu set sebagai bagian dari
            // ID composite di DaftarBelanjaDetil.
            int noUrut = 1;
            for (DaftarBelanjaDetil detil : arrDetil) {
                detil.setId(entity.getId(), noUrut++);
                entity.addDaftarBarang(detil);
            }
            repo.save(entity);

            return true;
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
            return false;
        }
    }
    public List<DaftarBelanja> search(String keyword){
        return repo.search(keyword);
    }
    public boolean update(DaftarBelanja entity, DaftarBelanjaDetil[] arrDetil) {
        try {
            DaftarBelanja existingDaftarBelanja = repo.findById(entity.getId()).orElse(null);
            existingDaftarBelanja.setJudul(entity.getJudul());
            existingDaftarBelanja.setTanggal(entity.getTanggal());
            repo.save(existingDaftarBelanja);

            int noUrut = 1;
            for (DaftarBelanjaDetil detil : arrDetil) {
                DaftarBelanjaDetil existingDaftarBelanjaDetil = repo.getDetil(entity.getId(), detil.getNoUrut());
                existingDaftarBelanjaDetil.setNamaBarang(detil.getNamaBarang());
                existingDaftarBelanjaDetil.setByk(detil.getByk());
                existingDaftarBelanjaDetil.setMemo(detil.getMemo());
                existingDaftarBelanjaDetil.setSatuan(detil.getSatuan());
                repoDetil.save(existingDaftarBelanjaDetil);
            }
            return true;
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
            return false;
        }
    }
//    public boolean delete(long id) {
//        try {
//            DaftarBelanja existingDaftarBelanja = repo.findById(id).orElse(null);
//            List<DaftarBelanjaDetil> arrDetil = repoDetil.getAllDetil(id);
//
//            int noUrut = 1;
//            for (DaftarBelanjaDetil detil : arrDetil) {
//                DaftarBelanjaDetil existingDaftarBelanjaDetil = repo.getDetil(id, detil.getNoUrut());
//                if (!repoDetil.deleteBydaftarbelanja_id(id)){
//                    return false;
//                }
//            }
//            repo.deleteById(id);
//            return true;
//        }
//        catch (Exception e) {
//            e.printStackTrace(System.out);
//            return false;
//        }
//    }
//
//    public DaftarBelanja getProductById(long id) {
//        return repo.findById(id).orElse(null);
//    }
}
