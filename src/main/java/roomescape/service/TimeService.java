package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;
import roomescape.utils.DateTimeConverter;

import java.util.List;

@Service
public class TimeService {

    private final ReservationTimeDao timeDao;

    public TimeService(ReservationTimeDao timeDao) {
        this.timeDao = timeDao;
    }

    public List<ReservationTimeResponse> getTimes() {
        return timeDao.findAll().stream()
                .map(ReservationTimeResponse::new)
                .toList();
    }

    public ReservationTimeResponse addTime(ReservationTimeRequest request) {
        ReservationTime time = new ReservationTime(null, DateTimeConverter.timeConverter(request.startAt()));
        Long id = timeDao.save(time);
        return new ReservationTimeResponse(id, request.startAt());
    }

    public void deleteTime(Long id) {
        timeDao.deleteById(id);
    }
}
