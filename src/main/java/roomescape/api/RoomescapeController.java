package roomescape.api;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationCreated;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.utils.DateTimeConverter;

import java.sql.PreparedStatement;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class RoomescapeController {

    private final JdbcTemplate jdbcTemplate;

    public RoomescapeController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Reservation> reservationRowMapper = (rs, rowNum) -> {
        ReservationTime time = new ReservationTime(
                rs.getLong("time_id"),
                DateTimeConverter.timeConverter(rs.getString("time_value"))
        );
        return new Reservation(
                rs.getLong("reservation_id"),
                rs.getString("name"),
                DateTimeConverter.dateConverter(rs.getString("date")),
                time
        );
    };

    @GetMapping
    public List<ReservationResponse> search() {
        String sql = "SELECT r.id as reservation_id, r.name, r.date, t.id as time_id, t.start_at as time_value " +
                "FROM reservation as r INNER JOIN reservation_time as t ON r.time_id = t.id";
        return jdbcTemplate.query(sql, reservationRowMapper).stream()
                .map(ReservationResponse::new)
                .toList();
    }

    @PostMapping
    public ReservationCreated add(@RequestBody ReservationRequest reservationRequest) {
        String sql = "INSERT INTO reservation (name, date, time_id) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, reservationRequest.name());
            ps.setString(2, reservationRequest.date());
            ps.setLong(3, reservationRequest.timeId());
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        return new ReservationCreated(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
