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

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * @author <a href="mailto:john.scattergood@navis.com">John Scattergood</a> 3/2/2016
 */
public class EmailNotifier implements Notifier {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailNotifier.class);
    private static final String MAIL_SMTP_HOST = "mail.smtp.host";
    private static final String MAIL_SMTP_USER = "mail.smtp.user";
    private static final String MAIL_SMTP_AUTH_PASS = "mail.smtp.authPass";
    private static final String MAIL_SMTP_PORT = "mail.smtp.port";
    private static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
    private static final String MAIL_SMTP_PORT_VALUE = "587";
    private static final String MAIL_SMTP_AUTH_VALUE = "true";
    private static final String TRANSPORT_PROTOCOL = "smtps";

    private final String host;
    private final String user;
    private final String pass;
    private final NotificationEventRepository eventRepository;

    public EmailNotifier(String host, String user, String pass, NotificationEventRepository eventRepository) {
        this.host = host;
        this.user = user;
        this.pass = pass;
        this.eventRepository = eventRepository;
    }

    @Override
    public void send(Rotation rotation, Integer rotationInterval, Person person, Date startDate, Date endDate) {
        try {
            Properties props = System.getProperties();
            props.put(MAIL_SMTP_HOST, host);
            props.put(MAIL_SMTP_USER, user);
            props.put(MAIL_SMTP_AUTH_PASS, pass);
            props.put(MAIL_SMTP_PORT, MAIL_SMTP_PORT_VALUE);
            props.put(MAIL_SMTP_AUTH, MAIL_SMTP_AUTH_VALUE);

            Session session = Session.getInstance(props);

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(this.user));

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(person.getEmail()));
            message.setSubject(rotation.getName() + " Rotation Reminder");

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            message.setText("Dear " + person.getFirstName() + ",\r\nThis message is to remind you that you have " +
                    "an upcoming " + rotation.getName() + " rotation starting on " + df.format(startDate) + " and " +
                    "ending on " + df.format(endDate) + ".");

            Transport transport = session.getTransport(TRANSPORT_PROTOCOL);
            try {
                transport.connect(host, user, pass);
                transport.sendMessage(message, message.getAllRecipients());
            } finally {
                transport.close();
                NotificationEvent event = new NotificationEvent(rotation.getId(), rotationInterval,
                        person.getId(), person.getEmail());
                eventRepository.save(event);
            }
        } catch (MessagingException e) {
            LOGGER.error("Exception creating email", e);
        }
    }
}
