package ro.ubb.family.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;
import ro.ubb.family.model.BaseEntity;

import java.io.Serializable;

@NoRepositoryBean
@Transactional
public interface MainRepository<T extends BaseEntity<ID>, ID extends Serializable>
        extends JpaRepository<T, ID> {


}