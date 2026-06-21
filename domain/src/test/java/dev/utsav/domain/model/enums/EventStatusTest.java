package dev.utsav.domain.model.enums;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class EventStatusTest {
    

    @Nested
    @DisplayName("Legal transitions")
    class LegalTransitions{

        @Test
        @DisplayName("DRAFT can be published")
        void draftCanBePublished(){
            assertThat(EventStatus.DRAFT.canTransitionTo(EventStatus.PUBLISHED)).isTrue();
        }


        @Test
        @DisplayName("DRAFT can be cancelled")
        void draftCanBeCancelled(){
            assertThat(EventStatus.DRAFT.canTransitionTo(EventStatus.CANCELLED)).isTrue();
        }

        @Test
        @DisplayName("PUBLISHED can sell out")
        void publishedCanSellOut(){
            assertThat(EventStatus.PUBLISHED.canTransitionTo(EventStatus.SOLD_OUT)).isTrue();
        }
        

        @Test
        @DisplayName("PUBLISHED can complete")
        void publishedCanComplete(){
            assertThat(EventStatus.PUBLISHED.canTransitionTo(EventStatus.COMPLETED)).isTrue();
        }

        @Test
        @DisplayName("SOLD_OUT can complete")
        void soldOutCanComplete(){
            assertThat(EventStatus.SOLD_OUT.canTransitionTo(EventStatus.COMPLETED)).isTrue();
        }

    }

    @Nested
    @DisplayName("Illegal transitions")
    class IllegalTransitions {

        @Test
        @DisplayName("DRAFT cannot jump straight to SOLD_OUT")
        void draftCannotJumpToSoldOut() {
            assertThat(EventStatus.DRAFT.canTransitionTo(EventStatus.SOLD_OUT)).isFalse();
        }

        @Test
        @DisplayName("PUBLISHED cannot go back to DRAFT")
        void publishedCannotRevertToDraft() {
            assertThat(EventStatus.PUBLISHED.canTransitionTo(EventStatus.DRAFT)).isFalse();
        }

        @Test
        @DisplayName("a state cannot transition to itself")
        void stateCannotTransitionToItself() {
            assertThat(EventStatus.PUBLISHED.canTransitionTo(EventStatus.PUBLISHED)).isFalse();
        }
    }

    @Nested
    @DisplayName("Terminal states")
    class TerminalStates {

        @Test
        @DisplayName("CANCELLED is a dead end — no transitions allowed")
        void cancelledAllowsNothing() {
            // Loop over EVERY possible state and assert none is reachable
            for (EventStatus target : EventStatus.values()) {
                assertThat(EventStatus.CANCELLED.canTransitionTo(target)).isFalse();
            }
        }

        @Test
        @DisplayName("COMPLETED is a dead end — no transitions allowed")
        void completedAllowsNothing() {
            for (EventStatus target : EventStatus.values()) {
                assertThat(EventStatus.COMPLETED.canTransitionTo(target)).isFalse();
            }
        }
    }

}
