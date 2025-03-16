package com.mycompany.app.utils;

import java.util.List;


public interface ResultBuilder<T> {
    ResultBuilder<T> withOffset(int offset);
    ResultBuilder<T> withLimit(int limit);

    List<T> result();
}