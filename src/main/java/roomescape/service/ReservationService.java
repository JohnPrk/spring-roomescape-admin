package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.dto.ReservationCreateResponse;
import roomescape.dto.ReservationResponse;
import roomescape.repository.ReservationRepository;

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

    public ReservationCreateResponse addReservation(Reservation reservation) {
        Long id = reservationRepository.save(reservation);
        return new ReservationCreateResponse(id);
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }
}
