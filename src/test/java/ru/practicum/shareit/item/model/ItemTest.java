package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    @Test
    void shouldSetId() {
        Item testItem = new Item();
        testItem.setId(1);
        assertEquals(1, testItem.getId(), "id не установлен, либо не получен");
    }
}