package ru.practicum.shareit.user.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

//    @Query("UPDATE users set " +
//            "name  = ?1 " +
//            "email = ?2 " +
//            "WHERE id = ?3 "  )
//    User update(String name, String email, long id);
}
