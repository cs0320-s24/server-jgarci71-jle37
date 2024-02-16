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

/**
 * This is a proxy class that wraps an ACD DataSource. This gives our caching proxy the ability to
 * wrap a real API or a mock API.
 */
public class ACSAPIwithCache implements ACSDatasource {

  private final ACSDatasource wrappedAPI;
  private final LoadingCache<StateCountyPair, String[][]> cache;

  // our fallback method when the data is not found in the cache.
  private CacheLoader<StateCountyPair, String[][]> loader =
      new CacheLoader<StateCountyPair, String[][]>() {
        @Override
        public String[][] load(StateCountyPair stateCountyPair) throws Exception {
          return nonCachedQuery(
              stateCountyPair.state().toLowerCase(), stateCountyPair.county().toLowerCase());
        }
      };

  /**
   * (NOT RECOMMENDED) Instantiates a cache with no expiration or maximum size limit.
   *
   * @throws URISyntaxException
   * @throws IOException
   * @throws InterruptedException
   */
  public ACSAPIwithCache() throws URISyntaxException, IOException, InterruptedException {
    this.wrappedAPI = new ACSAPI();
    this.cache = CacheBuilder.newBuilder().build(loader);
  }

  /**
   * Instantiates an ACSAPI with a limit of max size items.
   *
   * @param maxSize number of items the cache can hold at most.
   * @throws URISyntaxException
   * @throws IOException
   * @throws InterruptedException
   */
  public ACSAPIwithCache(long maxSize)
      throws URISyntaxException, IOException, InterruptedException {
    this.wrappedAPI = new ACSAPI();
    this.cache = CacheBuilder.newBuilder().maximumSize(maxSize).build(loader);
  }

  /**
   * Instantiates a cache with an expiration after write.
   *
   * @param expireAfterWrite when should items expire
   * @param durationType time unit
   * @throws URISyntaxException
   * @throws IOException
   * @throws InterruptedException
   */
  public ACSAPIwithCache(long expireAfterWrite, TimeUnit durationType)
      throws URISyntaxException, IOException, InterruptedException {
    this.wrappedAPI = new ACSAPI();
    this.cache =
        CacheBuilder.newBuilder().expireAfterWrite(expireAfterWrite, durationType).build(loader);
  }

  /**
   * Instantiates an API with both a size limit and an expiration time.
   *
   * @param maxSize
   * @param expireAfterWrite
   * @param durationType
   * @throws URISyntaxException
   * @throws IOException
   * @throws InterruptedException
   */
  public ACSAPIwithCache(long maxSize, long expireAfterWrite, TimeUnit durationType)
      throws URISyntaxException, IOException, InterruptedException {
    this.wrappedAPI = new ACSAPI();
    this.cache =
        CacheBuilder.newBuilder()
            .expireAfterWrite(expireAfterWrite, durationType)
            .maximumSize(maxSize)
            .build(loader);
  }

  /**
   * Instantiates a cache, as well as an ACSDatasource to use. This is helpful for using a
   * MockDatasource to perform queries.
   *
   * @param apiToWrap
   * @param maxSize
   * @param expireAfterWrite
   * @param durationType
   */
  public ACSAPIwithCache(
      ACSDatasource apiToWrap, long maxSize, long expireAfterWrite, TimeUnit durationType) {
    this.wrappedAPI = apiToWrap;
    this.cache =
        CacheBuilder.newBuilder()
            .expireAfterWrite(expireAfterWrite, durationType)
            .maximumSize(maxSize)
            .build(loader);
  }

  /**
   * Calls the wrapped ACSDatasource's query method
   *
   * @param state
   * @param county
   * @return
   * @throws IllegalArgumentException
   * @throws ExecutionException
   */
  public String[][] nonCachedQuery(String state, String county)
      throws IllegalArgumentException, ExecutionException {
    return this.wrappedAPI.query(state, county);
  }

  /**
   * Searches the cache for the state county pair.
   *
   * @param state
   * @param county
   * @return
   * @throws IllegalArgumentException
   * @throws ExecutionException
   */
  public String[][] query(String state, String county)
      throws IllegalArgumentException, ExecutionException {
    return this.cache.get(new StateCountyPair(state.toLowerCase(), county.toLowerCase()));
  }

  // -------------------Methods used for Testing-------------------------------------

  public CacheStats getStats() {
    return this.cache.stats();
  }

  public ConcurrentMap<StateCountyPair, String[][]> getCacheItems() {
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
