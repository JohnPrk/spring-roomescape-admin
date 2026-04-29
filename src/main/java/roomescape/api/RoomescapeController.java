package roomescape.api;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

import static roomescape.utils.DateTimeConverter.dateConverter;
import static roomescape.utils.DateTimeConverter.timeConverter;

@RestController
@RequestMapping("/reservations")
public class RoomescapeController {

    private final List<Reservation> reservations = new ArrayList<>();
    private final AtomicLong id = new AtomicLong(0);

    @GetMapping
    public List<ReservationResponse> search() {
        return reservations.stream()
                .map(ReservationResponse::new)
                .toList();
    }

    @PostMapping
    public ReservationCreated add(@RequestBody ReservationRequest reservationRequest) {
        reservations.add(new Reservation(id.incrementAndGet(), reservationRequest.name(), dateConverter(reservationRequest.date()), timeConverter(reservationRequest.time())));
        return new ReservationCreated(id.get());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        Reservation reservation = reservations.stream().filter(r -> Objects.equals(r.getId(), id)).findAny().orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));
        reservations.remove(reservation);
    }
}
