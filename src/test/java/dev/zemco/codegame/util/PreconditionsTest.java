package dev.zemco.codegame.util;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PreconditionsTest {

    @Test
    public void checkArgumentNotNullShouldReturnArgumentIfNotNull() {
        Object argument = new Object();
        Object result = Preconditions.checkArgumentNotNull(argument, "Argument name");
        assertThat(result, is(argument));
    }

    @Test
    // TODO:
    public void checkArgumentNotNullShouldThrowIllegalArgumentExceptionContainingArgumentNameIfArgumentIsNull() {
        Assert.fail();
    }

}
