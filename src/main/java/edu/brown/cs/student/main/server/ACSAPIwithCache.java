package edu.brown.cs.student.main.server;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class ACSAPIwithCache implements ACSDatasource {

  private final ACSAPI apiwithcache;
  private final LoadingCache<StateCountyPair, String[][]> cache;
  private CacheLoader<StateCountyPair, String[][]> loader =
      new CacheLoader<StateCountyPair, String[][]>() {
        @Override
        public String[][] load(StateCountyPair stateCountyPair) throws Exception {
          return nonCachedQuery(stateCountyPair.state(), stateCountyPair.county());
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
            CacheBuilder.newBuilder()
                    .expireAfterWrite(expireAfterWrite, durationType)
                    .build(loader);
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


  public String[][] nonCachedQuery(String state, String county) throws IllegalArgumentException {
    System.out.println("performing a non chached query");
    return this.apiwithcache.query(state, county);

  }

  public String[][] query(String state, String county) throws IllegalArgumentException, ExecutionException {
    return this.cache.get(new StateCountyPair(state.toLowerCase(), county.toLowerCase()));
  }

  //---------DataType------------
  private record StateCountyPair(String state, String county) {}

}
