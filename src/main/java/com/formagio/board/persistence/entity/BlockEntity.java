package com.formagio.board.persistence.entity;

//import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public class BlockEntity {

    private Long id;
    private OffsetDateTime blockedAt;
    private String blockReason;
    private OffsetDateTime unblockedAt;
    private String unblockReason;
    private Long cardId;
//    private Card card; // Assuming you have a Card entity

    // Default constructor (required by JPA)
    public BlockEntity() {
    }

    // Constructor with required fields (you might want to adjust this based on your needs)
    public BlockEntity(String blockReason, String unblockReason, Long cardId) {
        this.blockReason = blockReason;
        this.unblockReason = unblockReason;
        this.cardId = cardId;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OffsetDateTime getBlockedAt() {
        return blockedAt;
    }

    public void setBlockedAt(OffsetDateTime blockedAt) {
        this.blockedAt = blockedAt;
    }

    public String getBlockReason() {
        return blockReason;
    }

    public void setBlockReason(String blockReason) {
        this.blockReason = blockReason;
    }

    public OffsetDateTime getUnblockedAt() {
        return unblockedAt;
    }

    public void setUnblockedAt(OffsetDateTime unblockedAt) {
        this.unblockedAt = unblockedAt;
    }

    public String getUnblockReason() {
        return unblockReason;
    }

    public void setUnblockReason(String unblockReason) {
        this.unblockReason = unblockReason;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

 /*   public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }*/

    @Override
    public String toString() {
        return "Block{" +
               "id=" + id +
               ", blockedAt=" + blockedAt +
               ", blockReason='" + blockReason + '\'' +
               ", unblockedAt=" + unblockedAt +
               ", unblockReason='" + unblockReason + '\'' +
               ", cardId=" + cardId +
               '}';
    }
}