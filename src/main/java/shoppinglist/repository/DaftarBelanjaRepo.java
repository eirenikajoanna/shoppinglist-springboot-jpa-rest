/*
 * DaftarBelanjaRepo.java
 *
 * Created on Mar 22, 2021, 00.19
 */
package shoppinglist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import shoppinglist.entity.DaftarBelanja;
import shoppinglist.entity.DaftarBelanjaDetil;

import java.util.List;

/**
 * @author irfin
 */
public interface DaftarBelanjaRepo extends JpaRepository<DaftarBelanja, Long>
{
    @Query(value = "SELECT * FROM daftar_belanjas WHERE " +
            "MATCH (JUDUL) " +
            "AGAINST (?1)",
            nativeQuery = true)
    public List<DaftarBelanja> search(String keyword);

    @Query(value = "SELECT * FROM daftar_belanja_details WHERE daftarbelanja_id = (?1) and nourut = (?2)",
            nativeQuery = true)
    public DaftarBelanjaDetil getDetil(long daftarbelanja_id, int nourut);
}
