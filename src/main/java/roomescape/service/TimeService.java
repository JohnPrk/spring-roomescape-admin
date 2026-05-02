package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationTimeResponse;
import roomescape.repository.ReservationTimeRepository;

import java.util.List;

@Service
public class TimeService {

    private final ReservationTimeRepository timeRepository;

    public TimeService(ReservationTimeRepository timeRepository) {
        this.timeRepository = timeRepository;
    }

    public List<ReservationTimeResponse> getTimes() {
        return timeRepository.findAll().stream()
                .map(ReservationTimeResponse::new)
                .toList();
    }

    public ReservationTimeResponse addTime(ReservationTime time) {
        Long id = timeRepository.save(time);
        return new ReservationTimeResponse(id, time.getStartAt().toString());
    }

    public void deleteTime(Long id) {
        timeRepository.deleteById(id);
    }
}
