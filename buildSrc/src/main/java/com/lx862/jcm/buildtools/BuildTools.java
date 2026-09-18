package com.lx862.jcm.buildtools;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;

import java.net.URL;
import java.nio.charset.StandardCharsets;

public class BuildTools {
	public final String minecraftVersion;
	public final int javaLanguageVersion;
	public final Dependencies dependencies;

	public BuildTools(String minecraftVersion) {
		this.minecraftVersion = minecraftVersion;
		javaLanguageVersion = switch (minecraftVersion) {
			case "1.21.11", "1.21.4", "1.21.3", "1.21.2", "1.21.1", "1.21" -> 21;
			case "1.20.6", "1.20.5", "1.20.4", "1.20.3", "1.20.2", "1.20.1", "1.20" -> 17;
			default -> 17;
		};
		if ("1.21.11".equals(minecraftVersion)) {
			dependencies = new Dependencies(
					"0.19.3",
					"0.141.6+1.21.11",
					"17.0.1-beta.1"
			);
		} else if ("1.21.4".equals(minecraftVersion)) {
			dependencies = new Dependencies(
					"0.19.3",
					"0.119.4+1.21.4",
					"13.0.3"
			);
		} else {
			JsonObject json = getJson("https://files.ziyuesinicization.site/tjmetro/dependencies.json").getAsJsonObject();
			JsonObject versionSpecificJson = json.get(minecraftVersion).getAsJsonObject();
			dependencies = new Dependencies(
					json.get("fabric-loader").getAsString(),
					versionSpecificJson.get("fabric-api").getAsString(),
					versionSpecificJson.get("modmenu").getAsString()
			);
		}
		System.out.println(dependencies);
	}

	public record Dependencies(String fabricLoader, String fabricApi, String modMenu) {
		@Override
		public String toString() {
			return "Dependencies[fabricLoader=" + fabricLoader + ", fabricApi=" + fabricApi + ", modMenu=" + modMenu + "]";
		}
	}

	private static JsonElement getJson(String url) {
		for (int i = 0; i < 5; i++) {
			try {
				return JsonParser.parseString(IOUtils.toString(new URL(url), StandardCharsets.UTF_8));
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new JsonObject();
	}
}
