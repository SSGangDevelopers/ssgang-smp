package io.github.ssgangdevelopers.logs;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.message.Message;

public class JDALogFormatter implements Filter {

	public static void register() {
		new JDALogFormatter();
	}

	private final Logger logger;

	private JDALogFormatter() {
		logger = (Logger) LogManager.getRootLogger();
		logger.addFilter(this);
	}

	private Result mainHandle(String logger, Level level, String message, Throwable throwable) {
		if (!logger.startsWith("net.dv8tion.jda")) return Result.NEUTRAL;
		this.logger.log(level, "[JDA] " + message, throwable);
		return Result.DENY;
	}

	@Override
	public Result getOnMismatch() {
		return Result.NEUTRAL;
	}

	@Override
	public Result getOnMatch() {
		return Result.NEUTRAL;
	}

	@Override
	public Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
		return mainHandle(logger.getName(), level, msg, null);
	}

	@Override
	public Result filter(Logger logger, Level level, Marker marker, String message, Object p0) {
		return mainHandle(logger.getName(), level, message, null);
	}

	@Override
	public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1) {
		return mainHandle(logger.getName(), level, message, null);
	}

	@Override
	public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2) {
		return mainHandle(logger.getName(), level, message, null);
	}

	@Override
	public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
		return mainHandle(logger.getName(), level, message, null);
	}

	@Override
	public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
		return mainHandle(logger.getName(), level, message, null);
	}

	@Override
	public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
		return mainHandle(logger.getName(), level, message, null);
	}

	@Override
	public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
		return mainHandle(logger.getName(), level, message, null);
	}

	@Override
	public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
		return mainHandle(logger.getName(), level, message, null);
	}

	@Override
	public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
		return mainHandle(logger.getName(), level, message, null);
	}

	@Override
	public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
		return mainHandle(logger.getName(), level, message, null);
	}

	@Override
	public Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
		return mainHandle(logger.getName(), level, msg.toString(), t);
	}

	@Override
	public Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
		return mainHandle(logger.getName(), level, msg.getFormattedMessage(), t);
	}

	@Override
	public Result filter(LogEvent event) {
		return mainHandle(
						event.getLoggerName(),
						event.getLevel(),
						event.getMessage()
										.getFormattedMessage(),
						event.getThrown());
	}

	@Override
	public State getState() {
		return null;
	}

	@Override
	public void initialize() {

	}

	@Override
	public void start() {

	}

	@Override
	public void stop() {

	}

	@Override
	public boolean isStarted() {
		return true;
	}

	@Override
	public boolean isStopped() {
		return false;
	}
}
