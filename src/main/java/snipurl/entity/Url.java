package snipurl.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "url_table")
public class Url {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Lob
    private String originalLink;
    private String shortLink;
    private LocalDateTime creationDate;
    private LocalDateTime expirationDate;
}
