package com.gushuley.utils.mail;

public interface EmailConfig {
	public interface SmtpTransport {
	};
	
	public enum SmtpTransports implements SmtpTransport {
		SMTP, SSL, STARTTLS;

		public static SmtpTransport createInvalidTransport(final String name) {
			return new SmtpTransport() {
				@Override
				public String toString() {
					return "SMTP: " + name;
				}
			};
		}
	}
	


	/***
	 * If empty, will be selected by transport type
	 * @return
	 */
	Integer getSmtpPort();

	/***
	 * Should be specified
	 * @return
	 */
	String getSmtpHost();

	/***
	 * ssl, smtp, starttls
	 * @return
	 */
	SmtpTransport getSmtpTransport();

	boolean isSmtpAuth();

	String getSmtpUser();

	String getSmtpPassword();

	boolean isDebugMail();

}
