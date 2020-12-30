package se.nackademin.stringify.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.nackademin.stringify.domain.Message;

import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

}
