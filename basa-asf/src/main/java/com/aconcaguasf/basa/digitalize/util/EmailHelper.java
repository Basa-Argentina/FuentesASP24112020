package com.aconcaguasf.basa.digitalize.util;
/*
 *
 *  Copyright (c) 2017./ Aconcagua SF S.A.
 *  *
 *  Licensed under the Goycoolea inc License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://crossover.com/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *  @author 		: Hector Goycoolea
 *  @developer		: Hector Goycoolea
 *
 *  Date Changes
 *  02/09/17 03:53 Argentina Timezone
 *
 *  Notes
 *
 *  The Email helper uses basic java mail protocols and we are not using
 *  bean for configuration so we avoid the @Service("name") on the servlet-context
 *  this is a totally diff structure and this way we can manage a bit more the memmory
 *  of the iterations loop for sending massive mails.
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class EmailHelper {
    /**
     *
     */
    private static final Logger logger = LoggerFactory.getLogger(EmailHelper.class);
    /**
     *
     * @param msg
     * @param subject
     * @param to
     * @param cc
     * @param bcc
     */
    public void doSendEmail(String msg, String subject, String to, String cc, String bcc)
    {
        try {
            JavaMailSenderImpl sender;

            sender=new JavaMailSenderImpl();
            sender.setProtocol(JavaMailSenderImpl.DEFAULT_PROTOCOL);
            sender.setHost("smtp.gmail.com");
            sender.setUsername("aaa@gooo.com");
            sender.setPassword("aaaaaa");
            sender.setPort(25);

            Properties props = new Properties();
            props.put("mail.smtp.starttls.enable", "true");

            sender.setJavaMailProperties(props);

            MimeMessage message = sender.createMimeMessage();
            message.setContent(msg, "text/html; charset=utf-8");

            Date now=new Date(System.currentTimeMillis());
            message.setSubject(subject + " on " + now.toString());
            message.setSentDate(now);

            MimeMessageHelper helper = new MimeMessageHelper(message, false, "utf-8");
            helper.setFrom("noreply@goycooleainc.com");
            helper.setTo(to);
            helper.setCc(cc);
            helper.setBcc(bcc);

            sender.send(message);
        } catch (javax.mail.MessagingException ex) {

            logger.info("ERROR");
        }
    }
}
