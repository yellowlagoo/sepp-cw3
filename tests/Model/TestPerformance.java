package tests.Model;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import src.Model.Performance;
import src.Model.Event;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class TestPerformance {

    @Test
    public void testNotTicketedEvent() {

        // Arrange: mock Event
        Event event = mock(Event.class);
        when(event.isTicketed()).thenReturn(false);

        // Act: create Performance using mocked Event
        Performance performance = new Performance(
                1L,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(2),
                new ArrayList<>(),
                "London",
                100,
                false,
                false,
                50,
                10.0,
                event);

        // Assert: should return false because event is not ticketed
        assertFalse(performance.checkIfEventIsTicketed());
    }
}