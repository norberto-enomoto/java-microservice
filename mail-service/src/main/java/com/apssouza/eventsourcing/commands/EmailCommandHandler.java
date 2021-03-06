package com.apssouza.eventsourcing.commands;

import com.apssouza.eventsourcing.aggregates.EmailAggregate;
import com.apssouza.eventsourcing.services.EventSourcingService;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Email command service handler. Deal with the business requirements before
 * send the command
 *
 * @author apssouza
 */
@Service
@Transactional
public class EmailCommandHandler {

    @Autowired
    EventSourcingService eventSourcingService;

    public void create(EmailCreateCommand command) {
        UUID randomUUID = UUID.randomUUID();
        EmailAggregate emailAggregate = getByUUID(randomUUID);
        eventSourcingService.save(emailAggregate.create(command));
    }

    public void send(EmailSendCommand command) throws Exception {
        EmailAggregate emailAggregate = getByUUID(command.getUuid());
        emailAggregate.send(command);
        eventSourcingService.save(emailAggregate);
    }

    public void delivery(EmailDeliveryCommand command) throws Exception {
        EmailAggregate emailAggregate = getByUUID(command.getUuid());
        emailAggregate.delivery(command);
        eventSourcingService.save(emailAggregate);
    }

    public void delete(EmailDeleteCommand command) throws Exception {
        EmailAggregate emailAggregate = getByUUID(command.getUuid());
        emailAggregate.delete(command);
        eventSourcingService.save(emailAggregate);
    }

    public EmailAggregate getByUUID(UUID uuid) {
        return EmailAggregate.from(uuid, eventSourcingService.getRelatedEvents(uuid));
    }
}
