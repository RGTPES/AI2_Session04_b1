package org.example.btth1_1.model.repository;

import org.example.btth1_1.model.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
