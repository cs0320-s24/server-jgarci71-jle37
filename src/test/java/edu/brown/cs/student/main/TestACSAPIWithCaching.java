package edu.brown.cs.student.main;

import edu.brown.cs.student.main.mocks.MockACSAPI;
import edu.brown.cs.student.main.ACSDatasource.ACSAPIwithCache;
import edu.brown.cs.student.main.ACSDatasource.ACSDatasource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class TestACSAPIWithCaching {

    private ACSAPIwithCache cachedAPI;

    @Before
    public void setupCachedAPI(){
        String[][] data = {
                {"NAME", "S2802_C03_022E", "state", "county"},
                {"Los Angeles County, California", "79.7", "06", "12"}
        };
        ACSDatasource mockApi = new MockACSAPI(data);
        this.cachedAPI = new ACSAPIwithCache(mockApi, 10, 10, TimeUnit.MINUTES);
    }

    @Test
    public void initCacheTest(){
        Assert.assertEquals(0, this.cachedAPI.getStats().hitCount());
        Assert.assertEquals(0, this.cachedAPI.getStats().loadCount());
    }

    @Test
    public void cachePopulatedTest() throws ExecutionException {
        this.cachedAPI.query("California", "Los Angeles County");
        Assert.assertEquals(1, this.cachedAPI.getCacheItems().size());

        this.cachedAPI.query("California", "Los Angeles County");
        Assert.assertEquals(1, this.cachedAPI.getCacheItems().size());
    }
}
