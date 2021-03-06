/*
 * Copyright 2016 Victor Albertos
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.rx_cache.internal.migration;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.rx_cache.Migration;
import rx.Observable;

final class GetClassesToEvictFromMigrations {
    private List<Migration> migrations;

    @Inject public GetClassesToEvictFromMigrations() {}

    GetClassesToEvictFromMigrations with(List<Migration> migrations) {
        this.migrations = migrations;
        return this;
    }

    Observable<List<Class>> react() {
        List<Class> classesToEvict = new ArrayList<>();

        for (Migration migration : migrations) {
            for (Class candidate : migration.evictClasses()) {
                if (!isAlreadyAdded(classesToEvict, candidate)) classesToEvict.add(candidate);
            }
        }

        return Observable.just(classesToEvict);
    }

    private boolean isAlreadyAdded(List<Class> classesToEvict, Class candidate) {
        for (Class aClass : classesToEvict) {
            String className = aClass.getName();
            String classNameCandidate = candidate.getName();
            if (className.equals(classNameCandidate)) {
                return true;
            }
        }

        return false;
    }
}
