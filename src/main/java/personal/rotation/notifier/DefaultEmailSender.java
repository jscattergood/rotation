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

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class DefaultEmailSender implements EmailSender {
    @Override
    public void send(String email, String subject, String body, Properties props, String protocol) throws MessagingException {
        String user = (String) props.get(MAIL_SMTP_USER);
        String pass = (String) props.get(MAIL_SMTP_AUTH_PASS);
        String host = (String) props.get(MAIL_SMTP_HOST);

        Session session = Session.getInstance(props);
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(user));

        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
        message.setSubject(subject);

        message.setText(body);

        Transport transport = session.getTransport(protocol);
        try {
            transport.connect(host, user, pass);
            transport.sendMessage(message, message.getAllRecipients());
        } finally {
            transport.close();
        }
    }
}