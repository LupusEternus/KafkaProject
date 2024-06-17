package com.wilk.emailnotificationmicroservice.repository;

import com.wilk.emailnotificationmicroservice.entity.ProcessedEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessedEventRepository extends JpaRepository<ProcessedEventEntity,Long> {

    ProcessedEventEntity findByMsgId(String msgId);
}
