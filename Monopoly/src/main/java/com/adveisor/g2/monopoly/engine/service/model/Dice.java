package com.adveisor.g2.monopoly.engine.service.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Random;

@Getter
@Setter
@Service
public class Dice {

    @Max(6)
    @Min(1)
    private int firstThrow;
    @Max(6)
    @Min(1)
    private int secondThrow;
    private boolean pasch;


    public int getTotal() {
        return firstThrow + secondThrow;
    }

    // automatically set pasch based on the throwd number
    public void setIfPasch() {
        this.pasch = firstThrow == secondThrow;
    }

}
