package com.adveisor.g2.monopoly.engine.service.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Service
@NoArgsConstructor
public class Dice {

    private int firstThrow;
    private int secondThrow;
    private boolean pasch;


    public int getTotal() {
        return firstThrow + secondThrow;
    }

    // automatically set pasch based on the throwd number
    public void setIfPasch() {
        this.pasch = firstThrow == secondThrow;
    }

    public boolean isValid() {
        return firstThrow > 0 && secondThrow > 0
                && firstThrow < 7 && secondThrow < 7;
    }

    public void setZero() {
        firstThrow = 0;
        secondThrow = 0;
    }

}
