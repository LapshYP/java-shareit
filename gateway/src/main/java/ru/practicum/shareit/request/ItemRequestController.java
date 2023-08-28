package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/requests")
@Validated
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestClient requestClient;

    @PostMapping

    public ResponseEntity<Object> addItemRequest(
            @RequestHeader("X-Sharer-User-Id") int userId,
            @Valid @RequestBody RequestDTO requestDTO
    ) {
        return requestClient.addItemRequestService(userId, requestDTO);
    }

    @GetMapping
    public ResponseEntity<Object> requestsGet(@RequestHeader("X-Sharer-User-Id") int userId) {
        return requestClient.requestsGet(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> requestsAllGet(
            @RequestHeader("X-Sharer-User-Id") int userId,
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = "20") @Positive int size) {
        return requestClient.requestsAllGet(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(
            @RequestHeader("X-Sharer-User-Id") int userId,
            @PathVariable int requestId) {
        return requestClient.getRequestById(userId, requestId);
    }
}
