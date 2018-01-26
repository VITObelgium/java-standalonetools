package be.vito.rma.standalonetools.api;

/**
 * @author (c) 2018 Stijn.VanLooy@vito.be
 *
 */
public interface Notifier {

	/**
	 * Logs the given message and cause to the error log
	 * and sends an email notification with given subject and message,
	 * including the cause stack trace
	 * @param subject
	 * @param message
	 * @param cause
	 */
	public void notifyAdmin(final String subject, final String message, final Throwable cause);

	/**
	 * Logs the given message to the error log
	 * and sends an email notification with given subject and message
	 * @param subject
	 * @param message
	 */
	public void notifyAdmin(final String subject, final String message);

	/**
	 * Logs the given message and cause to the error log
	 * and sends an email notification with given message,
	 * including the cause stack trace
	 * and sends
	 * @param message
	 * @param cause
	 */
	public void notifyAdmin(final String message, final Throwable cause);

	/**
	 * Logs the given message to the error log
	 * and sends an email notification with given message
	 * @param message
	 */
	public void notifyAdmin(final String message);

}
