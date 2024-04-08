package com.restaurant.apirest.repository;

import com.restaurant.apirest.reservation.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository <Reservation, Integer> {
    @Query("SELECT FUNCTION('DATE', r.startDate) AS fecha, COUNT(*) AS reservas_realizadas FROM Reservation r GROUP BY FUNCTION('DATE', r.startDate) ORDER BY FUNCTION('DATE', r.startDate) ASC")
    List<Object[]> obtenerReservasPorDia();
    @Query(value = "SELECT fecha FROM (SELECT DATE_ADD('2024-01-01', " +
            "INTERVAL (t4*1000 + t3*100 + t2*10 + t1) DAY) AS fecha FROM" +
            " (SELECT 0 AS t1 UNION SELECT 1 UNION SELECT 2 UNION SELECT 3" +
            " UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION " +
            "SELECT 8 UNION SELECT 9) T1,(SELECT 0 AS t2 UNION SELECT 1 UNION SELECT " +
            "2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT " +
            "7 UNION SELECT 8 UNION SELECT 9) T2,(SELECT 0 AS t3 UNION SELECT 1 UNION SELECT" +
            " 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION " +
            "SELECT 8 UNION SELECT 9) T3,(SELECT 0 AS t4 UNION SELECT 1 UNION SELECT 2 UNION SELECT " +
            "3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) T4)" +
            " AS f WHERE f.fecha BETWEEN '2024-01-01' AND '2024-12-31' AND f.fecha NOT IN (SELECT DATE(r.start_date) FROM Reservation r)", nativeQuery = true)
    List<Object[]> obtenerDiasDisponibles();

}
