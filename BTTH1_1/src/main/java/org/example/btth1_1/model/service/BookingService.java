package org.example.btth1_1.model.service;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.example.btth1_1.model.dto.request.BookingRequestDTO;
import org.example.btth1_1.model.dto.request.ChatRequest;
import org.example.btth1_1.model.entity.Booking;
import org.example.btth1_1.model.repository.BookingRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class BookingService {
    private static final int MAX_MEMORY_MESSAGES = 10;

    private final List<String> memory = new CopyOnWriteArrayList<>();
    private final BookingRepository bookingRepository;
    private final ChatClient chatClient;
    private final Validator validator;
    private final BeanOutputConverter<BookingRequestDTO> outputConverter =
            new BeanOutputConverter<>(BookingRequestDTO.class);

    public BookingService(BookingRepository bookingRepository,
                          ChatClient.Builder chatClientBuilder,
                          Validator validator) {
        this.bookingRepository = bookingRepository;
        this.chatClient = chatClientBuilder.build();
        this.validator = validator;
    }

    public String createBooking(ChatRequest request) {
        String history = String.join("\n", memory);
        String aiResponse = chatClient.prompt()
                .system("""
                        Bạn là trợ lý lễ tân khách sạn, có nhiệm vụ ghi nhận yêu cầu đặt phòng.
                        Hôm nay là %s. Dựa vào lịch sử hội thoại và tin nhắn mới nhất, hãy trích xuất thông tin đặt phòng.
                        Ưu tiên thông tin trong tin nhắn mới nhất nếu khách thay đổi yêu cầu.
                        Không tự suy đoán thông tin chưa được cung cấp; trường còn thiếu phải để null.
                        Ngày dùng định dạng yyyy-MM-dd. Chỉ trả về dữ liệu đúng định dạng, không giải thích.

                        Lịch sử hội thoại:
                        %s

                        Định dạng trả về:
                        %s
                        """.formatted(LocalDate.now(), history, outputConverter.getFormat()))
                .user(request.getMessage())
                .options(ChatOptions.builder().temperature(0.0))
                .call()
                .content();

        addToMemory(request.getMessage());
        BookingRequestDTO bookingRequest = outputConverter.convert(aiResponse);
        validate(bookingRequest);
        save(bookingRequest);
        memory.clear();
        return "Dạ, em đã ghi nhận đặt phòng của anh/chị thành công. Cảm ơn anh/chị ạ!";
    }

    private void addToMemory(String message) {
        if (memory.size() >= MAX_MEMORY_MESSAGES) {
            memory.remove(0);
        }
        memory.add(message);
    }

    private void validate(BookingRequestDTO request) {
        var violations = validator.validate(request);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        if (!request.getCheckOutDate().isAfter(request.getCheckInDate())) {
            throw new IllegalArgumentException("ngày trả phòng phải sau ngày nhận phòng");
        }
    }

    private void save(BookingRequestDTO request) {
        bookingRepository.save(Booking.builder()
                .customerName(request.getCustomerName())
                .numberOfPeople(request.getNumberOfPeople())
                .checkInDate(request.getCheckInDate())
                .checkOutDate(request.getCheckOutDate())
                .budget(request.getBudget())
                .createdDate(LocalDateTime.now())
                .build());
    }
}
