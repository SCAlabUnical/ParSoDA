package it.unical.scalab.parsoda.common;

public class ColorPalette {

	private static final String[] colors = { "d11141", "00b159", "00aedb", "f37735", "ffc425", "00aedb", "a200ff", "f47835", "d41243", "8ec127" };

	public static String getColor(int id) {
		return "#" + colors[id % colors.length];
	}
}
