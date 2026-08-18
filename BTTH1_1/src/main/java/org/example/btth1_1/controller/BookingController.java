package org.example.btth1_1.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.btth1_1.model.dto.request.ChatRequest;
import org.example.btth1_1.model.service.BookingService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public String createBooking(@Valid @RequestBody ChatRequest request) {
        return bookingService.createBooking(request);
    }
}
