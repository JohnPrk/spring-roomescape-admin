package roomescape.dto;

import roomescape.domain.Reservation;

import java.time.format.DateTimeFormatter;

public class ReservationResponse {
    private final Long id;
    private final String name;
    private final String date;
    private final ReservationTimeResponse time;

    public ReservationResponse(Reservation reservation) {
        this.id = reservation.getId();
        this.name = reservation.getName();
        this.date = reservation.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.time = new ReservationTimeResponse(reservation.getTime());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public ReservationTimeResponse getTime() {
        return time;
    }
}
