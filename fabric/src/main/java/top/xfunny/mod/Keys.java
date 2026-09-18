package top.xfunny.mod;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public interface Keys {
    String MOD_VERSION = "1.0.2-beta.3";
    String BUILD_TIME = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd-HHmmXX"));
    String API_URL = "https://api.modrinth.com/v2/project/yunzhu-transit-extension/version";
}
