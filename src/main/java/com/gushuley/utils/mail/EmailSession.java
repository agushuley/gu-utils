package com.gushuley.utils.mail;

import java.io.*;
import java.util.*;

import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;

import com.gushuley.utils.Tools;
import com.gushuley.utils.mail.EmailConfig.SmtpTransports;

public class EmailSession {
	private final Session mailSession;

	public EmailSession(final EmailConfig config) throws MessagingException {
		final Properties p = new Properties();
		p.put("mail.smtp.host", config.getSmtpHost());
		
		final int defaultPort;
		if (SmtpTransports.SMTP == config.getSmtpTransport() || config.getSmtpTransport() == null) {
			p.put("mail.transport.protocol", "smtp");
			defaultPort = 25;
		} else if (SmtpTransports.SSL == config.getSmtpTransport()) {
			p.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			p.put("mail.smtp.ssl.enable", "true");
			defaultPort = 465;
		} else if (SmtpTransports.STARTTLS == config.getSmtpTransport()) {
			p.put("mail.smtp.starttls.enable", "true");
			defaultPort = 587;
		} else {
			throw new MessagingException("Invalid smtp transport: " + config.getSmtpTransport());
		}
		final int smtpPort;
		if ( config.getSmtpPort() == null ) {
			smtpPort = defaultPort;
		} else {
			smtpPort = config.getSmtpPort();
		}
		p.put("mail.smtp.port", smtpPort);
		if (SmtpTransports.SSL == config.getSmtpTransport()) {
			p.put("mail.smtp.socketFactory.port", smtpPort);
		}
		final Authenticator authenticator;
		if (config.isSmtpAuth()) {
			p.put("mail.smtp.auth", "true");
			authenticator = new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(config.getSmtpUser(), config.getSmtpPassword());
				}
			};
		} else {
			authenticator = null;
		}
		
		if (authenticator != null) {
			mailSession = Session.getInstance(p, authenticator);
		} else {
			mailSession = Session.getInstance(p);
		}
		if (config.isDebugMail()) {
			mailSession.setDebug(true);
		}
	}
	
	public void sendSmtpMail(
			String sender,
			String replyTo,
			String subject, 
			String body,
			String[] recipients,
			String[] cc,
			String[] bcc,
			File... attaches
	) throws AddressException, MessagingException {
		sendMailNonStandardBody(sender, replyTo, subject, body, "plain", recipients, cc,
				bcc, attaches);
	}

	public void sendMailNonStandardBody(String sender, String replyTo, String subject,
			String body, String bodyType, String[] recipients, String[] cc, String[] bcc,
			File... attaches
	)
	throws MessagingException, AddressException 
	{
		final Message msg = new MimeMessage(mailSession);

		msg.setFrom(new InternetAddress(sender));
		msg.setRecipients(Message.RecipientType.TO, parseAdresses(recipients));
		msg.setRecipients(Message.RecipientType.BCC, parseAdresses(bcc));
		msg.setRecipients(Message.RecipientType.CC, parseAdresses(cc));
		if (!Tools.isEmpty(replyTo)) {
			msg.setReplyTo(new InternetAddress[]{new InternetAddress(replyTo)});
		}
		msg.setSentDate(Calendar.getInstance().getTime());
		try {
			msg.setSubject(MimeUtility.encodeText(subject, "UTF-8", "B"));
		} catch (UnsupportedEncodingException e) {
			msg.setSubject(subject);
		}

		Multipart mailBody = new MimeMultipart();

		MimeBodyPart mainBody = new MimeBodyPart();
		mainBody.setText(body, "UTF-8", bodyType);
		mailBody.addBodyPart(mainBody);

		for (File attach : attaches) {
			FileDataSource fds = new FileDataSource(attach);
			MimeBodyPart mimeAttach = new MimeBodyPart();
			mimeAttach.setDataHandler(new DataHandler(fds));
			mimeAttach.setFileName(attach.getName());
			mailBody.addBodyPart(mimeAttach);
		}

		msg.setContent(mailBody);

		Transport.send(msg);
	}

	public void sendSmtpMailTemplatedHtml(String sender, String replyTo, String subject,
			Class<?> templateClass, String templateName,
			Map<String, String> values, String[] recipients, String[] cc,
			String[] bcc, File... attaches)
			throws AddressException, MessagingException 
	{
		final StringBuilder body = new StringBuilder();
		final InputStream templateStream = templateClass
				.getResourceAsStream(templateName);
		if (templateStream == null) {
			throw new MessagingException("Template " + templateName
					+ " not found");
		}
		try {
			try {
				final BufferedReader r = new BufferedReader(
						new InputStreamReader(templateStream, "UTF8"));
				try {
					String line;
					while ((line = r.readLine()) != null) {
						body.append(line);
						body.append('\n');
					}
				} finally {
					r.close();
				}
			} finally {
				templateStream.close();
			}
		} catch (IOException e) {
			throw new MessagingException("Error working with template " + templateName);
		}
		for (Map.Entry<String, String> e : values.entrySet()) {
			replaceAllInBuilder(body, "#" + e.getKey() + "#", e
					.getValue());
		}
		sendMailNonStandardBody(sender, replyTo, subject, body.toString(), "html",
				recipients, cc, bcc, attaches);
	}

	private static Address[] parseAdresses(String[] recipients)
			throws AddressException {
		Collection<Address> addresses = new ArrayList<Address>();
		if (recipients != null) {
			for (String rec : recipients) {
				for (Address a : InternetAddress.parse(rec)) {
					addresses.add(a);
				}
			}
		}
		return addresses.toArray(new Address[0]);
	}

	private static void replaceAllInBuilder(StringBuilder from, String source, String value) {
		if (source == null) {
			throw new NullPointerException("source");
		}
		int last = from.indexOf(source);
		while (last >= 0) {
			from.replace(last, last + source.length(), Tools.coalesce(value, ""));			
			last = from.indexOf(source, last + 1);
		};
	}
}
