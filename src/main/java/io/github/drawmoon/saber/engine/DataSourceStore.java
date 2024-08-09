/*
 *            _
 *  ___  __ _| |__   ___ _ __
 * / __|/ _` | '_ \ / _ \ '__|
 * \__ \ (_| | |_) |  __/ |
 * |___/\__,_|_.__/ \___|_|
 *
 * Copyright 2024 drash
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.drawmoon.saber.engine;

import com.google.common.base.Stopwatch;
import io.github.drawmoon.saber.common.PruneTimer;
import io.github.drawmoon.saber.exceptions.EngineException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class DataSourceStore implements AutoCloseable {

  private final Map<Integer, StoreItem> store;
  private final PruneTimer pruneTimer;
  private final SaberOptions options;
  private final SaberEventListener listener;

  public DataSourceStore(SaberOptions options, SaberEventListener listener) {
    this.store = new ConcurrentHashMap<>();
    this.pruneTimer = new PruneTimer();
    this.options = Objects.requireNonNull(options);
    this.listener = Objects.requireNonNull(listener);

    // prune every hour
    pruneTimer.schedule(() -> this.prune(false), TimeUnit.HOURS.toMillis(1));
  }

  public SaberDataSource get(RequestContext context) {
    listener.onBeforeGetDataSource(this, options, Objects.requireNonNull(context));

    String connectionUri = Objects.requireNonNull(context.getConnectionUri());
    SaberDataSource dataSource = null;

    Stopwatch stopwatch = Stopwatch.createStarted();
    Exception exception = null;
    try {
      StoreItem item = store.get(connectionUri.hashCode());
      if (item != null) {
        dataSource = item.get();
        dataSource.setContext(context);
      } else {
        dataSource = new SaberDataSource(context);

        long ttl = TimeUnit.DAYS.toMillis(1); // TODO: configurable
        item = new StoreItem(dataSource, ttl);
        store.put(connectionUri.hashCode(), item);
      }
    } catch (Exception e) {
      exception = e;
      throw e;
    } finally {
      long elapsed = stopwatch.stop().elapsed(TimeUnit.MILLISECONDS);
      listener.onAfterGetDataSource(this, options, context, dataSource, elapsed, exception);
    }

    return dataSource;
  }

  public void remove(String connectionUri) {
    int hash = Objects.requireNonNull(connectionUri).hashCode();
    StoreItem item = store.remove(hash);
    if (item != null) {
      try {
        item.close();
      } catch (Exception e) {
        throw new EngineException("Attempt to remove datasource fails", e);
      }
    }
  }

  public void prune(boolean force) {
    store.forEach(
        (hash, item) -> {
          if (force || item.isExpired()) {
            try {
              item.close();
            } catch (Exception expected) {
              // expected, just continue
            }
          }
        });
  }

  @Override
  public void close() throws Exception {
    try {
      pruneTimer.shutdown();
    } catch (Exception expected) {
      // safely close
    }
    try {
      prune(true);
    } catch (Exception expected) {
      // safely close
    }
  }

  static class StoreItem implements AutoCloseable {
    private final SaberDataSource item;
    private final long ttl;
    private volatile long lastAccessTime;

    public StoreItem(SaberDataSource item, long ttl) {
      this.item = Objects.requireNonNull(item);
      this.ttl = ttl;
      this.lastAccessTime = System.currentTimeMillis();
    }

    public boolean isExpired() {
      return System.currentTimeMillis() - lastAccessTime > ttl;
    }

    public SaberDataSource get() {
      lastAccessTime = System.currentTimeMillis();
      return item;
    }

    @Override
    public void close() throws Exception {
      try {
        item.close();
      } catch (Exception expected) {
        // safely close
      }
    }
  }
}
