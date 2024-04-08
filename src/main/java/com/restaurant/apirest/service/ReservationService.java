package com.restaurant.apirest.service;

import com.restaurant.apirest.repository.ReservationRepository;
import com.restaurant.apirest.reservation.Reservation;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {
 private final ReservationRepository reservationRepository;

 public Reservation createReservation (Reservation reservation){
        reservationRepository.save(reservation);
        return reservation;
 }

public Reservation updateReservation (Integer id, Reservation updateReservation){
 Reservation existingReservation = reservationRepository.findById(id)
         .orElseThrow(()->new EntityNotFoundException("Reservación no encontrada con el id" + id));

            existingReservation.setName(updateReservation.getName());
            existingReservation.setDocument(updateReservation.getDocument());
            existingReservation.setQuantity(updateReservation.getQuantity());
            existingReservation.setObservations(updateReservation.getObservations());

            return reservationRepository.save(existingReservation);
}

public List<Reservation> getAllReservations(){
     return reservationRepository.findAll();
}

}
