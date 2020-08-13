package edu.tdd.demo.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SupplyRecord {

    private Integer productId;

    private String productName;

    private String productCategory;

    private Integer quantity;

}
