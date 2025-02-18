package br.com.judev.backend.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class PageableDTO {
    @SuppressWarnings("rawtypes")
    private List content = new ArrayList<>();
    private boolean first;
    private boolean last;
    @JsonProperty("page")
    private int number;
    private int size;
    @JsonProperty("pageElements")
    private int numberOfElements;
    private int totalPages;
    private int totalElements;
}
