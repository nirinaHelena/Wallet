package org.example.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Transfer {
    private UUID transferId;
    private UUID senderAccountId;
    private UUID receiverAccountId;
    private Amount transferAmount;
    private LocalDateTime dateTime;

    // constructor without transfer id

    public Transfer(UUID senderAccountId, UUID receiverAccountId,
                    Amount transferAmount, LocalDateTime dateTime) {
        this.senderAccountId = senderAccountId;
        this.receiverAccountId = receiverAccountId;
        this.transferAmount = transferAmount;
        this.dateTime = dateTime;
    }
}
