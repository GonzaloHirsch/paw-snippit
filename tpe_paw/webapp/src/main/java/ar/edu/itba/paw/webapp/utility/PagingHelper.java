package ar.edu.itba.paw.webapp.utility;

public final class PagingHelper {

    /*
     * "The AssertionError isn’t strictly required, but it provides insurance in case the
     *  constructor is accidentally invoked from within the class" - Page 19, Effective Java
     */
    private PagingHelper() {
        throw new AssertionError();
    }

    public static int CalculateTotalPages(int itemsCount, int pageSize){
        return itemsCount / pageSize + (itemsCount % pageSize == 0 ? 0 : 1);
    }
}
