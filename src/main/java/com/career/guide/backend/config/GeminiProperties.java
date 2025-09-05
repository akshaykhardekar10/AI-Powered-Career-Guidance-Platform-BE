package com.career.guide.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "gemini.api")
public class GeminiProperties {
	private String key;
	private String baseUrl = "https://generativelanguage.googleapis.com/v1beta";
	private String model = "models/gemini-1.5-pro";

	public String getKey() { return key; }
	public void setKey(String key) { this.key = key; }

	public String getBaseUrl() { return baseUrl; }
	public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }

	public String getModel() { return model; }
	public void setModel(String model) { this.model = model; }
}


