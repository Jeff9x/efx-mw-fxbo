package com.empirefx.fxbo.models.provider;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AccountType {
    private int cid;
    private int id;
    private String name;
    private List<String> groups;
    private int priority;
    private List<String> leverages;
    private String category;
    private String description;
}
