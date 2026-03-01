package ro.ubb.family.repository;

import ro.ubb.family.model.FamilyRelation;

public interface FamilyRelationRepository  extends MainRepository<FamilyRelation, Long>{
    FamilyRelation findByUsernameAndFatherOrMother(String username,String mother, String father);
    FamilyRelation findByUsername(String username);
}
