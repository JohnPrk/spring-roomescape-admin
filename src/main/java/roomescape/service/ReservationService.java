package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationCreated;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.utils.DateTimeConverter;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;

    public ReservationService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public List<ReservationResponse> getReservations() {
        return reservationDao.findAll().stream()
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

        Long id = reservationDao.save(reservation);
        return new ReservationCreated(id);
    }

    public void deleteReservation(Long id) {
        reservationDao.deleteById(id);
    }
}
