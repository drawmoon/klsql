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

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import io.github.drawmoon.saber.Catalog;
import io.github.drawmoon.saber.Keyword;
import java.util.Objects;
import javax.annotation.Nonnull;

public final class ExpressionContext {

  private final StringBuilder sqlBuilder = new StringBuilder();
  private final Catalog catalog;
  private final SaberOptions options;
  private final SaberEventListener listener;
  private ExpressionContext superCtx;
  private ExpressionContext subCtx;

  public ExpressionContext(Catalog catalog, SaberOptions options, SaberEventListener listener) {
    this.catalog = Objects.requireNonNull(catalog);
    this.options = Objects.requireNonNull(options);
    this.listener = Objects.requireNonNull(listener);
  }

  // -----------------------------------------------------------------------
  public SaberEventListener getListener() {
    return listener;
  }

  public ExpressionContext getSuperCtx() {
    return superCtx;
  }

  public ExpressionContext getSubCtx() {
    return subCtx;
  }

  public ExpressionContext createSubCtx() {
    ExpressionContext subCtx = new ExpressionContext(catalog, options, listener);
    subCtx.superCtx = this;

    this.subCtx = subCtx;
    return subCtx;
  }

  public BasicSqlBuilder createSqlBuilder() {
    return new BasicSqlBuilder(this); // TODO: 根据 SqlDialect 创建对应的 SqlBuilder
  }

  @Nonnull
  @CanIgnoreReturnValue
  public ExpressionContext writeKeyword(Keyword keyword) {
    this.writeSql(keyword.upper()); // TODO: 根据 DSLOptions 确定使用大写还是小写
    return this;
  }

  @Nonnull
  @CanIgnoreReturnValue
  public ExpressionContext writeSql(String sql) {
    return this.autoFormatAppend(sql);
  }

  @Nonnull
  public String render() {
    return this.sqlBuilder.toString();
  }

  // -----------------------------------------------------------------------
  private ExpressionContext autoFormatAppend(String sql) {
    int len = this.sqlBuilder.length();
    if (len != 0 && this.sqlBuilder.charAt(len - 1) != ' ' && !sql.startsWith(" ")) {
      this.sqlBuilder.append(" ");
    }
    this.sqlBuilder.append(sql);
    return this;
  }
}
