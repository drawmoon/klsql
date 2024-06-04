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
package io.github.drawmoon.saber.common;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import com.google.common.collect.ImmutableList;
import java.util.ArrayDeque;
import org.junit.jupiter.api.Test;

public class SequenceTest {

  public void itTest() {}

  public void rangeTest() {}

  @Test
  public void filterTest() {
    Sequence<Integer> list = Sequence.range(1, 10);
    ArrayDeque<Integer> arrayList = Sequence.it(list).filter(i -> i % 2 == 0).toDequeList();
    assertThat(arrayList.toArray(), is(equalTo(ImmutableList.of(2, 4, 6, 8, 10).toArray())));
  }

  @Test
  public void sizeTest() {
    Sequence<Integer> list = Sequence.range(1, 10);
    long size = Sequence.it(list).filter(i -> i % 2 == 0).size();
    assertEquals(5, size);
  }
}
