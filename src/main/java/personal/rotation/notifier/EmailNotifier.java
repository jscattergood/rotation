/*
 * Copyright 2016 John Scattergood
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package personal.rotation.notifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import personal.rotation.domain.NotificationEvent;
import personal.rotation.domain.Person;
import personal.rotation.domain.Rotation;
import personal.rotation.repository.NotificationEventRepository;

import javax.mail.MessagingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * @author <a href="https://github.com/jscattergood">John Scattergood</a> 3/2/2016
 */
public class EmailNotifier implements Notifier {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailNotifier.class);
    private static final String DEFAULT_PROTOCOL = "smtps";
    private static final String REMINDER_SUBJECT = "%s Rotation Reminder";
    private static final String REMINDER = "Dear %s," +
            "\r\n\r\n" +
            "This message is to remind you that you have an upcoming %s rotation starting on %s and ending on %s." +
            "\r\n\r\n" +
            "If you have any questions please contact your rotation administrator";

    private final String host;
    private final String user;
    private final String pass;
    private final NotificationEventRepository eventRepository;
    private final EmailSender emailSender;
    private String smtpPort = EmailSender.MAIL_SMTP_PORT_VALUE;
    private String transportProtocol = DEFAULT_PROTOCOL;

    public EmailNotifier(String host, String user, String pass,
                         NotificationEventRepository eventRepository, EmailSender emailSender) {
        this.host = host;
        this.user = user;
        this.pass = pass;
        this.eventRepository = eventRepository;
        this.emailSender = emailSender;
    }

    public void setSmtpPort(String smtpPort) {
        this.smtpPort = smtpPort;
    }

    public void setTransportProtocol(String transportProtocol) {
        this.transportProtocol = transportProtocol;
    }

    @Override
    public void send(Rotation rotation, Long rotationInterval, Person person, Date startDate, Date endDate) {
        try {
            Properties props = System.getProperties();
            props.put(EmailSender.MAIL_SMTP_HOST, host);
            props.put(EmailSender.MAIL_SMTP_USER, user);
            props.put(EmailSender.MAIL_SMTP_AUTH_PASS, pass);
            props.put(EmailSender.MAIL_SMTP_PORT, smtpPort);
            props.put(EmailSender.MAIL_SMTP_AUTH, EmailSender.MAIL_SMTP_AUTH_VALUE);

            String rotationName = rotation.getName();
            String subject = String.format(REMINDER_SUBJECT, rotationName);

            String firstName = person.getFirstName();
            String email = person.getEmail();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String body = String.format(REMINDER, firstName, rotationName, df.format(startDate), df.format(endDate));

            emailSender.send(email, subject, body, props, transportProtocol);

            NotificationEvent event = new NotificationEvent(rotation.getId(), rotationInterval, person.getId(), email);
            eventRepository.save(event);
        } catch (MessagingException e) {
            LOGGER.error("Exception creating email", e);
        }
    }
}
