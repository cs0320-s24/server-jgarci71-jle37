package edu.brown.cs.student.main.server;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

public class ACSAPIwithCache {

  private record StateCountyPair(String state, String county) {}
  ;

  private final ACSAPI apiwithcache;
  private final LoadingCache<StateCountyPair, String[][]> cache;
  private CacheLoader<StateCountyPair, String[][]> loader =
      new CacheLoader<StateCountyPair, String[][]>() {
        @Override
        public String[][] load(StateCountyPair stateCountyPair) throws Exception {
          return apiwithcache.query(stateCountyPair.state(), stateCountyPair.county());
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

  public ACSAPIwithCache(long maxSize, long expireAfterWrite)
      throws URISyntaxException, IOException, InterruptedException {
    this.apiwithcache = new ACSAPI();
    this.cache =
        CacheBuilder.newBuilder()
            .expireAfterWrite(expireAfterWrite, TimeUnit.MINUTES)
            .maximumSize(maxSize)
            .build(loader);
  }

  public String[][] query(String state, String county) throws IllegalArgumentException {
    return this.apiwithcache.query(state, county);
  }
}
