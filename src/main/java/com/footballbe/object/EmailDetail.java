package com.footballbe.object;

import lombok.Data;

@Data
public class EmailDetail {
    private String recipient;
    private String msgBody;
    private String subject;
    private String attachment;
}
