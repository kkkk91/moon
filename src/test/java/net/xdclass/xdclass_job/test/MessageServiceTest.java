package net.xdclass.xdclass_job.test;

import net.xdclass.xdclass_job.domain.DelayedMessage;
import net.xdclass.xdclass_job.service.MessageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

@SpringBootTest
public class MessageServiceTest {

}
