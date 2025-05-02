package api.bundesliga.dao.operations;

import api.bundesliga.dao.DataSource;
import api.bundesliga.dao.mapper.SeasonMapper;
import api.bundesliga.entity.Season;
import api.bundesliga.entity.SeasonStatus;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Repository
@RequiredArgsConstructor
public class SeasonCrudOperations {
    private final  DataSource dataSource;
    private final SeasonMapper seasonMapper;

    public List<Season> getAll(int page, int size) {
        List<Season> seasons = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT s.year, s.alias, s.id, s.status FROM season s ORDER BY id ASC LIMIT ? OFFSET ?")) {

            statement.setInt(1, size);
            statement.setInt(2, (page - 1) * size);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    seasons.add(seasonMapper.apply(resultSet));
                }
            }
            return seasons;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    public List<Season> saveAll(List<Season> entities) {
        List<Season> seasons = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement =
                         connection.prepareStatement("insert into season (year, alias) values (?, ?)"
                                 + " returning id, year, alias, status")) {
                entities.forEach(entityToSave -> {
                    try {
                        statement.setInt(1, entityToSave.getYear());
                        statement.setString(2, entityToSave.getAlias());
                        statement.addBatch(); // group by batch so executed as one query in database
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        seasons.add(seasonMapper.apply(resultSet));
                    }
                }
                return seasons;
            }
        }
    }

    @SneakyThrows
    public Season updateSeasonStatus(int seasonYear, SeasonStatus newStatus) {
        try (Connection connection = dataSource.getConnection()) {
            Season current = findByYear(seasonYear);
            if (!current.getStatus().canTransitionTo(newStatus)) {
                throw new IllegalArgumentException("Invalid status transition");
            }

            try (PreparedStatement statement = connection.prepareStatement(
                    "UPDATE season SET status = ?::season_status WHERE year = ? RETURNING id, alias, year, status")) {
                statement.setString(1, newStatus.name());
                statement.setInt(2, seasonYear);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return seasonMapper.apply(resultSet);
                    } else {
                        throw new IllegalStateException("Season update failed");
                    }
                }
            }
        }
    }

    @SneakyThrows
    public Season findByYear(int year) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM season WHERE year = ?")) {
                statement.setInt(1, year);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        return seasonMapper.apply(rs);
                    } else {
                        throw new NoSuchElementException("Season not found for year: " + year);
                    }
                }
            }
        }
    }


}
