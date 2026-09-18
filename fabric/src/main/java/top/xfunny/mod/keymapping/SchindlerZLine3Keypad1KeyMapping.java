package top.xfunny.mod.keymapping;

public class SchindlerZLine3Keypad1KeyMapping {
    public String mapping(String screenId, double x, double hitY) {
        boolean number1 = x > 6.25 / 16 && x < 7.25 / 16 && hitY > 9.325 / 16 && hitY < 10.4 / 16;
        boolean number2 = x > 7.475 / 16 && x < 8.475 / 16 && hitY > 9.325 / 16 && hitY < 10.4 / 16;
        boolean number3 = x > 8.7 / 16 && x < 9.7 / 16 && hitY > 9.325 / 16 && hitY < 10.4 / 16;
        boolean number4 = x > 6.25 / 16 && x < 7.25 / 16 && hitY > 7.625 / 16 && hitY < 8.7 / 16;
        boolean number5 = x > 7.475 / 16 && x < 8.475 / 16 && hitY > 7.625 / 16 && hitY < 8.7 / 16;
        boolean number6 = x > 8.7 / 16 && x < 9.7 / 16 && hitY > 7.625 / 16 && hitY < 8.7 / 16;
        boolean number7 = x > 6.25 / 16 && x < 7.25 / 16 && hitY > 6.0 / 16 && hitY < 7.075 / 16;
        boolean number8 = x > 7.475 / 16 && x < 8.475 / 16 && hitY > 6.0 / 16 && hitY < 7.075 / 16;
        boolean number9 = x > 8.7 / 16 && x < 9.7 / 16 && hitY > 6.0 / 16 && hitY < 7.075 / 16;
        boolean star = x > 6.25 / 16 && x < 7.25 / 16 && hitY > 4.4 / 16 && hitY < 5.075 / 16;
        boolean number0 = x > 7.475 / 16 && x < 8.475 / 16 && hitY > 4.4 / 16 && hitY < 5.075 / 16;
        boolean basement = x > 8.7 / 16 && x < 9.7 / 16 && hitY > 4.4 / 16 && hitY < 5.075 / 16;
        boolean accessibility = x > 6.25 / 16 && x < 9.7 / 16 && hitY > 2.2 / 16 && hitY < 3.275 / 16;

        if (number1) {
            return "number1";
        } else if (number2) {
            return "number2";
        } else if (number3) {
            return "number3";
        } else if (number4) {
            return "number4";
        } else if (number5) {
            return "number5";
        } else if (number6) {
            return "number6";
        } else if (number7) {
            return "number7";
        } else if (number8) {
            return "number8";
        } else if (number9) {
            return "number9";
        } else if (star) {
            return "lobby";
        } else if (number0) {
            return "number0";
        } else if (basement) {
            return "basement";
        } else if (accessibility) {
            return "accessibility";
        }
        return "null";
    }
}
