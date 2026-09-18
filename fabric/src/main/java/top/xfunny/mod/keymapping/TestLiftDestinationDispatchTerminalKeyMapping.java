package top.xfunny.mod.keymapping;

public class TestLiftDestinationDispatchTerminalKeyMapping {
    public String mapping(String screenId, double x, double hitY) {
        switch (screenId) {
            case "test_lift_destination_dispatch_terminal_key_mapping_home":
                if (x > 0.0625 * 1 && x < 0.0625 * 2 && hitY > 0.375 && hitY < 0.4375) {
                    return "number1";
                }
                if (x > 0.0625 * 3 && x < 0.0625 * 4 && hitY > 0.375 && hitY < 0.4375) {
                    return "number2";
                }
                if (x > 0.0625 * 5 && x < 0.0625 * 6 && hitY > 0.375 && hitY < 0.4375) {
                    return "number3";
                }
                if (x > 0.0625 * 1 && x < 0.0625 * 2 && hitY > 0.25 && hitY < 0.3125) {
                    return "number4";
                }
                if (x > 0.0625 * 3 && x < 0.0625 * 4 && hitY > 0.25 && hitY < 0.3125) {
                    return "number5";
                }
                if (x > 0.0625 * 5 && x < 0.0625 * 6 && hitY > 0.25 && hitY < 0.3125) {
                    return "number6";
                }
                if (x > 0.0625 * 1 && x < 0.0625 * 2 && hitY > 0.125 && hitY < 0.1875) {
                    return "number7";
                }
                if (x > 0.0625 * 3 && x < 0.0625 * 4 && hitY > 0.125 && hitY < 0.1875) {
                    return "number8";
                }
                if (x > 0.0625 * 5 && x < 0.0625 * 6 && hitY > 0.125 && hitY < 0.1875) {
                    return "number9";
                }
                if (x > 0.0625 * 7 && x < 0.0625 * 8 && hitY > 0.25 && hitY < 0.3125) {
                    return "number0";
                }

                if (x > 0.0625 * 7 && x < 0.0625 * 8 && hitY > 0.375 && hitY < 0.4375) {
                    return "clearNumber";
                }
                if (x > 0.0625 * 7 && x < 0.0625 * 8 && hitY > 0.125 && hitY < 0.1875) {
                    return "callLift";
                }
        }
        return "null";
    }
}
