package ToDo.utils;

import java.util.regex.Pattern;

public class StringUtils {

    public static boolean isInvalidText(String name) {
        if (name == null) {
            return true;
        }

        Pattern isEmptyText = Pattern.compile("\\s+");
        return (name.isEmpty()) || (isEmptyText.matcher(name).matches());
    }
}
