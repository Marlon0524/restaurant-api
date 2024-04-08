package com.restaurant.apirest.controller;

import com.restaurant.apirest.repository.ReservationRepository;
import com.restaurant.apirest.reservation.ApiResponse;
import com.restaurant.apirest.reservation.Reservation;
import com.restaurant.apirest.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationControllerTest {

    @Mock
    private ReservationService reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationController reservationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createReservation() {
        Reservation reservation = new Reservation();
        reservation.setName("Marlon lopez");

        when(reservationService.createReservation(reservation)).thenReturn(reservation);

        ResponseEntity<Reservation> response = reservationController.createReservation(reservation);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(reservation, response.getBody());

        verify(reservationService, times(1)).createReservation(reservation);
    }

    @Test
    void updateReservation() {
        int id = 1;
        Reservation updateReservation = new Reservation();
        updateReservation.setName("modificar nombre");

        Reservation updatedReservation = new Reservation();
        updatedReservation.setId(id);
        updatedReservation.setName("modificar nombre");

        // Simular el comportamiento del servicio
        when(reservationService.updateReservation(id, updateReservation)).thenReturn(updatedReservation);

        // Ejecutar el método del controlador
        ResponseEntity<?> response = reservationController.updateReservation(id, updateReservation);

        // Verificar que la respuesta sea la esperada
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedReservation, ((ApiResponse<Reservation>) response.getBody()).getData());

        // Verificar que el método del servicio fue llamado una vez con los parámetros adecuados
        verify(reservationService, times(1)).updateReservation(id, updateReservation);
    }

    @Test
    void getAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(new Reservation());
        reservations.add(new Reservation());

        // Mock del servicio para devolver la lista de reservas
        when(reservationService.getAllReservations()).thenReturn(reservations);

        // Llamar al controlador
        ResponseEntity<ApiResponse<List<Reservation>>> response = reservationController.getAllReservations();

        // Verificar el código de estado OK
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Extraer la lista de reservas del cuerpo de la respuesta
        List<Reservation> responseBody = (List<Reservation>) response.getBody().getData();

        // Verificar que la lista de reservas devuelta sea igual a la lista esperada
        assertEquals(reservations.size(), responseBody.size());
        assertTrue(reservations.containsAll(responseBody));
        assertTrue(responseBody.containsAll(reservations));

        // Verificar que el método getAllReservations del servicio se haya llamado una vez
        verify(reservationService, times(1)).getAllReservations();
    }



    @Test
    void obtenerReservasPorDia_WhenNoReservations_ReturnsNotFound() {
        // Arrange
        when(reservationRepository.obtenerReservasPorDia()).thenReturn(new ArrayList<>());

        // Act
        ResponseEntity<ApiResponse<List<Map<String, Object>>>> response = reservationController.obtenerReservasPorDia();

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("error", response.getBody().getStatus());
        assertEquals("No hay reservas", response.getBody().getMessage());

        // Verificar si getData() es nulo antes de llamar a isEmpty()
        if (response.getBody().getData() != null) {
            assertTrue(response.getBody().getData().isEmpty());
        } else {
            assertTrue(true); // Si getData() es nulo, no hay datos, por lo tanto está vacío
        }
    }
    @Test
    void obtenerReservasPorDia_WhenReservationsExist_ReturnsReservations() {
        // Arrange
        List<Object[]> mockResultados = new ArrayList<>();
        Object[] reserva1 = { "2024-12-01", 3 };
        Object[] reserva2 = { "2024-12-02", 5 };
        mockResultados.add(reserva1);
        mockResultados.add(reserva2);

        when(reservationRepository.obtenerReservasPorDia()).thenReturn(mockResultados);

        // Act
        ResponseEntity<ApiResponse<List<Map<String, Object>>>> response = reservationController.obtenerReservasPorDia();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("success", response.getBody().getStatus());
        assertEquals("Reservas por dia obtenidas satisfactoriamente", response.getBody().getMessage());

        List<Map<String, Object>> expectedReservations = new ArrayList<>();
        Map<String, Object> reservaMap1 = new HashMap<>();
        reservaMap1.put("fecha", "2024-12-01");
        reservaMap1.put("reservas_realizadas", 3);
        expectedReservations.add(reservaMap1);

        Map<String, Object> reservaMap2 = new HashMap<>();
        reservaMap2.put("fecha", "2024-12-02");
        reservaMap2.put("reservas_realizadas", 5);
        expectedReservations.add(reservaMap2);

        assertEquals(expectedReservations, response.getBody().getData());
    }
    @Test
    void obtenerDiasDisponibles_WhenDiasDisponibles_ReturnsSuccess() {
        // Arrange
        List<Object[]> diasDisponibles = new ArrayList<>();
        Object[] dia1 = {"2024-12-01"};
        Object[] dia2 = {"2024-12-02"};
        diasDisponibles.add(dia1);
        diasDisponibles.add(dia2);
        when(reservationRepository.obtenerDiasDisponibles()).thenReturn(diasDisponibles);

        // Act
        ResponseEntity<ApiResponse<List<Map<String, Object>>>> response = reservationController.obtenerDiasDisponibles();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("success", response.getBody().getStatus());
        assertEquals("Días disponibles obtenidos satisfactoriamente", response.getBody().getMessage());
        assertEquals(2, response.getBody().getData().size());
    }
    @Test
    void obtenerDiasDisponibles_WhenNoDiasDisponibles_ReturnsNotFound() {
        // Arrange
        when(reservationRepository.obtenerDiasDisponibles()).thenReturn(new ArrayList<>());

        // Act
        ResponseEntity<ApiResponse<List<Map<String, Object>>>> response = reservationController.obtenerDiasDisponibles();

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("error", response.getBody().getStatus());
        assertEquals("No se encontraron días disponibles", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }
}