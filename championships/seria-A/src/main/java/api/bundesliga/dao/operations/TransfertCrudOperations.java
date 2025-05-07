package api.bundesliga.dao.operations;

import api.bundesliga.dao.DataSource;
import api.bundesliga.entity.MouvementType;
import api.bundesliga.entity.Transfert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class TransfertCrudOperations {
    private final DataSource dataSource;

    public void saveMovement(Transfert movement) {
        String sql = "INSERT INTO player_transfer_movement (id, player_id, movement_type, club_id, transfer_date) " +
                "VALUES (?::uuid, ?::uuid, ?, ?::uuid, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {


            if (movement.getId() == null) {
                movement.setId(UUID.randomUUID().toString());
            }

            stmt.setString(1, movement.getId());
            stmt.setString(2, movement.getPlayerId());
            stmt.setString(3, movement.getMovementType().name());
            stmt.setString(4, movement.getClubId());
            stmt.setTimestamp(5, Timestamp.valueOf(movement.getTransferDate()));

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l’enregistrement du mouvement", e);
        }
    }



    public List<Transfert> findAll() {
        String sql = "SELECT * FROM player_transfer_movement";
        List<Transfert> list = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(Transfert.builder()
                        .id(rs.getString("id"))
                        .playerId(rs.getString("player_id"))
                        .movementType(MouvementType.valueOf(rs.getString("movement_type")))
                        .clubId(rs.getString("club_id"))
                        .transferDate(rs.getTimestamp("transfer_date").toLocalDateTime())
                        .build());
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des mouvements", e);
        }

        return list;
    }
}

