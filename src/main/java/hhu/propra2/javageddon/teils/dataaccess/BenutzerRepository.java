package hhu.propra2.javageddon.teils.dataaccess;

import hhu.propra2.javageddon.teils.model.Benutzer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface BenutzerRepository extends CrudRepository<Benutzer,Long> {


}
