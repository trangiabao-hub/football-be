package com.footballbe.dto.request;

import com.footballbe.entity.UnderrafterValue;
import lombok.Data;

import java.util.List;

@Data
public class UnderrafterRequest {
    List<UnderrafterValue> underrafterValues;
}
