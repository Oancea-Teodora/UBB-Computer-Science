package ro.ubb.journals.model;


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
public class Article extends BaseEntity<Long>{
    String username;
    int journalId;
    String summary;
    int date;
}
