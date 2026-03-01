package ro.ubb.assets_jsp.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Asset extends BaseEntity<Long>{

    Long userId;
    String name;
    String description;
    int value;
}
