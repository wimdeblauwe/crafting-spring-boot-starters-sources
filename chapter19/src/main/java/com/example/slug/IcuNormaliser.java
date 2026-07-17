package com.example.slug;

import com.ibm.icu.text.Transliterator;

public class IcuNormaliser implements Normaliser {

    private static final String RULES =
            "Any-Latin; NFD; [:Nonspacing Mark:] Remove; NFC";

    private final Transliterator transliterator =
            Transliterator.getInstance(RULES);

    @Override
    public String normalise(String input) {
        return transliterator.transliterate(input);
    }
}
