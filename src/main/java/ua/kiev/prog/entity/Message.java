package ua.kiev.prog.entity;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
public class Message {
	private Date date = new Date();
	private String from;
	private String to;
	private String text;

	public Message(String from, String text) {
		this.from = from;
		this.text = text;
	}

	@Override
	public String toString() {
		return new StringBuilder().append(date)
				.append(" | " + from).append(" to: ").append(to + " >>" + "\n")
				.append("- "+ text)
				.toString();
	}
}
