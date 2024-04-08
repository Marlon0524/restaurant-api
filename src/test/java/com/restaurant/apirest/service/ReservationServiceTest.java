package com.restaurant.apirest.service;

import com.restaurant.apirest.repository.ReservationRepository;
import com.restaurant.apirest.reservation.Reservation;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    void createReservation_Success() {
        // Arrange
        Reservation reservation = new Reservation();
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        // Act
        Reservation createdReservation = reservationService.createReservation(reservation);

        // Assert
        assertEquals(reservation, createdReservation);
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void updateReservation_ExistingReservation_Success() {
        // Arrange
        Integer id = 1;
        Reservation existingReservation = new Reservation();
        existingReservation.setId(id);
        Reservation updateReservation = new Reservation();
        updateReservation.setName("Updated Name");
        when(reservationRepository.findById(id)).thenReturn(Optional.of(existingReservation));
        when(reservationRepository.save(existingReservation)).thenReturn(existingReservation);

        // Act
        Reservation updatedReservation = reservationService.updateReservation(id, updateReservation);

        // Assert
        assertEquals(updateReservation.getName(), updatedReservation.getName());
        verify(reservationRepository, times(1)).findById(id);
        verify(reservationRepository, times(1)).save(existingReservation);
    }

    @Test
    void getAllReservations_Success() {
        // Arrange
        List<Reservation> reservations = Arrays.asList(new Reservation(), new Reservation());
        when(reservationRepository.findAll()).thenReturn(reservations);

        // Act
        List<Reservation> retrievedReservations = reservationService.getAllReservations();

        // Assert
        assertEquals(reservations.size(), retrievedReservations.size());
        verify(reservationRepository, times(1)).findAll();
    }
}