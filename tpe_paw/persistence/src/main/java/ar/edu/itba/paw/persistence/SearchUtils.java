package ar.edu.itba.paw.persistence;

public final class SearchUtils {
    private SearchUtils() {
        throw new AssertionError();
    }

    public static String EscapeSpecialCharacters(String s){
        return s.replace("%", "\\%").replace("_", "\\_");
    }

    public static String TranslateField(String field){
        return "TRANSLATE(lower(" + field + "),'ÀÁÂÃáàâãÉÈÊéèêëÍíÓÒÔÕóòôõÚÜúü','AAAAaaaaEEEeeeeIiOOOOooooUUuu')";
    }
}
