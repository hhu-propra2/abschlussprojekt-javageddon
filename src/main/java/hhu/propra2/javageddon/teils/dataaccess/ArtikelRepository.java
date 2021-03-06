package hhu.propra2.javageddon.teils.dataaccess;

import hhu.propra2.javageddon.teils.model.Artikel;
import hhu.propra2.javageddon.teils.model.Benutzer;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ArtikelRepository extends CrudRepository<Artikel,Long> {

    List<Artikel> findByAktiv(boolean b);
    Artikel findById(long i);
    List<Artikel> findByEigentuemer(Benutzer b);

}
