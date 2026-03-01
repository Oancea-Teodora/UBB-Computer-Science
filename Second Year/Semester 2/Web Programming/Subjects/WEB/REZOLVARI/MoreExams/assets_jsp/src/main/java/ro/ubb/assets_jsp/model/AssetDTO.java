package ro.ubb.assets_jsp.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class AssetDTO {
    String name;
    String description;
    int value;
}
