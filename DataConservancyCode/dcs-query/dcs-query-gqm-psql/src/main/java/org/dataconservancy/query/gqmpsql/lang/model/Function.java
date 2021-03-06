/*
 * Copyright 2012 Johns Hopkins University
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
package org.dataconservancy.query.gqmpsql.lang.model;

import java.util.Arrays;

public class Function {
    private final String name;
    private final Object[] arguments;

    public Function(String name, Object... arguments) {
        this.name = name;
        this.arguments = arguments;
    }

    public String name() {
        return name;
    }

    public Object[] arguments() {
        return arguments;
    }

    public int hashCode() {
        return name.hashCode();
    }

    public boolean equals(Object o) {
        if (!(o instanceof Function)) {
            return false;
        }

        Function f = (Function) o;

        return name.equals(f.name) && Arrays.equals(arguments, f.arguments);
    }

    public String toString() {
        return name + "(" + Arrays.toString(arguments) + ")";
    }
}
