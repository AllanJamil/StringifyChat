package se.nackademin.stringify.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.nackademin.stringify.domain.Profile;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, UUID> {

    boolean existsByGuid(UUID guid);

    long deleteByGuidAndChatSession_Id(UUID guid, UUID chatSession_id);
}
