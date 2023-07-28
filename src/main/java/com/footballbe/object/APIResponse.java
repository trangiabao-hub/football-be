package com.footballbe.object;

import lombok.Data;

@Data
public class APIResponse<T> {
    int code;

    T results;

    ResultsExtra results_extra;
}
