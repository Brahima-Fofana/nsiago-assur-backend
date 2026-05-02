package ci.nsiago.assur.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "validation")
public class ValidationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String code;
    private Instant creation;
    private Instant expiration;

    @OneToOne(cascade = CascadeType.ALL)
    private UtilisateurEntity utilisateur;
}
