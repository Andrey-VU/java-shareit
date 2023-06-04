package ru.practicum.shareit.item.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
//    @Query("select id from items " +
//            "where upper(name) like upper(concat('%', ?1, '%')) " +
//            " or upper(description) like upper(concat('%', ?1, '%')) " +
//            "and isAvailable == true ")
//   List<Long> searchForItems(String text);

    List<Item> findByNameContainingIgnoreCase(String text);
}
