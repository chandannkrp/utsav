package dev.utsav.domain.model.enums;


public enum EventStatus {

    DRAFT,
    PUBLISHED,
    SOLD_OUT,
    CANCELLED,
    COMPLETED;

    public boolean canTransitionTo(EventStatus target){
        return switch(this){
            case DRAFT -> target == PUBLISHED || target == CANCELLED;
            case PUBLISHED -> target == SOLD_OUT || target == CANCELLED || target == COMPLETED;
            case SOLD_OUT -> target == CANCELLED || target == COMPLETED;
            case CANCELLED, COMPLETED -> false;
        };
    }

    
}
