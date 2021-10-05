package com.raytotti.convertcurrency.commons.application;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ResponseCollection<T> {

    private boolean hasNext;
    private Collection<T> items = new ArrayList<>();

    public static <T> ResponseCollection<T> from(Page<T> page) {
        return new ResponseCollection<>(
                !page.isLast(),
                page.getContent());
    }

}
