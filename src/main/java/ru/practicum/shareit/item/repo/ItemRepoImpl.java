package ru.practicum.shareit.item.repo;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

@Repository
public class ItemRepoImpl implements ItemRepo {
    @Override
    public Collection<Item> findByUserId(long userId) {
        return null;
    }

    @Override
    public Item save(Item item) {
        return null;
    }

    @Override
    public void deleteByUserIdAndItemId(long userId, long itemId) {

    }
}
