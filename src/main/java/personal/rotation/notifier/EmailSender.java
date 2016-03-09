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

import javax.mail.MessagingException;
import java.util.Properties;

/**
 * @author <a href="https://github.com/jscattergood">John Scattergood</a> 3/8/2016
 */
@FunctionalInterface
public interface EmailSender {
    String MAIL_SMTP_HOST = "mail.smtp.host";
    String MAIL_SMTP_USER = "mail.smtp.user";
    String MAIL_SMTP_AUTH_PASS = "mail.smtp.authPass";
    String MAIL_SMTP_PORT = "mail.smtp.port";
    String MAIL_SMTP_AUTH = "mail.smtp.auth";
    String MAIL_SMTP_PORT_VALUE = "587"; // gmail default
    String MAIL_SMTP_AUTH_VALUE = "true";

    void send(String email, String subject, String body, Properties props, String protocol) throws MessagingException;
}
