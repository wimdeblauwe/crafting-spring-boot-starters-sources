package com.example.slug;

import java.text.Normalizer;

public class JavaTextNormaliser implements Normaliser {

    @Override
    public String normalise(String input) {
        String decomposed = Normalizer.normalize(input, Normalizer.Form.NFD);
        return decomposed.replaceAll("\\p{M}+", "");
    }
}
