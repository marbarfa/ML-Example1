package com.ml.android.melitraining.net.robospice;

import org.codehaus.jackson.map.MapperConfig;
import org.codehaus.jackson.map.introspect.AnnotatedField;
import org.codehaus.jackson.map.introspect.AnnotatedMethod;

public class UnderscorePropertyNamingStrategy extends org.codehaus.jackson.map.PropertyNamingStrategy {

	@Override
	public String nameForField(MapperConfig<?> config, AnnotatedField field, String defaultName) {
		return convert(defaultName);
	}

	@Override
	public String nameForGetterMethod(MapperConfig<?> config, AnnotatedMethod method, String defaultName) {
		return convert(defaultName);
	}

	@Override
	public String nameForSetterMethod(MapperConfig<?> config, AnnotatedMethod method, String defaultName) {
		return convert(defaultName);
	}

	private String convert(String input) {
		// easy: replace capital letters with underscore, lower-cases equivalent
		StringBuilder result = new StringBuilder();
		for (int i = 0, len = input.length(); i < len; ++i) {
			char c = input.charAt(i);
			if (Character.isUpperCase(c)) {
				result.append('_');
				c = Character.toLowerCase(c);
			}
			result.append(c);
		}
		return result.toString();
	}
}