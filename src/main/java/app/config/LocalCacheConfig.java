package app.config;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class LocalCacheConfig {

    int localCacheTTLInMinutes = 100;

    @Bean("nf")
    public Cache<String, List<String>> getNfts() {
        return CacheBuilder.newBuilder().build();
    }

}
