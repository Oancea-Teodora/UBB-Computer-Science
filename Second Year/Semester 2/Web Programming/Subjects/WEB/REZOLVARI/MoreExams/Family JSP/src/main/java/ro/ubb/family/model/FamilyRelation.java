package ro.ubb.family.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FamilyRelation extends BaseEntity<Long>{
    String username;
    String mother;
    String father;

}
