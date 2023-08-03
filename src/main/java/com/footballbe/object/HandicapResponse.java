package com.footballbe.object;

import com.footballbe.object.Handicap;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class HandicapResponse {
    private HashMap<String, HashMap<String, List<Handicap>>> company;
}
