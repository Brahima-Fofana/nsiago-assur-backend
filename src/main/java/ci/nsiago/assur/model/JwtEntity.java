package ci.nsiago.assur.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "jwt")
public class JwtEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(length = 512)
    private String value;
    private boolean expirer;
    private boolean desactiver;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE})
    private UtilisateurEntity utilisateur;

}
