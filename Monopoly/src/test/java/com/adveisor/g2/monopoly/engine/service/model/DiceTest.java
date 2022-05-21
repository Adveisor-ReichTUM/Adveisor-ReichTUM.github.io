package com.adveisor.g2.monopoly.engine.service.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiceTest {

    int [] i = new int[2];

    @Test
    void throwDices() {
        assertNotNull(Dice.throwDices());
    }

    @Test
    void getTotal() {
        i = Dice.throwDices();
        int expected = i[0] + i[1];
        int actual = Dice.getTotal(i);
        assertEquals(expected, actual);
    }

    @Test
    void isPasch() {
        i = Dice.throwDices();
        boolean expected = (i[0] == i[1]);
        boolean actual = Dice.isPasch(i);
        assertEquals(expected, actual);
    }
}