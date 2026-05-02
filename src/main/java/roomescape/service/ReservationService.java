package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationCreated;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.repository.ReservationRepository;
import roomescape.utils.DateTimeConverter;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<ReservationResponse> getReservations() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponse::new)
                .toList();
    }

    public ReservationCreated addReservation(ReservationRequest request) {
        ReservationTime time = new ReservationTime(request.timeId(), null);
        Reservation reservation = new Reservation(
                null,
                request.name(),
                DateTimeConverter.dateConverter(request.date()),
                time
        );

        Long id = reservationRepository.save(reservation);
        return new ReservationCreated(id);
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }
}
