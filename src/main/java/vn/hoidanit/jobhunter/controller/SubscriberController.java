package vn.hoidanit.jobhunter.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Subscriber;
import vn.hoidanit.jobhunter.service.SubscriberService;
import vn.hoidanit.jobhunter.util.error.ExistedException;
import vn.hoidanit.jobhunter.util.error.IdNotFoundException;

@RestController
@RequestMapping("/api/v1/subscribers")
public class SubscriberController {
    private final SubscriberService subscriberService;


    public SubscriberController(SubscriberService service) {
        this.subscriberService = service;
    }

    @PostMapping
    public ResponseEntity<Subscriber> create(@RequestBody @Valid Subscriber subscriber) throws ExistedException {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.subscriberService.create(subscriber));
    }

    @PutMapping
    public ResponseEntity<Subscriber> update(@RequestBody Subscriber subscriber) throws IdNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(this.subscriberService.update(subscriber));
    }

}
