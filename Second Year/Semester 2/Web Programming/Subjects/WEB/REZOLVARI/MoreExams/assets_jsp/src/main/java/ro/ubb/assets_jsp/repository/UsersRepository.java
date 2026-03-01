package ro.ubb.assets_jsp.repository;

import ro.ubb.assets_jsp.model.AppUser;

public interface UsersRepository extends MainRepository<AppUser,Long>{
    AppUser findByUsernameAndPassword(String username, String password);
}
