package model.dictionary;

import junit.framework.Assert;

import org.junit.Test;


/**
 * Created by pietro on 14/05/15.
 */
public class GlobalTest {

    @Test
    public void getLogFromResultShouldReturnGoodString(){
        Assert.assertEquals("Result on Success", Global.getLogFromResult(Global.SUCCESS), " SUCCESS ");
        Assert.assertEquals("Result on Success", Global.getLogFromResult(Global.NOT_FOUND), " NOT FOUND ");
        Assert.assertEquals("Result on Success", Global.getLogFromResult(Global.ALREADY_EXISTS), " ALREADY EXISTS ");
        Assert.assertEquals("Result on Success", Global.getLogFromResult(Global.FAILURE), " FAILURE ");
        Assert.assertEquals("Result on Success", Global.getLogFromResult(Global.OUT_OF_BOUNDS), " OUT OF BOUNDS ");
        Assert.assertEquals("Result on Success", Global.getLogFromResult(Global.NOT_AVAILABLE), " NOT AVAILABLE ");
    }
}