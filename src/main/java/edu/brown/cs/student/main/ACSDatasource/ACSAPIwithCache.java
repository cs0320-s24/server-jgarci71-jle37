package edu.brown.cs.student.main.ACSDatasource;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class ACSAPIwithCache implements ACSDatasource {

  private final ACSDatasource apiwithcache;
  private final LoadingCache<StateCountyPair, String[][]> cache;
  private CacheLoader<StateCountyPair, String[][]> loader =
      new CacheLoader<StateCountyPair, String[][]>() {
        @Override
        public String[][] load(StateCountyPair stateCountyPair) throws Exception {
          return nonCachedQuery(stateCountyPair.state().toLowerCase(), stateCountyPair.county().toLowerCase());
        }
      };

  public ACSAPIwithCache() throws URISyntaxException, IOException, InterruptedException {
    this.apiwithcache = new ACSAPI();
    this.cache = CacheBuilder.newBuilder().build(loader);
  }

  public ACSAPIwithCache(long maxSize)
      throws URISyntaxException, IOException, InterruptedException {
    this.apiwithcache = new ACSAPI();
    this.cache = CacheBuilder.newBuilder().maximumSize(maxSize).build(loader);
  }

  public ACSAPIwithCache(long expireAfterWrite, TimeUnit durationType)
      throws URISyntaxException, IOException, InterruptedException {
    this.apiwithcache = new ACSAPI();
    this.cache =
        CacheBuilder.newBuilder().expireAfterWrite(expireAfterWrite, durationType).build(loader);
  }

  public ACSAPIwithCache(long maxSize, long expireAfterWrite, TimeUnit durationType)
      throws URISyntaxException, IOException, InterruptedException {
    this.apiwithcache = new ACSAPI();
    this.cache =
        CacheBuilder.newBuilder()
            .expireAfterWrite(expireAfterWrite, durationType)
            .maximumSize(maxSize)
            .build(loader);
  }

  public ACSAPIwithCache(ACSDatasource apiToWrap, long maxSize, long expireAfterWrite, TimeUnit durationType){
    this.apiwithcache = apiToWrap;
    this.cache =
            CacheBuilder.newBuilder()
                    .expireAfterWrite(expireAfterWrite, durationType)
                    .maximumSize(maxSize)
                    .build(loader);
  }

  public String[][] nonCachedQuery(String state, String county) throws IllegalArgumentException, ExecutionException {
    System.out.println("performing a non chached query");
    return this.apiwithcache.query(state, county);
  }

  public String[][] query(String state, String county)
      throws IllegalArgumentException, ExecutionException {
    return this.cache.get(new StateCountyPair(state.toLowerCase(), county.toLowerCase()));
  }

  //-------------------Methods used for Testing-------------------------------------

  public CacheStats getStats() {
    return this.cache.stats();
  }

  public ConcurrentMap<StateCountyPair, String[][]> getCacheItems(){
    return this.cache.asMap();
  }

  // ---------DataType------------
  public record StateCountyPair(String state, String county) {
    @Override
    public boolean equals(Object o) {
      System.out.println("making a comparison");
      if (this == o) return true;
      if (o == null || this.getClass() != o.getClass()) return false;
      StateCountyPair that = (StateCountyPair) o;
      return this.state.equals(that.state()) && this.county.equals(that.county());
    }

    @Override
    public int hashCode() {
      return Objects.hash(this.state, this.county);
    }
  }
}
