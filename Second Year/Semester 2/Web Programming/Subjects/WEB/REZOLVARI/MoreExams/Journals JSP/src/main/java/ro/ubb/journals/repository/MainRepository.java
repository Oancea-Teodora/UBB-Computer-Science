package ro.ubb.journals.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;
import ro.ubb.journals.model.BaseEntity;

import java.io.Serializable;

@NoRepositoryBean
@Transactional
public interface MainRepository<T extends BaseEntity<ID>, ID extends Serializable>
        extends JpaRepository<T, ID> {


}