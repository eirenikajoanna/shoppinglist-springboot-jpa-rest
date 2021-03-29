package shoppinglist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import shoppinglist.entity.DaftarBelanjaDetil;
import shoppinglist.entity.DaftarBelanjaDetilId;

import java.util.List;

public interface DaftarBelanjaDetilRepo extends JpaRepository<DaftarBelanjaDetil, DaftarBelanjaDetilId> {
    @Query(value = "SELECT * FROM daftar_belanja_details WHERE daftarbelanja_id = (?1)",
            nativeQuery = true)
    public List<DaftarBelanjaDetil> getAllDetil(long daftarbelanja_id);
}