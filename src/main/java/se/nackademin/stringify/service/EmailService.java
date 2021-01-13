package se.nackademin.stringify.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class EmailService {

    private SendGrid sg = new SendGrid(System.getenv("SG_API_KEY"));

    private void sendMail(Mail mail) {

        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            Logger.getLogger(EmailService.class.getName()).log(Level.WARNING, ex.getMessage());
        }
    }

    public void sendInvitationEmail(String sendTo, String invitedBy, String connectUrl) {
        Mail mail = new Mail();
        mail.setFrom(new Email("allanjamil_91@hotmail.com"));
        mail.setTemplateId("d-ee2942f110ba453b9f208384338c0824");

        Personalization personalization = new Personalization();
        personalization.addDynamicTemplateData("INVITED_BY", invitedBy);
        personalization.addDynamicTemplateData("MEETING_URL", connectUrl);
        personalization.addTo(new Email(sendTo));
        mail.addPersonalization(personalization);
        sendMail(mail);
    }
}