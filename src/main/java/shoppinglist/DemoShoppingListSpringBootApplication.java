package shoppinglist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import shoppinglist.entity.DaftarBelanja;
import shoppinglist.entity.DaftarBelanjaDetil;
import shoppinglist.repository.DaftarBelanjaDetilRepo;
import shoppinglist.repository.DaftarBelanjaRepo;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@SpringBootApplication
public class DemoShoppingListSpringBootApplication implements CommandLineRunner
{
    @Autowired
    private DaftarBelanjaRepo repo;
    private DaftarBelanjaDetilRepo repoDetil;

    public static void main(String[] args)
    {
        SpringApplication.run(DemoShoppingListSpringBootApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception
    {
        System.out.println("Membaca Semua Record DaftarBelanja:");
        List<DaftarBelanja> all = repo.findAll();
        for (DaftarBelanja db : all) {
            System.out.println("[" + db.getId() + "] " + db.getJudul());

            List<DaftarBelanjaDetil> listBarang = db.getDaftarBarang();
            for (DaftarBelanjaDetil barang : listBarang) {
                System.out.println("\t" + barang.getNamaBarang() + " " + barang.getByk() + " " + barang.getSatuan());
            }
        }
        
        Scanner keyb = new Scanner(System.in);

        // Nomor 1
        // Baca berdasarkan ID
        System.out.print("Masukkan ID dari objek DaftarBelanja yg ingin ditampilkan: ");
        long id = Long.parseLong(keyb.nextLine());
        System.out.println("Hasil pencarian: ");
        
        Optional<DaftarBelanja> optDB = repo.findById(id);
        if (optDB.isPresent()) {
            DaftarBelanja db = optDB.get();
            System.out.println("\tJudul: " + db.getJudul());
        }
        else {
            System.out.println("\tTIDAK DITEMUKAN.");
        }

        //Nomor 2
        //Mencari daftar DaftarBelanja berdasarkan kemiripan string judul yg diberikan

        System.out.println("\nMencari daftar DaftarBelanja berdasarkan kemiripan string judul yg diberikan");
        System.out.print("Masukkan judul : ");
        String judul = keyb.nextLine();
        List<DaftarBelanja> miripDaftarBelanja = repo.search(judul);
        if(miripDaftarBelanja.size() > 0){
            for (DaftarBelanja i : miripDaftarBelanja) {
                System.out.println(i.getJudul());
                List<DaftarBelanjaDetil> dbd = i.getDaftarBarang();

                for (DaftarBelanjaDetil k : dbd) {
                    System.out.println("Nama: " + k.getNamaBarang() + "\n"
                            + "Banyak: " + k.getByk() + "\n"
                            + "Satuan:  " + k.getSatuan());
                }
            }
        } else {
            System.out.println("\tTIDAK DITEMUKAN.");
        }

        //Nomor 3
        //Menyimpan sebuah objek DaftarBelanja ke tabel database.

        System.out.println("\nMenyimpan sebuah objek DaftarBelanja ke tabel database");
        System.out.print("Masukkan judul : ");
        judul = keyb.nextLine();
        DaftarBelanja newDaftarBelanja = new DaftarBelanja();
        newDaftarBelanja.setJudul(judul);
        newDaftarBelanja.setTanggal(LocalDateTime.now());
        newDaftarBelanja = repo.save(newDaftarBelanja);

        List<DaftarBelanjaDetil> details = new LinkedList();
        int cntr = 1;
        String namaBarang = "";
        while(true){
            System.out.println("Masukkan Done untuk berhenti");
            System.out.print("Nama Barang :");
            namaBarang = keyb.nextLine();

            if(namaBarang.equals("Done")){
                break;
            }

            System.out.print("Banyak :");
            float jml = Float.parseFloat(keyb.nextLine());

            System.out.print("Satuan :");
            String satuan = keyb.nextLine();

            System.out.print("Memo :");
            String memo = keyb.nextLine();

            DaftarBelanjaDetil detil = new DaftarBelanjaDetil();
            detil.setNoUrut(cntr);
            detil.setNamaBarang(namaBarang);
            detil.setByk(jml);
            detil.setSatuan(satuan);
            detil.setMemo(memo);
            details.add(detil);
            cntr++;
        }

        for (DaftarBelanjaDetil detil : details) {
            detil.setInduk(newDaftarBelanja);
            repoDetil.save(detil);
        }

        optDB = repo.findById(newDaftarBelanja.getId());
        newDaftarBelanja = optDB.get();
        System.out.println("INSERTED");
        System.out.println(newDaftarBelanja.getJudul());
        List<DaftarBelanjaDetil> daftarDetil = newDaftarBelanja.getDaftarBarang();
        for (DaftarBelanjaDetil detil : daftarDetil) {
            System.out.println("\t" + detil.getNamaBarang() + " " + detil.getByk() + " " + detil.getSatuan());
        }

        //Nomor 4
        //Mengupdate sebuah objek DaftarBelanja ke tabel database.
        System.out.println("\nMengupdate sebuah objek DaftarBelanja ke tabel database");
        System.out.print("Masukkan ID : ");
        id = Long.parseLong(keyb.nextLine());
        Optional<DaftarBelanja>  existingDaftarBelanja = repo.findById(id);
        if (existingDaftarBelanja.isPresent()) {
            newDaftarBelanja = existingDaftarBelanja.get();
            System.out.print("Masukkan judul : ");
            judul = keyb.nextLine();
            newDaftarBelanja.setJudul(judul);

            daftarDetil  = newDaftarBelanja.getDaftarBarang();
            for (DaftarBelanjaDetil detil : daftarDetil) {

                System.out.print("Nama Barang : ");
                namaBarang = keyb.nextLine();
                detil.setNamaBarang(namaBarang);

                System.out.print("Banyak : ");
                float jml = Float.parseFloat(keyb.nextLine());
                detil.setByk(jml);

                System.out.print("Satuan : ");
                String satuan = keyb.nextLine();
                detil.setSatuan(satuan);

                System.out.print("Memo : ");
                String memo = keyb.nextLine();
                detil.setMemo(memo);

                repoDetil.save(detil);
            }
            optDB = repo.findById(newDaftarBelanja.getId());
            newDaftarBelanja = optDB.get();
            System.out.println("Updated");
            System.out.println(newDaftarBelanja.getJudul());
            daftarDetil = newDaftarBelanja.getDaftarBarang();
            for (DaftarBelanjaDetil detil : daftarDetil) {
                System.out.println("\t" + detil.getNamaBarang() + " " + detil.getByk() + " " + detil.getSatuan());
            }

        } else {
            System.out.println("\tTIDAK DITEMUKAN.");
        }

        //Nomor 5
        //Menghapus objek DaftarBelanja berdasarkan ID yg diberikan.
        System.out.println("\nMenghapus objek DaftarBelanja berdasarkan ID yg diberikan");
        System.out.print("Masukkan ID : ");
        id = Long.parseLong(keyb.nextLine());
        optDB = repo.findById(id);

        if (optDB.isPresent()) {
            DaftarBelanja daftarBelanja = optDB.get();
            System.out.println(daftarBelanja.getJudul());
            daftarDetil = daftarBelanja.getDaftarBarang();
            for (DaftarBelanjaDetil detil : daftarDetil) {
                repoDetil.deleteById(detil.getInduk());
            }
            repo.deleteById(daftarBelanja.getId());

            all = repo.findAll();
            System.out.println("Hasil: ");
            for (DaftarBelanja daftar : all) {
                System.out.println(daftar.getJudul());
                daftarDetil = daftar.getDaftarBarang();
                for (DaftarBelanjaDetil detil : daftarDetil) {
                    System.out.println("\t" + detil.getNamaBarang() + " " + detil.getByk() + " " + detil.getSatuan());
                }
            }
        } else {
            System.out.println("\tTIDAK DITEMUKAN.");
        }
    }




}
