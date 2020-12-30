package se.nackademin.stringify.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.nackademin.stringify.domain.ChatSession;

import java.util.Optional;
import java.util.UUID;

public interface ChatSessionRepository extends JpaRepository<ChatSession, UUID> {

    Optional<ChatSession> findByGuid(UUID guid);
}
