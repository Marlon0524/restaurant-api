package com.restaurant.apirest.controller;
import com.restaurant.apirest.repository.ReservationRepository;
import com.restaurant.apirest.reservation.ApiResponse;
import com.restaurant.apirest.reservation.Reservation;
import com.restaurant.apirest.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.logging.Logger;

@RestController
@RequestMapping ("api/restaurant")
@RequiredArgsConstructor
@Validated
public class ReservationController {
    private final ReservationService reservationService;
    private final ReservationRepository reservationRepository;
    private static final Logger logger = Logger.getLogger(ReservationController.class.getName());
@PostMapping
    public ResponseEntity<Reservation> createReservation (@RequestBody Reservation reservation){
    logger.info("Llamada al endpoint POST /restaurant recibida.");
         Reservation newReservation = reservationService.createReservation(reservation);
         ApiResponse<Reservation> response = new ApiResponse<>(newReservation, "success", "Reserva creada");
         return ResponseEntity.status(HttpStatus.CREATED).body(response.getData());
}
@PutMapping
public ResponseEntity<ApiResponse<Reservation>> updateReservation(@PathVariable Integer id, @RequestBody Reservation updateReservation) {
    logger.info("Llamada al endpoint PUT /restaurant recibida.");
    Reservation updatedReservation = reservationService.updateReservation(id, updateReservation);
    if (updatedReservation != null) {
        ApiResponse<Reservation> response = new ApiResponse<>(updatedReservation, "success", "Reserva modificada satisfactoriamente");
        return ResponseEntity.ok(response);
    } else {
        ApiResponse<Reservation> response = new ApiResponse<>(null, "error", "Reserva no encontrada");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}

@GetMapping
public ResponseEntity<ApiResponse<List<Reservation>>> getAllReservations() {
    logger.info("Llamada al endpoint GET /restaurant recibida.");
    List<Reservation> reservations = reservationService.getAllReservations();
    ApiResponse<List<Reservation>> response;
    if (!reservations.isEmpty()) {
        response = new ApiResponse<>(reservations, "success", "Reservas obtenidas satisfactoriamente");
        return ResponseEntity.ok(response);
    } else {
        response = new ApiResponse<>(null, "error", "Reservas no encontradas");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}

    @GetMapping("/por-dia")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> obtenerReservasPorDia() {
        logger.info("Llamada al endpoint GET /restaurant recibida.");
        List<Object[]> resultados = reservationRepository.obtenerReservasPorDia();
        List<Map<String, Object>> respuesta = new ArrayList<>();

        for (Object[] fila : resultados) {
            Map<String, Object> registro = new HashMap<>();
            registro.put("fecha", fila[0]);
            registro.put("reservas_realizadas", fila[1]);
            respuesta.add(registro);
        }

        if (!respuesta.isEmpty()) {
            ApiResponse<List<Map<String, Object>>> response = new ApiResponse<>(respuesta, "success", "Reservas por dia obtenidas satisfactoriamente");
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<List<Map<String, Object>>> response = new ApiResponse<>(null, "error", "No hay reservas");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    @GetMapping("/dias-disponibles")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> obtenerDiasDisponibles() {
        logger.info("Llamada al endpoint GET /restaurant recibida.");
        List<Object[]> diasDisponibles = reservationRepository.obtenerDiasDisponibles();
        List<Map<String, Object>> respuesta = new ArrayList<>();

        for (Object[] fila : diasDisponibles) {
            Map<String, Object> dia = new HashMap<>();
            dia.put("fecha", fila[0]);
            // Agregar más información si es necesario
            respuesta.add(dia);
        }

        if (!respuesta.isEmpty()) {
            ApiResponse<List<Map<String, Object>>> response = new ApiResponse<>(respuesta, "success", "Días disponibles obtenidos satisfactoriamente");
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<List<Map<String, Object>>> response = new ApiResponse<>(null, "error", "No se encontraron días disponibles");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
