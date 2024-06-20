package henry.hotel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

import henry.hotel.entity.Reservation;
import henry.hotel.repository.ReservationRep;
import henry.hotel.services.ReservationServiceImpl;
import henry.hotel.services.UserService;
import henry.hotel.temp.CurrentReservation;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ReservationServiceUnitTests {

	private final static int USER_ID = 12;
	private final static int RESERVATION_ID = 6544;
	
	@Mock
    private UserService userService;
	
	@Mock
	private ReservationRep reservationRepository;

	@InjectMocks
	private ReservationServiceImpl service;
	
	@Test
	void serviceLoads() {
		assertThat(this.service).isNotNull();
	}

	@Test
	void getReservationForLoggedUserById() {		
		final Reservation reservation = build(RESERVATION_ID);
		
		Mockito.when(reservationRepository.findById(RESERVATION_ID)).thenReturn(reservation);
		
		final Reservation result = this.service.getReservationForLoggedUserById(RESERVATION_ID);

		assertEquals(reservation, result);
	}

	@Test
	void getReservationsForLoggedUser() {
		final Collection<Reservation> reservations = new LinkedList<>();
		for (int r=0; r<10; r++) {
			reservations.add(build(RESERVATION_ID + r));			
		}
		
		Mockito.when(userService.getLoggedUserId()).thenReturn(RESERVATION_ID);
		Mockito.when(reservationRepository.findAllByUserId(RESERVATION_ID)).thenReturn(reservations);
		
		final Collection<Reservation> result = this.service.getReservationsForLoggedUser();

		assertEquals(reservations, result);
	}
		
	@Test
	void saveOrUpdateReservation() {
        Mockito.when(this.userService.getLoggedUserId()).thenReturn(USER_ID);
        final ArgumentCaptor<Reservation> argCaptor = ArgumentCaptor.forClass(Reservation.class);

		final CurrentReservation newReservation = build(RESERVATION_ID, USER_ID);
		this.service.saveOrUpdateReservation(newReservation);

		verify(this.reservationRepository).save(argCaptor.capture());

		assertEquals(newReservation.getId(), argCaptor.getValue().getId());
		assertEquals(newReservation.getRoom(), argCaptor.getValue().getRoom());
		assertEquals(newReservation.getPrice(), argCaptor.getValue().getPrice());
		assertEquals(newReservation.getRooms(), argCaptor.getValue().getRooms());
		assertEquals(newReservation.getPersons(), argCaptor.getValue().getPersons());
		assertEquals(newReservation.getChildren(), argCaptor.getValue().getChildren());
		assertEquals(newReservation.getOpenBuffet(), argCaptor.getValue().getOpenBuffet());
		assertEquals(newReservation.getArrivalDate(), argCaptor.getValue().getArrivalDate());
		assertEquals(newReservation.getStayPeriod(), argCaptor.getValue().getStayDays());
	}

	@Test
	void deleteReservation() {
        final ArgumentCaptor<Integer> argCaptor = ArgumentCaptor.forClass(Integer.class);
        
		this.service.deleteReservation(RESERVATION_ID);

        verify(this.reservationRepository).deleteById(argCaptor.capture());

        assertEquals(RESERVATION_ID, argCaptor.getValue());
	}
	
	@Test
	void reservationToCurrentReservationById() {
		final Reservation reservation = build(RESERVATION_ID);
		
		Mockito.when(reservationRepository.findById(RESERVATION_ID)).thenReturn(reservation);
		
		final CurrentReservation result = this.service.reservationToCurrentReservationById(RESERVATION_ID);

		assertEquals(reservation.getId(), result.getId());
		assertEquals(reservation.getRoom(), result.getRoom());
		assertEquals(reservation.getPrice(), result.getPrice());
		assertEquals(reservation.getRooms(), result.getRooms());
		assertEquals(reservation.getPersons(), result.getPersons());
		assertEquals(reservation.getChildren(), result.getChildren());
		assertEquals(reservation.getOpenBuffet(), result.getOpenBuffet());
		assertEquals(reservation.getArrivalDate(), result.getArrivalDate());
		assertEquals(reservation.getStayDays(), result.getStayPeriod());
	}

	CurrentReservation build(final int id, final int usertId) {
		final int stayPeriod = 1;
		final String room = "rooms";
		final int price = 1245;
		final int rooms = 2;
		final int persons = 2;
		final int children = 1;
		final String openBuffet = "open buffet";
		final Date arrivalDate = new Date();
		
		return new CurrentReservation(id, stayPeriod, room, price, rooms, persons, children, openBuffet, arrivalDate, usertId);
	}

	Reservation build(final int usertId) {
		final String room = "rooms";
		final int price = 1245;
		final int rooms = 2;
		final int persons = 2;
		final int children = 1;
		final String openBuffet = "open buffet";
		final Date arrivalDate = new Date();
		final int stayDays = 1;
		
		return new Reservation(room, price, rooms, persons, children, openBuffet, arrivalDate, stayDays, usertId);
	}

}
