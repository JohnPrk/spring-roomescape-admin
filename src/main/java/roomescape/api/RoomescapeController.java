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

    private final RowMapper<Reservation> reservationRowMapper = (rs, rowNum) -> new Reservation(
            rs.getLong("id"),
            rs.getString("name"),
            DateTimeConverter.dateConverter(rs.getString("date")),
            DateTimeConverter.timeConverter(rs.getString("time"))
    );

    @GetMapping
    public List<ReservationResponse> search() {
        String sql = "SELECT id, name, date, time FROM reservation";
        return jdbcTemplate.query(sql, reservationRowMapper).stream()
                .map(ReservationResponse::new)
                .toList();
    }

    @PostMapping
    public ReservationCreated add(@RequestBody ReservationRequest reservationRequest) {
        String sql = "INSERT INTO reservation (name, date, time) VALUES (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, reservationRequest.name());
            ps.setString(2, reservationRequest.date());
            ps.setString(3, reservationRequest.time());
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
