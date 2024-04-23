package eco.swarm.cloud.interview.config

import io.r2dbc.spi.ConnectionFactory
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DSLContextConfig {
  @Bean
  fun dslContext(connectionFactory: ConnectionFactory): DSLContext {
    return DSL.using(connectionFactory)
  }
}
