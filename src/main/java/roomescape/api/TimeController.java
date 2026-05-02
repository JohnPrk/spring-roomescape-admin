package roomescape.api;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;
import roomescape.facade.ReservationFacade;
import roomescape.service.TimeService;
import roomescape.utils.DateTimeConverter;

import java.util.List;

@RestController
@RequestMapping("/times")
public class TimeController {

    private final TimeService timeService;
    private final ReservationFacade reservationFacade;

    public TimeController(TimeService timeService, ReservationFacade reservationFacade) {
        this.timeService = timeService;
        this.reservationFacade = reservationFacade;
    }

    @GetMapping
    public List<ReservationTimeResponse> search() {
        return timeService.getTimes();
    }

    @PostMapping
    public ReservationTimeResponse add(@RequestBody ReservationTimeRequest request) {
        ReservationTime time = new ReservationTime(null, DateTimeConverter.timeConverter(request.startAt()));
        return timeService.addTime(time);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        reservationFacade.deleteTime(id);
    }
}
